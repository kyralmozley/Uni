package group27.weatherapp.weather_view;

import group27.weatherapp.AbstractSceneFactory;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Helper for loading the general weather forecast layout, specifying the appropriate XML file and setting coordinates.
 */
public class WeatherSceneFactory extends AbstractSceneFactory {

    private double lat;
    private double lon;

    public void setLatLong(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public Scene loadScene() throws IOException {
        Scene scene = super.loadScene();
        scene.getStylesheets().add("group27/weatherapp/weather_view/weather.css");
        WeatherController controller = (WeatherController) getLoadedController();
        controller.setLatLong(lat, lon);
        controller.postSceneInitialise();
        return scene;
    }

    @Override
    protected String getFxmlFileName() {
        return "weather_view.fxml";
    }
}
