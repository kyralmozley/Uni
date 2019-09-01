package group27.weatherapp.weather_view.mountain_view;

import group27.weatherapp.datasources.location.InMountainRange;
import group27.weatherapp.datasources.weather.basic.BasicWeatherData;
import group27.weatherapp.datasources.weather.basic.DailyBasicPoint;
import group27.weatherapp.datasources.weather.basic.HourlyBasicPoint;
import group27.weatherapp.datasources.weather.mountain.MountainDay;
import group27.weatherapp.datasources.weather.mountain.MountainHeight;
import group27.weatherapp.datasources.weather.mountain.MountainPeriod;
import group27.weatherapp.datasources.weather.mountain.MountainWeatherData;
import group27.weatherapp.weather_view.WeatherNodeController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MountainController extends WeatherNodeController {

    @FXML
    private BorderPane rootpane;

    @FXML
    private BorderPane header;

    @FXML
    private GridPane weatherGrid;

    @FXML
    public Text rainDetails;

    @FXML
    public ImageView hazardImage;

    @FXML
    public ImageView sunriseImage;

    @FXML
    public Text sunriseSunset;

    @FXML
    public Text hazardText;

    private List<HBox> heightList = new ArrayList<>();

    @FXML
    public void initialize() {
    }

    /**
     * This function is called to update the weather view with a new location and time
     *
     * @param lat The location's latitude
     * @param lon The location's longitude
     * @param time The time desired for the weather forecast
     */
    @Override
    public void setLocationAndTime(double lat, double lon, Date time) {
        super.setLocationAndTime(lat, lon, time);

        // Get the mountain and basic weather forecasts
        try {
            MountainWeatherData data = new MountainWeatherData(InMountainRange.inMountainRange(lat, lon));
            MountainDay todaysMountainData = data.getTodaysData();
            BasicWeatherData  todaysBasicData = new BasicWeatherData(lat, lon);

            DailyBasicPoint dailyPoint = todaysBasicData.getSingleDailyData(time);
            HourlyBasicPoint hourlyPoint = todaysBasicData.getSingleHourlyData(time);

            loadData(todaysMountainData, todaysMountainData.getSinglePeriodData(time), dailyPoint, hourlyPoint);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Insert the sunrise/sunset image
        Image image = null;
        try {
            image = new Image(ClassLoader.getSystemResource("res/WeatherIcons/sunrise.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sunriseImage.setImage(image);

        // Set basic header styling
        header.setPadding(new Insets(-5,10,10,10));
        header.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.8), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * When the location and time is set, this procedure is run on the forecasts received
     *
     * @param today The mountain forecast for the day overall
     * @param now The mountain forecast for this period of time
     * @param dailyPoint The basic weather forecast for today
     */
    private void loadData(MountainDay today, MountainPeriod now, DailyBasicPoint dailyPoint, HourlyBasicPoint hourlyPoint) {
        if (now == null) return;

        // Sunrise and sunset times
        DateFormat format = new SimpleDateFormat("HH:mm");
        sunriseSunset.setText("Sunrise: " + format.format(dailyPoint.getSunriseTime())
                +"\nSunset:  " + format.format(dailyPoint.getSunsetTime()));

        // Rain and visibility statistics
        if (hourlyPoint != null) rainDetails.setText("Chance of rain: " + now.getPrecipitationProb() + "\n" +
                    "Visibility: " + hourlyPoint.getVisibility() + " miles");
        else rainDetails.setText("Chance of rain: " + now.getPrecipitationProb());

        // Hazard button
        Map<String, String> parsedHazards = today.getHazards().parseHazards(false);
        if (parsedHazards.isEmpty()) { // If there are no hazards
            // Green hazard symbol

            Image image = null;
            try {
                image = new Image(ClassLoader.getSystemResource("res/HazardIcons/green_hazard.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            hazardImage.setImage(image);

            hazardText.setText("No Hazards");
        }
        else {
            StringBuilder hazardsBuilder = new StringBuilder();
            for (Map.Entry<String, String> hazard : parsedHazards.entrySet()) {
                hazardsBuilder.append(hazard.getKey())
                        .append(": ")
                        .append(hazard.getValue())
                        .append("\n");
            }

            Image image = null;
            try {
                image = new Image(ClassLoader.getSystemResource("res/HazardIcons/yellow_hazard.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            hazardImage.setImage(image);

            GridPane hazardsView = new GridPane();
            int i = 1;
            for (Map.Entry<String, String> hazard : parsedHazards.entrySet()){
                Text hazardName = new Text();
                Text hazardLikelihood = new Text();
                hazardName.setText(hazard.getKey());
                hazardName.setFont(new Font(16));
                hazardLikelihood.setText(hazard.getValue());
                hazardLikelihood.setFont(new Font(12));
                hazardLikelihood.setFill(Color.rgb(50,50,50));
                VBox hazardThing = new VBox(hazardName, hazardLikelihood);
                hazardThing.setPadding(new Insets(0,0,0,10));
                hazardThing.setAlignment(Pos.CENTER_LEFT);
                ImageView hazardImageView = new ImageView();
                hazardImageView.setImage(image);
                hazardImageView.setFitHeight(50);
                hazardImageView.setFitWidth(50);
                HBox hazardRow = new HBox(hazardImageView, hazardThing);
                hazardRow.setPadding(new Insets(10, 10, 10, 10));
                hazardsView.add(hazardRow, 1, i);
                i += 1;
            }
            hazardsView.setPadding(new Insets(10, 10, 10, 10));

            hazardImage.setOnMouseClicked((actionEvent) -> {
                PopOver popOver = new PopOver(hazardsView);
                popOver.setDetachable(false);
                popOver.show(hazardText);
                popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
            });

            hazardText.setOnMouseClicked((actionEvent) -> {
                PopOver popOver = new PopOver(hazardsView);
                popOver.setDetachable(false);
                popOver.show(hazardText);
                popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
            });
        }

        int i = 0;
        // Generate the height specific data, iterating over the heights
        for (MountainHeight height : now.getHeight()) {
            // The surface level text specifies height, wind and temperature details
            //Text text = new Text("\u25bc " + height.getLevel() + " : Wind " + height.getWindSpeed() + "mph " + height.getTemperature() + "\u00b0C");

            Text heightText = new Text("\u25bc " + height.getLevel() + " :  ");
            heightText.setFont(new Font(14));

            Text tempText = new Text(height.getTemperature() + "\u00b0C");
            tempText.setStyle("-fx-font-weight: bold");
            tempText.setFont(new Font(16));

            Text windText = new Text("Wind: " + height.getWindSpeed() + "mph");
            windText.setFont(new Font(14));

            VBox weatherText = new VBox(tempText, windText);
            HBox weatherBox = new HBox(heightText, weatherText);

            weatherBox.setAlignment(Pos.CENTER_LEFT);



            // Horizontal line under text
            Separator s = new Separator();
            s.prefWidthProperty().bind(weatherGrid.widthProperty());

            // Layout for horizontal line
            HBox textBox = new HBox(weatherBox);
            VBox pane = new VBox(textBox, s);
            HBox outer = new HBox(pane);


            weatherGrid.add(outer, 1, i++);

            // More styling for the text
            outer.setFillHeight(false);
            outer.setAlignment(Pos.CENTER_LEFT);
            outer.setPadding(new Insets(0, 0, 0, 10));
            pane.setPadding(new Insets(5,10,5,10));
            pane.setFillWidth(false);
            textBox.setPadding(new Insets(10,20,10,20));
            textBox.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.7), new CornerRadii(3), Insets.EMPTY)));

            // Distributing the height lines evenly
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0/now.getHeight().length);
            weatherGrid.getRowConstraints().add(rowConstraints);
            heightList.add(weatherBox);

            // Popover for additional information when clicking on the height-specific text
            textBox.setOnMouseClicked((actionEvent) -> {
                Text popoverText = new Text(
                        "Wind Direction: " + height.getWindDirection() + "\n" +
                                "Wind Gust: " + height.getMaxGust() + "mph \n" +
                                "Feels Like: " + height.getFeelsLike() + "\u00b0C");
                popoverText.setFont(new Font(14));
                HBox textContainer = new HBox(popoverText);
                textContainer.setPadding(new Insets(10));
                PopOver popOver = new PopOver(textContainer);
                popOver.setDetachable(false);
                popOver.show(weatherBox);
            });
        }
    }
}
