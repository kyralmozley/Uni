package group27.weatherapp.weather_view.daily_view;

import group27.weatherapp.datasources.MountainRange;
import group27.weatherapp.datasources.location.InMountainRange;
import group27.weatherapp.datasources.weather.basic.BasicWeatherData;
import group27.weatherapp.datasources.weather.basic.DailyBasicPoint;
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
 * Controller for the layout displaying daily forecasts, used when hourly data is not available.
 *
 * Once the FXML fields and the time and place are given, it queries DarkSky and fills in the details.
 * If the location is in a mountain region, it also queries Met Office and includes the text-based forecast for it.
 */
public class DailyController extends WeatherNodeController {
    @FXML
    private ImageView weatherIcon;
    @FXML
    private Text forecastDetails;
    @FXML
    private Text temp;
    @FXML
    private HBox detailsBox;
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
            DailyBasicPoint dailyPoint = new BasicWeatherData(lat, lon).getSingleDailyData(time);
            loadData(dailyPoint);
            MountainRange range = InMountainRange.inMountainRange(lat, lon);
            if (range != null) {
                MountainDay day = new MountainWeatherData(range).getDailyData()[2];
                if (day.getSummary() != null) {
                    forecastDetails.setText(forecastDetails.getText() + "\n\n" + day.getSummary());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fills in the layout contents with the given weather data.
     */
    private void loadData(DailyBasicPoint dailyPoint) {
        String iconName = (dailyPoint.getIcon() != null ? dailyPoint.getIcon() : "partly-cloudy-day");
        Image image = null;
        try {
            image = new Image(ClassLoader.getSystemResource("res/WeatherIcons/" + iconName + ".png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        weatherIcon.setImage(image);
        DateFormat format = new SimpleDateFormat("HH:mm");
        temp.setText(Math.round(dailyPoint.getTemperatureLow()) + " - " + Math.round(dailyPoint.getTemperatureHigh()) + "°C");
        forecastDetails.setText(
                "Chance of precipitation: " + Math.round(100*dailyPoint.getPrecipProbability()) + "%\n" +
                        "Wind speed: " + Math.round(dailyPoint.getWindSpeed()) + " mph\n" +
                        "Wind direction: " + dailyPoint.getWindDirection() + "\n" +
                        "Visibility: " + dailyPoint.getVisibility() + " miles \n" +
                        "Sunrise: " + format.format(dailyPoint.getSunriseTime()) + "\n" +
                        "Sunset: " + format.format(dailyPoint.getSunsetTime())
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
