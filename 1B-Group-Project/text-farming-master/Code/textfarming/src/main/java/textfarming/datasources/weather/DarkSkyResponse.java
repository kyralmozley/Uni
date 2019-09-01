package textfarming.datasources.weather;

/**
 * A Java class in the same structure as the JSON data received from the Dark Sky API when requesting the weather
 * forecast for a specific place.
 * Most of the information we get is ignored and not used.
 *
 * @see DailyDataSet
 * @see <a href="http://darksky.net/">Dark Sky</a>
 * @author Benji
 */
class DarkSkyResponse {

    /**
     * The latitude of the requested location
     */
    double latitude;

    /**
     * The longitude of the requested location
     */
    double longitude;

    /**
     * The timezone of the requested location
     */
    String timezone;

    /**
     * The daily weather forecast information.
     *
     * @see DailyDataSet
     */
    DailyDataSet daily;
}
