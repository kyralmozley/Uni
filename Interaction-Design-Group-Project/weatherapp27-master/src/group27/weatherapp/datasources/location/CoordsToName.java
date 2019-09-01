package group27.weatherapp.datasources.location;

import com.google.gson.Gson;
import group27.weatherapp.datasources.MountainRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The top-level class for converting co-ordinates to place names, using Open Street Map's Nominatim Reverse. Much of
 * this is reused from Benji's and Kyra's IB Group project, but adjusted to suit this project's needs
 */
public class CoordsToName {
    // A magic number for OSM to specify the level of detail. Somewhere between 13 and 16 is right, 15 works
    private static final int ZOOM_LEVEL = 15;

    /**
     * Convert a latitude-longitude co-ordinate to a place name
     *
     * @param lat The point's latitude
     * @param lon The point's longitude
     * @return the place's name
     */
    public static String coordsToName(double lat, double lon) {
        MountainRange range = InMountainRange.inMountainRange(lat, lon);
        if (range != null) return range.toString().replaceAll("_", " ");

        try {
            // Request the data
            URL u = new URL("https://nominatim.openstreetmap.org/reverse?format=json&zoom="+ ZOOM_LEVEL + "&" +
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

            if (nominatimResponse.display_name == null) return null;

            String[] splits = nominatimResponse.display_name.split(",");
            if (splits.length==0) return null;
            else if (splits.length==1) return splits[0];
            else return splits[0] + ", " + splits[1];
        }
        catch (IOException e) {
            return null;
        }
    }
}
