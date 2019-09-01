package textfarming.sms.registration.location.what3words;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import textfarming.sms.registration.location.LatLongAcc;

public class What3Words {

    private What3Words() {}

    /**
     * @param threeWords .-delimited three words, no space allowed
     * @param restTemplate
     * @return null if failed; LatLongAcc if successful.
     */
    public static LatLongAcc getLoc(String threeWords, RestTemplate restTemplate) {
        LatLong latLong = restTemplate.getForObject("https://api.what3words.com/v2/forward?addr=" 
                + threeWords + "&key=API_KEY", 
                Response.class).geometry;
        if (latLong == null) return null;
        // because what3words is a 3mx3m square, radius between 1.5 and 2.12m
        return new LatLongAcc(latLong.lat, latLong.lng, 2);
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Response {
    public LatLong geometry;
}

class LatLong {
    public double lat;
    public double lng;
}
