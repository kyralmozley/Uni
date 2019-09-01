package textfarming.datasources.coordstocountry;

/**
 * A Java class in the same structure as the JSON data received as the address field in a OpenStreetMap's Nominatim
 * response. We only use country codes here because they conform to the ISO 3166-1 alpha-2 standard these can be used
 * by the code accessing the data source APIs to convert into the correct format for that API.
 *
 * @author Benji
 * @see <a href="https://nominatim.openstreetmap.org/">OpenStreetMap Nominatim</a>
 */
public class NominatimAddress {
    /**
     * An English name for the country
     */
    String country;

    /**
     * The ISO 3166-1 alpha-2 code for the country, which are two letter codes
     *
     * @see <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">Wikipedia's article on the standard</a>
     */
    String country_code;
}
