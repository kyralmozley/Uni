package group27.weatherapp.weather_view.daily_view;

import group27.weatherapp.AbstractNodeLoader;

/**
 * Helper for loading a daily forecast layout, specifying the appropriate XML file.
 */
public class DailyNodeLoader extends AbstractNodeLoader {

    @Override
    protected String getFxmlFileName() {
        return "daily.fxml";
    }
}
