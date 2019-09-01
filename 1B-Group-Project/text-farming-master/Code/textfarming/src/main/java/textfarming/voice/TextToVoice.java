package textfarming.voice;

import static textfarming.Translator.translate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class TextToVoice {

    @Autowired
    private RestTemplate restTemplate;

    @SuppressWarnings("unused")
    private static class Request {
        public final String engine = "Google";
        public final Data data;

        private Request(String text, String voice) {
            data = new Data(text, voice);
        }
    }

    @SuppressWarnings("unused")
    private static class Data {
        public final String text;
        public final String voice;

        private Data(String text, String voice) {
            this.text = text;
            this.voice = voice;
        }
    }

    private static class IdResponse {
        public final boolean success;
        public final String id;
        public final String message;

        @JsonCreator
        public IdResponse(
                @JsonProperty("id") String id,
                @JsonProperty("success") boolean success,
                @JsonProperty("message") String message
                ) {
            this.id = id;
            this.success = success;
            this.message = message;
        }
    }

    private static class LocationResponse {
        public final String status;
        public final String location;
        public final String message;

        @JsonCreator
        public LocationResponse(
                @JsonProperty("status") String status,
                @JsonProperty("location") String location,
                @JsonProperty("message") String message
                ) {
            this.status = status;
            this.location = location;
            this.message = message;
        }

        private LocationResponse() {
            status = "Pending";
            location = null;
            message = null;
        }
    }

    public String getVoiceURL(String englishText, String textToVoiceCode) {
        Request request = new Request(translate(englishText, textToVoiceCode), textToVoiceCode);
        IdResponse idResponse = restTemplate.postForObject(
                "https://api.soundoftext.com/sounds",
                request,
                IdResponse.class);

        if (!idResponse.success) {
            System.err.println("Text to voice: " + idResponse.message);
            return null;
        }

        String getURL = "https://api.soundoftext.com/sounds/" + idResponse.id;
        LocationResponse locResponse = new LocationResponse();
        while (locResponse.status.equals("Pending"))
            locResponse = restTemplate.getForObject(getURL, LocationResponse.class);

        if (locResponse.location == null) {
            System.err.println("Text to voice: " + locResponse.message);
            return null;
        }

        String encodedURL = "";
        try {
            encodedURL = URLEncoder.encode(locResponse.location, "UTF-8");
        } catch (UnsupportedEncodingException e) {}
        return "http://138.68.187.31:8080/getMP3?path=" + encodedURL;
    }
}
