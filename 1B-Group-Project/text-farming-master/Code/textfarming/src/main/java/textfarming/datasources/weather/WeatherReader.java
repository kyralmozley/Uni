package textfarming.datasources.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;

/**
 * A class containing a function that finds weather information for a given location.
 *
 * @see DarkSkyResponse
 * @see <a href="http://darksky.net/">Dark Sky</a>
 * @author Benji
 */
public class WeatherReader {

    /**
     * Gets the weather information for a given pair of latitude-longitude coordinates
     *
     * @param lat Latitude of the point in degrees
     * @param lon Longitude of the point in degrees
     * @return The response from the API deserialized into a Java object
     * @see DarkSkyResponse
     * @throws IOException If something went wrong with retrieving the data from <a href="http://darksky.net/">Dark Sky</a>
     */
    static DarkSkyResponse getWeatherData(double lat, double lon) throws IOException {
        // Request the data
        URL u = new URL("https://api.darksky.net/forecast/" + DarkSkyKey.API_KEY + "/" + lat + "," + lon+"?units=si&" +
                "exclude=currently,minutely,hourly,alerts,flags");
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
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
        return gson.fromJson(content.toString(), DarkSkyResponse.class);
    }

    /**
     * Helper function to describe a day, such as "Today" or "4 days from now"
     *
     * @param i The index of the day, where 1 is "Today"
     * @return The description of the day
     */
    private static String nextDaysPhrase(int i) {
        if (i == 0) return "Today: ";
        else if (i == 1) return "Tomorrow: ";
        else return i + " days from now: ";
    }

    /**
     * Gets the weather information for a given pair of latitude-longitude coordinates, in simple English
     *
     * @param lat Latitude of the point in degrees
     * @param lon Longitude of the point in degrees
     * @param numDays The number of days of weather data to respond with. A short summary of day {@code numDays + 1} is
     *                given where possible. Dark Sky usually provides a maximum of 8 days of data, but if more data is
     *                requested than can be provided then as much data as possible is provided.
     * @return The weather information requested, as a {@code String} of English
     */
    public static String getWeatherUpdate(double lat, double lon, int numDays) {
        try {
            DarkSkyResponse dsr = getWeatherData(lat, lon);
            StringBuilder sb = new StringBuilder();

            // Bound the number of days we generate text for using the amount of data we have
            if (numDays >= dsr.daily.data.length)
                numDays = dsr.daily.data.length;
            // We can't return a negative amount of data
            if (numDays < 0)
                numDays = 0;

            DateFormat dateFormat = new SimpleDateFormat("ha");   // Format eg. 11am or 3pm
            dateFormat.setTimeZone(TimeZone.getTimeZone(dsr.timezone));  // Localise the times

            for (int i = 0; i<numDays; i++) {
                DailyDataPoint today = dsr.daily.data[i];

                sb.append(nextDaysPhrase(i));

                boolean reportPrecipitation = today.precipIntensityMaxTime != 0;

                sb.append(today.summary)
                .append("\n");

                if (reportPrecipitation)
                    sb.append("At ")
                    .append(dateFormat.format(new Date(1000L*today.precipIntensityMaxTime)))
                    .append(", precipitation is highest, at ")
                    .append(today.precipIntensityMax)
                    .append("mm per hour.\n");

                sb.append("Minimum temperature is ")
                .append(Math.round(today.temperatureMin))
                .append("°C at ")
                .append(dateFormat.format(new Date(1000L*today.temperatureMinTime)))
                .append(".\nMaximum temperature is ")
                .append(Math.round(today.temperatureMax))
                .append("°C at ")
                .append(dateFormat.format(new Date(1000L*today.temperatureMaxTime)))
                .append(".");

                sb.append("\n\n");
            }

            // Bonus summary-only for the next day
            if (numDays != dsr.daily.data.length) {
                sb.append(nextDaysPhrase(numDays))
                .append(dsr.daily.data[numDays].summary)
                .append("\n\n");
            }


            return sb.toString();
        }
        catch (IOException e) {
            System.err.println("Failed to get weather data: ");
            e.printStackTrace(System.err);
            return "Weather data unavailable";
        }
    }
}
