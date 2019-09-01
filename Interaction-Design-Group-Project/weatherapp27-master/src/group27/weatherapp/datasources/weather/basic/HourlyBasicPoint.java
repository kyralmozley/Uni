package group27.weatherapp.datasources.weather.basic;

import java.util.Date;

public class HourlyBasicPoint {
    private final Date time;
    private final String summary;
    private final String icon;
    private final double precipIntensity;
    private final double precipProbability;
    private final String precipType;
    private final double temperature;
    private final double apparentTemperature;
    private final double windSpeed;
    private final double windGust;
    private final int windBearing;
    private final String windDirection;
    private final double visibility;

    public HourlyBasicPoint(HourlyDataPoint dataPoint) {
        summary = dataPoint.summary;
        icon = dataPoint.icon;
        precipIntensity = dataPoint.precipIntensity;
        precipProbability = dataPoint.precipProbability;
        precipType = dataPoint.precipType;
        temperature = dataPoint.temperature;
        apparentTemperature = dataPoint.apparentTemperature;
        windSpeed = dataPoint.windSpeed;
        windGust = dataPoint.windGust;
        windBearing = dataPoint.windBearing;
        visibility = dataPoint.windGust;

        time = new Date(1000L*dataPoint.time);
        windDirection = bearingToCardinalDirection(windBearing);
    }

    public static String bearingToCardinalDirection(int bearing) {
        if (bearing < 0 || bearing > 360) return "";
        if (bearing < 79) {
            if (bearing < 11) return "N";
            else if (bearing < 34) return "NNE";
            else if (bearing < 56) return "NE";
            else return "ENE";
        }
        else if (bearing < 169) {
            if (bearing < 101) return "E";
            else if (bearing < 124) return "ESE";
            else if (bearing < 146) return "SE";
            else return "SSE";
        }
        else if (bearing < 259) {
            if (bearing < 191) return "S";
            else if (bearing < 214) return "SSW";
            else if (bearing < 236) return "SW";
            else return "WSW";
        }
        else {
            if (bearing < 281) return "W";
            else if (bearing < 304) return "WNW";
            else if (bearing < 326) return "NW";
            else if (bearing < 349) return "NNW";
            else return "N";
        }
    }

    public Date getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindGust() {
        return windGust;
    }

    public int getWindBearing() {
        return windBearing;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getVisibility() {
        return visibility;
    }
}
