package textfarming.sms.registration.location.nametocoords;

import static textfarming.InfoMessageGenerator.distanceBetweenCoords;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import textfarming.sms.registration.location.LatLongAcc;

public class NameToCoords {
    // TODO: note the fact that in the current URL format, "q={place}&country={country}",
    // COUNTRY IS IGNORED, and NOT USED
    // To fix it, should use "q={place},{country}"
    // TODO: Decide if this is a bug or a feature

    /**
     * @param place name of a town / city
     * @param countryCode
     * @param restTemplate
     * @return null if failed; LatLongAcc if successful.
     */
    public static LatLongAcc getLoc(String place, String countryCode, 
            RestTemplate restTemplate) {
        Response[] responses = restTemplate.getForObject(
                "https://nominatim.openstreetmap.org/search?format=json&" +
                        "q="+ place + "&country=" + countryCode, Response[].class);
        if (responses == null || responses.length == 0) return null;

        Response best = responses[0];
        for (Response response : responses) {
            if (best.importance < response.importance)
                best = response;
        }

        int accuracy = (int)(distanceBetweenCoords(best.boundingbox[0], best.boundingbox[2],
                best.boundingbox[0], best.boundingbox[3]));
        return new LatLongAcc(best.lat, best.lon, accuracy);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Response {
    public double lat;
    public double lon;
    public double importance;
    public double[] boundingbox;
}
