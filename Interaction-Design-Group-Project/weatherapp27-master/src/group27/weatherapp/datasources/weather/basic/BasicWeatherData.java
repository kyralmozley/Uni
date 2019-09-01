package group27.weatherapp.datasources.weather.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;


/**
 * The top-level class for dealing with normal weather forecasts, using DarkSky.NET. Much of this is reused from Benji's
 * and Kyra's IB Group project, but reformatted to suit this project's needs
 */
public class BasicWeatherData {
    private List<HourlyBasicPoint> hourlyData;
    private List<DailyBasicPoint> dailyData;
    private double lat, lon;

    /**
     * An object representing a basic weather forecast, constructed using  latitude and longitude for the desired
     * location, by requesting it from Dark Sky and parse it
     *
     * @param lat The point's latitude
     * @param lon The point's longitude
     * @throws IOException if there was a network error
     */
    public BasicWeatherData(double lat, double lon) throws IOException {
        DarkSkyResponse dsr = getWeatherData(lat, lon);
        this.lat = lat;
        this.lon = lon;

        hourlyData = Arrays.stream(dsr.hourly.data)
                .map(HourlyBasicPoint::new)
                .collect(Collectors.toList());

        dailyData = Arrays.stream(dsr.daily.data)
                .map(DailyBasicPoint::new)
                .collect(Collectors.toList());
    }

    /**
     * The transport layer code to get the weather forecast parsed using GSon
     *
     * @param lat The point's latitude
     * @param lon The point's longitude
     * @return An object representing the weather forecast similar to the JSON received
     * @throws IOException if there was a network error
     */
    private static DarkSkyResponse getWeatherData(double lat, double lon) throws IOException {
        // Request the data
        URL u = new URL("https://api.darksky.net/forecast/" + DarkSkyKey.API_KEY + "/" + lat + "," + lon+"?units=uk2&" +
                "exclude=currently,minutely,alerts,flags");
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

    public List<HourlyBasicPoint> getHourlyData() {
        return hourlyData;
    }

    public List<DailyBasicPoint> getDailyData() {
        return dailyData;
    }

    /**
     * For a forecast, find the forecast for a specific day
     *
     * @param time Which day
     * @return A representation of the day's forecast
     * @throws IOException if there was a network error
     */
    public DailyBasicPoint getSingleDailyData(Date time) throws IOException {
            Calendar givenTime = Calendar.getInstance();
            givenTime.setTime(time);
            for (DailyBasicPoint dailyPoint : dailyData) {
                Calendar dailyPointTime = Calendar.getInstance();
                dailyPointTime.setTime(dailyPoint.getTime());
                if (dailyPointTime.get(Calendar.DAY_OF_YEAR) == givenTime.get(Calendar.DAY_OF_YEAR)) {
                    return dailyPoint;
                }
            }
            return null;
    }

    /**
     * For a forecast, find the forecast for a specific hour
     *
     * @param time Which day
     * @return A representation of the hour's forecast
     * @throws IOException if there was a network error
     */
    public HourlyBasicPoint getSingleHourlyData(Date time) throws IOException {
        Calendar givenTime = Calendar.getInstance();
        givenTime.setTime(time);
        for (HourlyBasicPoint hourlyPoint : hourlyData) {
            Calendar hourlyPointTime = Calendar.getInstance();
            hourlyPointTime.setTime(hourlyPoint.getTime());
            if (hourlyPointTime.get(Calendar.DAY_OF_YEAR) == givenTime.get(Calendar.DAY_OF_YEAR)
                    && hourlyPointTime.get(Calendar.HOUR_OF_DAY) == givenTime.get(Calendar.HOUR_OF_DAY)) {
                return hourlyPoint;
            }
        }
        return null;
    }
}
