package textfarming.datasources.coordstocountry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import com.google.gson.Gson;

/**
 * A class containing a function that finds the country of a specific point, returned as a Java Locale.
 * Uses OpenStreetMap's Nominatim API.
 *
 * @author Benji
 * @see NominatimReverseResponse
 * @see <a href="https://nominatim.openstreetmap.org/">OpenStreetMap Nominatim</a>
 */
public class CoordsToCountry {

    /**
     * Produces a Java Locale of the country that contains the point specified by the latitude-longitude coordinates
     * provided as arguments
     *
     @param lat Latitude of the point in degrees
     @param lon Longitude of the point in degrees
     @return A Java Locale of the country that contains the given point. The language field will be blank.
     */
    public static Locale coordsToLocale(double lat, double lon) {
        try {
            // Request the data
            URL u = new URL("https://nominatim.openstreetmap.org/reverse?format=json&zoom=0&" +
                    "lat="+ lat + "&lon=" + lon);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //con.setRequestProperty("Accept", "*/*");
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
            NominatimReverseResponse nominatimResponse = gson.fromJson(content.toString(), NominatimReverseResponse.class);

            if (nominatimResponse.address == null) return null;
            else return new Locale("", nominatimResponse.address.country_code);
        }
        catch (IOException e) {
            return null;
        }
    }

}
