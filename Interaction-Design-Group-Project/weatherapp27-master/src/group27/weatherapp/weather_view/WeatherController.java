package group27.weatherapp.weather_view;

import group27.weatherapp.AbstractController;
import group27.weatherapp.AbstractNodeLoader;
import group27.weatherapp.datasources.location.CoordsToName;
import group27.weatherapp.datasources.location.InMountainRange;
import group27.weatherapp.map_view.MapSceneFactory;
import group27.weatherapp.weather_view.hourly_view.HourlyNodeLoader;
import group27.weatherapp.weather_view.mountain_view.MountainNodeLoader;
import group27.weatherapp.weather_view.daily_view.DailyNodeLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Controller for the weather forecast scene layout. Handles time navigation in the footer, titling in the header
 * and creating the appropriate forecast layout in the center.
 */
public class WeatherController implements AbstractController {
    @FXML
    BorderPane root;
    @FXML
    BorderPane headerPane;
    @FXML
    BorderPane footerPane;
    @FXML
    Button backBtn;
    @FXML
    Button prevDayBtn;
    @FXML
    Button nextDayBtn;
    @FXML
    Text headerTxt;
    @FXML
    Text dateTxt;
    @FXML
    Slider dateSlider;

    Parent weatherView;

    Region headerPadding;


    private double lat, lon;
    private int day = 0;
    private int time = 12;
    private boolean isMountainRange = false;

    /**
     * Sets up layout of data presentation and control logic for time slider and next/prev buttons
     * @throws InterruptedException
     */
    public void initialize() throws InterruptedException {
        try {
            Image image = null;
            try {
                image = new Image(ClassLoader.getSystemResource("res/back.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            backBtn.setGraphic(imageView);
            backBtn.setStyle("-fx-background-color: none");
        } catch (Exception e) {
            System.out.println("Exception occured in WeatherController");
        }
        backBtn.setOnAction((actionEvent) -> {
            changeToMapScene(actionEvent);
        });
        dateSlider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double aDouble) {
                return String.format("%.0f:00", aDouble);
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        dateSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            if (!dateSlider.isValueChanging()) {
                if (day == 0) {
                    time = 3 * ((Math.max(new_val.intValue(), getCurrentHour()) + 2) / 3);
                    dateSlider.setValue(time);
                } else {
                    time = new_val.intValue();
                }
                updateWeatherView();
            }
        });
        try {
            Image image = null;
            try {
                image = new Image(ClassLoader.getSystemResource("res/prevDay.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            prevDayBtn.setGraphic(imageView);
            prevDayBtn.setStyle("-fx-background-color: none");


        }catch (Exception e) {

        }
        prevDayBtn.setOnAction((actionEvent) -> {
            if (day == 1) {
                time = 3 * ((Math.max(time, getCurrentHour()) + 2) / 3);
                dateSlider.setValue(time);
            }
            if (day <= 2) {
                dateSlider.setVisible(true);
            }
            if (day > 0) {
                day--;
                updateWeatherView();
                if (day == 0)
                    prevDayBtn.setVisible(false);
                if (day == 3)
                    nextDayBtn.setVisible(true);
            }
        });
        try {
            Image image = null;
            try {
                image = new Image(ClassLoader.getSystemResource("res/nextDay.png").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            nextDayBtn.setGraphic(imageView);
            nextDayBtn.setStyle("-fx-background-color: none");


        }catch (Exception e) {

        }
        nextDayBtn.setOnAction((actionEvent) -> {
            if (day >= 1) {
                dateSlider.setVisible(false);
            }
            if (day < 4) {
                day++;
                if (day == 1)
                    prevDayBtn.setVisible(true);
                if (day == 4)
                    nextDayBtn.setVisible(false);
                updateWeatherView();
            }
        });
        headerPadding = new Region();
        headerPadding.prefWidthProperty().bind(backBtn.widthProperty());
        headerPane.setRight(headerPadding);

        headerPane.setPadding(new Insets(10,10,10,10));
        headerPane.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.8), CornerRadii.EMPTY, Insets.EMPTY)));
        footerPane.setPadding(new Insets(10,10,10,10));
        footerPane.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.9), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     *
     * @return Current hour of the day in UK time.
     */
    private int getCurrentHour() {
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public void postSceneInitialise(){
        headerTxt.wrappingWidthProperty().bind(headerPane.getScene().widthProperty().subtract(headerPadding.widthProperty().multiply(2)).subtract(20));
        headerTxt.setTextAlignment(TextAlignment.CENTER);
    }

    private void changeToMapScene(ActionEvent event) {
        Scene curScene = ((Node)event.getSource()).getScene();
        Stage stage = (Stage) curScene.getWindow();
        try {
            MapSceneFactory sceneFactory = new MapSceneFactory();
            sceneFactory.setDimensions(curScene);
            stage.setScene(sceneFactory.loadScene());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when time slider or next/prev buttons clicked, i.e. time or date is changed.
     * Updates the data (and layout if nec.) displayed accordingly.
     */
    private void updateWeatherView() {
        try {
            AbstractNodeLoader loader;
            if (day == 0 && isMountainRange && time >= 6 && time <= 21) {
                loader = new MountainNodeLoader();
            } else if (day <= 1) {
                loader = new HourlyNodeLoader();
            } else {
                loader = new DailyNodeLoader();
            }
            if (weatherView != null) {
                root.getChildren().remove(weatherView);
            }
            weatherView = loader.loadNode();
            WeatherNodeController weatherNodeController = (WeatherNodeController) loader.getLoadedController();
            Calendar date = Calendar.getInstance();
            date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + day);
            date.set(Calendar.HOUR_OF_DAY, time);
            String pattern = "EEEEE dd MMMMM";
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(pattern);
            dateTxt.setText(simpleDateFormat.format(date.getTime()));
            weatherNodeController.setLocationAndTime(lat, lon, date.getTime());
            root.setCenter(weatherView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows caller to pass location information to WeatherController. Affected fields are set appropriately. In addition, sets the default data displayed for the current day and time.
     * @param lat Latitude of location
     * @param lon Longitude of location
     */
    public void setLatLong(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        isMountainRange = InMountainRange.inMountainRange(lat, lon) != null;
        String placeName = CoordsToName.coordsToName(lat, lon);
        System.out.println("Finding name for: lat: " + lat + " lon: " + lon + " Found: " + placeName);
        headerTxt.setText(placeName != null ? placeName : "Unknown place");
        time = 3 * ((getCurrentHour() + 2) / 3);
        System.out.println(getCurrentHour());
        dateSlider.setValue(time);
        updateWeatherView();
    }
}
