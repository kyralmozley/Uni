package group27.weatherapp.weather_view.mountain_view;

import group27.weatherapp.AbstractNodeLoader;

/**
 * Helper for loading a mountain forecast layout, specifying the appropriate XML file.
 */
public class MountainNodeLoader extends AbstractNodeLoader {

    @Override
    protected String getFxmlFileName() {
        return "mountain.fxml";
    }
}
