package textfarming.datasources.weather;

/**
 * A Java class in the same structure as the JSON data received from the Dark Sky API when requesting the weather
 * forecast for a specific place. This structure is inside of the overall response.
 * Most of the information we get is ignored and not used.
 *
 * @see <a href="http://darksky.net/">Dark Sky</a>
 * @author Benji
 */
class DailyDataPoint {
    int time;
    String summary;
    String icon;
    int sunriseTime;
    int sunsetTime;
    double moonPhase;
    double precipIntensity;
    double precipIntensityMax;
    int precipIntensityMaxTime;
    double precipProbability;
    double precipAccumulation;
    String precipType;
    double temperatureHigh;
    int temperatureHighTime;
    double temperatureLow;
    int temperatureLowTime;
    double apparentTemperatureHigh;
    int apparentTemperatureHighTime;
    double apparentTemperatureLow;
    int apparentTemperatureLowTime;
    double dewPoint;
    double humidity;
    double pressure;
    double windSpeed;
    double windGust;
    int windGustTime;
    int windBearing;
    double cloudCover;
    int uvIndex;
    int uvIndexTime;
    double visibility;
    double ozone;
    double temperatureMin;
    int temperatureMinTime;
    double temperatureMax;
    int temperatureMaxTime;
    double apparentTemperatureMin;
    int apparentTemperatureMinTime;
    double apparentTemperatureMax;
    int apparentTemperatureMaxTime;
}
