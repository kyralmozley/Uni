package textfarming.datasources.coordstocountry;

/**
 * A Java class in the same structure as the JSON data received from a OpenStreetMap's Nominatim response.
 * Most of the information we get is ignored and not used
 *
 * @author Benji
 * @see NominatimAddress
 * @see <a href="https://nominatim.openstreetmap.org/">OpenStreetMap Nominatim</a>
 */
public class NominatimReverseResponse {
    String place_id;
    String licence;
    String osm_type;
    String osm_id;
    double lat;
    double lon;
    String display_name;

    /**
     * This is the object that contains the address of the point we submit to Nominatim
     *
     * @see NominatimAddress
     */
    NominatimAddress address;

    double[] boundingbox;
}
