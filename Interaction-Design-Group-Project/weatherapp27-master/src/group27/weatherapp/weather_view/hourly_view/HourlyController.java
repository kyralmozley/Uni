package group27.weatherapp.weather_view.hourly_view;

import group27.weatherapp.datasources.MountainRange;
import group27.weatherapp.datasources.location.InMountainRange;
import group27.weatherapp.datasources.weather.basic.BasicWeatherData;
import group27.weatherapp.datasources.weather.basic.DailyBasicPoint;
import group27.weatherapp.datasources.weather.basic.HourlyBasicPoint;
import group27.weatherapp.datasources.weather.mountain.MountainDay;
import group27.weatherapp.datasources.weather.mountain.MountainWeatherData;
import group27.weatherapp.weather_view.WeatherNodeController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Controller for the layout displaying hourly forecasts. Used for tomorrow in mountain regions with a combination of
 * Met Office mountain data and DarkSky forecasts, and for the current day as well outside mountain regions using just
 * DarkSky.
 *
 * Once the FXML fields and the time and place are given, it queries these weather providers and fills in the details.
 */
public class HourlyController extends WeatherNodeController {
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Text forecastDetails;
    @FXML
    private Text temp;
    @FXML
    HBox detailsBox;
    @FXML
    HBox rootContainer;
    @FXML
    ScrollPane scrollpane;

    /**
     * Sets location and time and updates the contents accordingly, querying the data sources in the process.
     */
    @Override
    public void setLocationAndTime(double lat, double lon, Date time) {
        super.setLocationAndTime(lat, lon, time);
        try {
            BasicWeatherData data = new BasicWeatherData(lat, lon);
            loadData(data.getSingleHourlyData(time), data.getSingleDailyData(time));
            MountainRange range = InMountainRange.inMountainRange(lat, lon);
            if (range != null) {
                MountainDay tomorrow = new MountainWeatherData(range).getDailyData()[1];
                if (tomorrow.getWeather() != null) {
                    forecastDetails.setText(forecastDetails.getText() + "\n\nDaily forecast:\n"+ tomorrow.getWeather());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Fills in the layout contents with the given weather data.
     */
    private void loadData(HourlyBasicPoint hourlyPoint, DailyBasicPoint dailyData) {
        String iconName = (hourlyPoint.getIcon() != null ? hourlyPoint.getIcon() : "partly-cloudy-day");
        Image image = null;
        try {
            image = new Image(ClassLoader.getSystemResource("res/WeatherIcons/" + iconName + ".png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        weatherIcon.setImage(image);
        DateFormat format = new SimpleDateFormat("HH:mm");
        DateFormat hourFormat = new SimpleDateFormat("h a");
        String hour = hourFormat.format(hourlyPoint.getTime());
        temp.setText(Math.round(hourlyPoint.getTemperature()) + "°C");
        forecastDetails.setText(
                "Weather at "+hour+":\n"+
                "\tChance of precipitation: " + Math.round(100*hourlyPoint.getPrecipProbability()) + "%\n" +
                "\tWind speed: " + Math.round(hourlyPoint.getWindSpeed()) + " mph\n" +
                "\tWind direction: " + hourlyPoint.getWindDirection() + "\n" +
                "\tVisibility: " + Math.round(hourlyPoint.getVisibility()) + " miles\n" +
                "\tFeels like: " + Math.round(hourlyPoint.getApparentTemperature()) + "°C" + "\n\n" +
                "Sunrise: " + format.format(dailyData.getSunriseTime()) + "\n" +
                "Sunset: " + format.format(dailyData.getSunsetTime())
        );
    }

    @FXML
    public void initialize() {
        detailsBox.setPadding(new Insets(10,10,10,10));
        scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        detailsBox.prefWidthProperty().bind(rootContainer.widthProperty().subtract(20));
        detailsBox.setMaxWidth(500);
        forecastDetails.wrappingWidthProperty().bind(detailsBox.widthProperty().subtract(20));
    }
}
