package textfarming.datasources.market;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

 /**
  * A class containing a function that finds crop price information, and converting it into local currencies
  *
  * @author Benji
  * @see CurrencyConverter
  * @see <a href="http://www.foodsecurityportal.org/api/">Food Security Portal</a>
  * @see <a href="https://nominatim.openstreetmap.org/">OpenStreetMap Nominatim</a>
  */
public class CropPriceReader {
     /**
      * Helper function to convert abbreviated three-letter month codes into full month names. This gives the
      * translation API a better chance at doing a sensible job.
      *
      * @param threeLetterMonth The three-letter month code
      * @return The full name of the month
      */
    private static String monthConvert(String threeLetterMonth) {
        switch (threeLetterMonth) {
            case "Jan":
                return "January";
            case "Feb":
                return "February";
            case "Mar":
                return "March";
            case "Apr":
                return "April";
            case "May":
                return "May";
            case "Jun":
                return "June";
            case "Jul":
                return "July";
            case "Aug":
                return "August";
            case "Sep":
                return "September";
            case "Oct":
                return "October";
            case "Nov":
                return "November";
            case "Dec":
                return "December";
            default:
                return "";
        }
    }

     /**
      * A function that finds crop price information, for a given crop, and converting it into local currencies.
      * If the currency conversion fails, return the result in the original format which is USD/kg. If the data isn't
      * available return a message explaining this.`
      *
      * @param locale The location to find the local crop price for, and the currency information. Stored in a Java
      *               Locale, but the language information in it is ignored
      * @param crop Which crop to get prices for
      * @return A string of a crop price and currency name, or a message explaining data wasn't available
      * @see Crop
      * @see CurrencyConverter
      */
    public static String getCropPrice(Locale locale, Crop crop) {
        String originalCountry = locale.getDisplayCountry();
        String country = originalCountry;
        String cropName = "";

        try {
            // Reformat the country string
            country = country.replaceAll("[^a-zA-Z\\s]", "");  // Remove non-alphabetic characters
            country = country.replaceAll("[\\s+]", "%20");  // Replace whitespace with %20 code
            country = country.toLowerCase();

            URL u;
            if (crop == Crop.MAIZE) {
                cropName = "Maize";
                u = new URL("http://www.foodsecurityportal.org/api/countries/maize/" + country + ".csv");
            }
            else { // crop == Crop.RICE
                cropName = "Rice";
                u = new URL("http://www.foodsecurityportal.org/api/countries/rice/" + country + ".csv");
            }

            // Set up connection
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            // int status = con.getResponseCode();

            // Read in the data
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // Parse data, yielding a record of months followed by a record of values in USD/kg
            List<CSVRecord> records = CSVFormat.RFC4180.parse(in).getRecords();

            // Find most recent data point
            int bestIndex = records.get(1).size() - 1;
            while (records.get(1).get(bestIndex).equals("")) bestIndex--;

            if (bestIndex == 0) return "Market data unavailable";
            else {
                double dollarAmount = Double.parseDouble(records.get(1).get(bestIndex));

                String convertedAmount = CurrencyConverter.convertCurrency(dollarAmount, Locale.US, locale);

                String rawDate = records.get(0).get(bestIndex); // Of form "Dec 18"
                String date = monthConvert(rawDate.substring(0,3)) + " 20" + rawDate.substring(4,6);

                StringBuilder sb = new StringBuilder();
                sb.append(cropName).append(" price from ").append(date).append(": ");

                if (convertedAmount.equals("")) {
                    sb.append(String.format("%.2f", dollarAmount)).append(" United States Dollars per kg");
                }
                else {
                    sb.append(convertedAmount).append(" per kg");
                }

                return sb.toString();
            }
        }
        catch (IOException e) {
            System.err.println("Failed to get market data: ");
            System.err.println(e.getMessage());
            return "Market data unavailable";
        }
        catch (IndexOutOfBoundsException e) {
            return "No " + cropName + " data found for " + originalCountry;
        }
    }
}
