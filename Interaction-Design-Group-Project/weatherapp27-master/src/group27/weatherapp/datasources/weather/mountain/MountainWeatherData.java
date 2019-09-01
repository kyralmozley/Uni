package group27.weatherapp.datasources.weather.mountain;

import com.google.gson.Gson;
import group27.weatherapp.datasources.MountainRange;
import group27.weatherapp.datasources.weather.basic.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The top-level class for dealing with mountain weather forecasts, using DarkSky.NET. MetOffice DataPoint
 */
public class MountainWeatherData {
    private MountainDay todaysData;
    private MountainDay[] dailyData;
    private MountainRange place;

    /**
     * An object representing a mountain weather forecast, constructed for a given mountain range
     * location, by requesting it from the Met Office and parse it
     *
     * @param place An enum value representing a mountain range
     * @throws IOException if there is a network error
     */
    public MountainWeatherData(MountainRange place) throws IOException {
        this.place = place;

        MountainResponse response = getWeatherData(place);

        this.dailyData = response.Report.Days.Day;
        this.todaysData = response.Report.Days.Day[0];
    }

    /**
     * The transport layer code to get the weather forecast parsed using GSon
     *
     * @param place An enum value representing a mountain range
     * @return An object representing the weather forecast similar to the JSON received
     * @throws IOException if there was a network error
     */
    private static MountainResponse getWeatherData(MountainRange place) throws IOException {
        if (place == null) return null;

        // Request the data
        URL u = new URL("http://datapoint.metoffice.gov.uk/public/data/txt/wxfcs/mountainarea/json/" + place +
                "?key=" + MetOfficeKey.API_KEY);
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
        return gson.fromJson(content.toString(), MountainResponse.class);
    }

    public MountainDay getTodaysData() {
        return todaysData;
    }

    public MountainDay[] getDailyData() {
        return dailyData;
    }

    public MountainRange getPlace() {
        return place;
    }
}
