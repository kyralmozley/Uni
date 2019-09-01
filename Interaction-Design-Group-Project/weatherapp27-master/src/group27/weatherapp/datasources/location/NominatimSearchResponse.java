package group27.weatherapp.datasources.location;

public class NominatimSearchResponse {
    String place_id;
    String licence;
    String osm_type;
    String osm_id;
    public double lat;
    public double lon;
    public String display_name;

    String type;
    double importance;

    public double[] boundingbox;
}
