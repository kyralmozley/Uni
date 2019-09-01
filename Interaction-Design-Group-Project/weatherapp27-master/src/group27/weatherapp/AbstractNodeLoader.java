package group27.weatherapp;

import group27.weatherapp.weather_view.WeatherNodeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Wrapper around FXMLLoader where subclasses can specify the file to load, and the controller can be queried after loading.
 */
public abstract class AbstractNodeLoader {
    protected abstract String getFxmlFileName();

    private FXMLLoader loader;

    /**
     * Load the JavaFX node from the file given by getFxmlFileName().
     */
    public Parent loadNode() throws IOException {
        loader = new FXMLLoader(getClass().getResource(getFxmlFileName()));
        return loader.load();
    }

    /**
     * Return the controller of the last loaded node if there is one, null if we never loaded.
     */
    public AbstractController getLoadedController() {
        if (loader == null)
            return null;
        return loader.getController();
    }
}
