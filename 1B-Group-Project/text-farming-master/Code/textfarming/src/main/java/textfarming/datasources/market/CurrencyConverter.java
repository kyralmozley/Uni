package textfarming.datasources.market;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.MissingResourceException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * A singleton class used for converting between currencies. The state is the list of currently supported countries,
 * and the processing of this only needs to be done very occasionally so we build this every time the server starts,
 * and use it for the duration of execution.
 *
 * Currency conversion rates are obtained from <a href="http://currencyconverterapi.com/">Currency Converter API</a>
 *
 * @author Benji
 */
public class CurrencyConverter {
    /**
     * The instance of the singleton.
     */
    private static CurrencyConverter instance;

    /**
     * The list of countries currently supported by the <a href="http://currencyconverterapi.com/">Currency Converter API</a>.
     */
    private CurrencyConverterCountryResponse countrySet;

    /**
     * Get the instance of the Converter if it already exists, else construct one.
     *
     * @return The single CurrencyConverter instance
     * @throws IOException If the construction of the instance fails, which occurs if the
     * <a href="http://currencyconverterapi.com/">Currency Converter API</a> is unreachable when making the set of
     * countries.
     */
    public static synchronized CurrencyConverter getInstance() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        if (instance == null) {
            instance = new CurrencyConverter();
        }

        return instance;
    }

    /**
     * Construct the instance of the singleton, which involves fetching and parsing the list of countries supported
     * by the converter API.
     *
     * @throws IOException If the <a href="http://currencyconverterapi.com/">Currency Converter API</a> is unreachable
     * when making the set of countries.
     */
    private CurrencyConverter() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // Request the data
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, null, new java.security.SecureRandom());

        URL u = new URL("https://free.currencyconverterapi.com/api/v6/countries?apiKey=" + CurrencyConverterKey.API_KEY);
        HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        //System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        // int status = con.getResponseCode();

        // Read in the data
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        // Parse the json data and put it into custom classes
        Gson gson = new Gson();
        countrySet = gson.fromJson(content.toString(), CurrencyConverterCountryResponse.class);

    }

    /**
     * Gets the country converter API country-identity of a given Java Locale.
     *
     * @param locale The Java Locale to convert
     * @return The Country Converter API country-identity of the requested country
     *
     * @see CurrencyConverterCountry
     */
    CurrencyConverterCountry getCountry(Locale locale) {
        String targetCode;
        try {
            targetCode = locale.getISO3Country();
        }
        catch(MissingResourceException e) {
            targetCode = null;
        }
        if (targetCode == null) targetCode = CountryCodes.countryCodes.get(
                StringUtils.capitalize(locale.getDisplayCountry().toLowerCase()));

        for (CurrencyConverterCountry country : countrySet.results.values()) {
            if (country.alpha3.equals(targetCode)) {
                return country;
            }
        }

        return null;
    }

    /**
     * For given start and end countries, return the amount of start country currency units for 1 target country
     * currency unit. This value can be multiplied by an amount of start currency units to give an amount in target
     * currency units
     *
     * @param start The start country
     * @param target The target country
     * @return The amount of target country currency units for 1 start country currency unit.
     * @throws IOException If the <a href="http://currencyconverterapi.com/">Currency Converter API</a> is unreachable
     * when making the set of countries.
     */
    double getConversionRatio(CurrencyConverterCountry start, CurrencyConverterCountry target) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String currencyPair = start.currencyId + "_" + target.currencyId;

        // Request the data
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, null, new java.security.SecureRandom());

        URL u = new URL("https://free.currencyconverterapi.com/api/v6/convert?apiKey="+
                CurrencyConverterKey.API_KEY + "&q=" + currencyPair);
        HttpsURLConnection con = (HttpsURLConnection) u.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        // int status = con.getResponseCode();

        // Read in the data
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        // Parse the json data and put it into custom classes
        Gson gson = new Gson();
        CurrencyConversionResponse response = gson.fromJson(content.toString(), CurrencyConversionResponse.class);

        return response.results.get(currencyPair).val;
    }

    /**
     * For given start and end countries, convert an amount of start country currency units into target country
     * currency units. Return this as a string, giving the value to 2 d.p. with the name of the target currency units
     * in it.
     *
     * @param amount The amount of start currency units to be converted
     * @param start The start country
     * @param target The target country
     * @return The amount in target currency units to 2 d.p. with the name of the currency units. Returns the empty
     * string if conversion fails.
     */
    static String convertCurrency(double amount, Locale start, Locale target) {
        try {
            CurrencyConverter instance = getInstance();
            CurrencyConverterCountry startCountry = instance.getCountry(start);
            CurrencyConverterCountry targetCountry = instance.getCountry(target);

            if (targetCountry != null) {
                return String.format("%.2f", amount * instance.getConversionRatio(startCountry, targetCountry))
                        + " " + targetCountry.currencyName;
            }
            else {
                return "";
            }
        }
        catch (IOException | IndexOutOfBoundsException | NullPointerException | NoSuchAlgorithmException |
                KeyManagementException e) {
            e.printStackTrace();
            System.out.println("\n\n\n");
            System.out.println(e.getMessage());
            return "";
        }
    }
}
