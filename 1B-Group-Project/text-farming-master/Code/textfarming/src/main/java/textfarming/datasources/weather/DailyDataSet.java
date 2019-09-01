package textfarming.datasources.weather;

/**
 * A Java class in the same structure as the JSON data received from the Dark Sky API when requesting the weather
 * forecast for a specific place. This structure is inside of the overall response.
 * Most of the information we get is ignored and not used.
 *
 * @see DailyDataPoint
 * @see <a href="http://darksky.net/">Dark Sky</a>
 * @author Benji
 */
class DailyDataSet {
    /**
     * A textual summary of the weather forecast for the upcoming week
     */
    String summary;

    /**
     * Symbol name such as "rain" or "sunny" to describe the upcoming week
     */
    String icon;

    /**
     * A list of weather forecasts for each day.
     *
     * @see DailyDataPoint
     */
    DailyDataPoint[] data;
}
