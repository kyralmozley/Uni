package group27.weatherapp.datasources.location;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The top-level class for converting co-ordinates to place names, using Open Street Map's Nominatim Search. Much of
 * this is reused from Benji's and Kyra's IB Group project, but adjusted to suit this project's needs
 */
public class NameToCoords {

    /**
     * Searches a place name and gives an object containing lat-long co-ordinates
     *
     * @param place A place name
     * @return An object returned by OSM Search, containing lat-long co-ordinates
     */
    public static NominatimSearchResponse nameToCoords(String place) {
        place = place.trim().replaceAll(" +", " ").replaceAll(" ", "+");

        try {
            // Request the data
            URL u = new URL("https://nominatim.openstreetmap.org/search?format=json&" +
                    "q="+ place);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            System.setProperty("http.agent", "");


            int status = con.getResponseCode();

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
            NominatimSearchResponse[] nominatimResponses = gson.fromJson(content.toString(), NominatimSearchResponse[].class);

            if (nominatimResponses != null && nominatimResponses.length != 0) {
                NominatimSearchResponse best = nominatimResponses[0];

                for (NominatimSearchResponse response : nominatimResponses) {
                    if (best.importance < response.importance) {
                        best = response;
                    }
                }

                return best;
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}