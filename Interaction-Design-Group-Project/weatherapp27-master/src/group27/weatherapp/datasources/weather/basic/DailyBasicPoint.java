package group27.weatherapp.datasources.weather.basic;

import java.util.Date;

public class DailyBasicPoint {
    private final Date time;
    private final String summary;
    private final String icon;
    private final Date sunriseTime;
    private final Date sunsetTime;
    private final double moonPhase;
    private final double precipIntensity;
    private final double precipIntensityMax;
    private final Date precipIntensityMaxTime;
    private final double precipProbability;
    private final double precipAccumulation;
    private final String precipType;
    private final double temperatureHigh;
    private final Date temperatureHighTime;
    private final double temperatureLow;
    private final Date temperatureLowTime;
    private final double apparentTemperatureHigh;
    private final Date apparentTemperatureHighTime;
    private final double apparentTemperatureLow;
    private final Date apparentTemperatureLowTime;
    private final double windSpeed;
    private final double windGust;
    private final Date windGustTime;
    private final int windBearing;
    private final String windDirection;
    private final double visibility;
    private final double temperatureMin;
    private final Date temperatureMinTime;
    private final double temperatureMax;
    private final Date temperatureMaxTime;
    private final double apparentTemperatureMin;
    private final Date apparentTemperatureMinTime;
    private final double apparentTemperatureMax;
    private final Date apparentTemperatureMaxTime;

    DailyBasicPoint(DailyDataPoint dataPoint) {
        summary = dataPoint.summary;
        icon = dataPoint.icon;
        moonPhase = dataPoint.moonPhase;
        precipIntensity = dataPoint.precipIntensity;
        precipProbability = dataPoint.precipProbability;
        precipIntensityMax = dataPoint.precipIntensityMax;
        precipAccumulation = dataPoint.precipAccumulation;
        temperatureHigh = dataPoint.temperatureHigh;
        apparentTemperatureHigh = dataPoint.apparentTemperatureHigh;
        temperatureLow = dataPoint.temperatureLow;
        apparentTemperatureLow = dataPoint.apparentTemperatureLow;
        precipType = dataPoint.precipType;
        temperatureMin = dataPoint.temperatureMin;
        apparentTemperatureMin = dataPoint.apparentTemperatureMin;
        temperatureMax = dataPoint.temperatureMax;
        apparentTemperatureMax = dataPoint.apparentTemperatureMax;
        windSpeed = dataPoint.windSpeed;
        windGust = dataPoint.windGust;
        windBearing = dataPoint.windBearing;
        visibility = dataPoint.windGust;

        time = new Date(1000L*dataPoint.time);
        sunriseTime = new Date(1000L*dataPoint.sunriseTime);
        sunsetTime = new Date(1000L*dataPoint.sunsetTime);
        precipIntensityMaxTime= new Date(1000L*dataPoint.precipIntensityMaxTime);
        temperatureHighTime= new Date(1000L*dataPoint.temperatureHighTime);
        apparentTemperatureHighTime= new Date(1000L*dataPoint.apparentTemperatureHighTime);
        temperatureLowTime= new Date(1000L*dataPoint.temperatureLowTime);
        apparentTemperatureLowTime = new Date(1000L*dataPoint.apparentTemperatureLowTime);
        windGustTime = new Date(1000L*dataPoint.windGustTime);
        temperatureMinTime = new Date(1000L*dataPoint.temperatureMinTime);
        temperatureMaxTime = new Date(1000L*dataPoint.temperatureMaxTime);
        apparentTemperatureMinTime = new Date(1000L*dataPoint.apparentTemperatureMinTime);
        apparentTemperatureMaxTime = new Date(1000L*dataPoint.apparentTemperatureMaxTime);
        windDirection = HourlyBasicPoint.bearingToCardinalDirection(windBearing);
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

    public Date getSunriseTime() {
        return sunriseTime;
    }

    public Date getSunsetTime() {
        return sunsetTime;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public double getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public Date getPrecipIntensityMaxTime() {
        return precipIntensityMaxTime;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public double getPrecipAccumulation() {
        return precipAccumulation;
    }

    public String getPrecipType() {
        return precipType;
    }

    public double getTemperatureHigh() {
        return temperatureHigh;
    }

    public Date getTemperatureHighTime() {
        return temperatureHighTime;
    }

    public double getTemperatureLow() {
        return temperatureLow;
    }

    public Date getTemperatureLowTime() {
        return temperatureLowTime;
    }

    public double getApparentTemperatureHigh() {
        return apparentTemperatureHigh;
    }

    public Date getApparentTemperatureHighTime() {
        return apparentTemperatureHighTime;
    }

    public double getApparentTemperatureLow() {
        return apparentTemperatureLow;
    }

    public Date getApparentTemperatureLowTime() {
        return apparentTemperatureLowTime;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindGust() {
        return windGust;
    }

    public Date getWindGustTime() {
        return windGustTime;
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

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public Date getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public Date getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public Date getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public Date getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }
}
