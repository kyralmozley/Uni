package group27.weatherapp.weather_view.hourly_view;

import group27.weatherapp.AbstractNodeLoader;

/**
 * Helper for loading an hourly forecast layout, specifying the appropriate XML file.
 */
public class HourlyNodeLoader extends AbstractNodeLoader {

    @Override
    protected String getFxmlFileName() {
        return "hourly.fxml";
    }
}
