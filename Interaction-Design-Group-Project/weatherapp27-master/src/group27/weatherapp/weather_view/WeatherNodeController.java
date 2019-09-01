package group27.weatherapp.weather_view;

import group27.weatherapp.AbstractController;

import java.util.Date;

/**
 * Superclass for controllers of forecast layout (mountain, hourly, daily).
 * All of these need to receive location and time data, hence this is implemented here.
 */
public abstract class WeatherNodeController implements AbstractController {

    protected double lat, lon;
    protected Date time;

    /**
     * Sets the latitude, longitude and time of the forecast to display.
     */
    public void setLocationAndTime(double lat, double lon, Date time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }
}
