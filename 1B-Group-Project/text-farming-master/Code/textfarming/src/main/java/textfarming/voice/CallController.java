package textfarming.voice;

import static textfarming.InfoMessageGenerator.generateUserMessage;
import static textfarming.InfoMessageGenerator.splitMessage;
import static textfarming.persistence.User.startRegistration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Play;
import com.twilio.twiml.voice.Record;
import com.twilio.twiml.voice.Redirect;

import textfarming.persistence.CustomUpdateMessageService;
import textfarming.persistence.User;
import textfarming.persistence.UserService;
import textfarming.sms.registration.LanguageQuerier;
import textfarming.sms.registration.RegistrationState;

@RestController
public class CallController {

    @Autowired
    private CustomUpdateMessageService customUpdateMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TextToVoice textToVoice;

    @PostMapping("/callIn")
    public void handleCall(HttpServletRequest request, HttpServletResponse response){
        String source = request.getParameter("From");
        String countryCode = request.getParameter("FromCountry");
        response.setContentType("text/xml; charset=UTF-8");

        User user;
        if(!userService.contains(source))
            user = startRegistration(source, countryCode, userService);
        else
            user = userService.getUser(source);
        VoiceResponse.Builder builder = new VoiceResponse.Builder();


        if (!user.regis_getState().equals(RegistrationState.REGISTERED)) {
            String text = "You have not registered yet, please respond to the registration messages. Thank you.";

            List<String> languages;
            if (user.getLang() == null) 
                languages = LanguageQuerier.getLanguages(countryCode);
            else {
                languages = new LinkedList<>();
                languages.add(user.getLang());
            }

            for (String lang : languages) {
                String voiceURL = textToVoice.getVoiceURL(text, lang);
                builder = builder.play(new Play.Builder(voiceURL).build());
            }




        } else {
            String text_1 = "Welcome. Click 1 to get a new update."
                    + " Click 2 to report an issue with your crops.";
            String text_2 = "Click 3 to change your settings. Click 4 to hear this again.";

            String voiceURL_1 = textToVoice.getVoiceURL(text_1, user.getLang());
            String voiceURL_2 = textToVoice.getVoiceURL(text_2, user.getLang());

            Gather gather = new Gather.Builder().play(new Play.Builder(voiceURL_1).build()).play(new Play.Builder(voiceURL_2).build())
                    .action("/menu").numDigits(1).build();

            builder = builder.gather(gather);
        }

        try {
            response.getWriter().print(builder.build().toXml());
        } catch (TwiMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/menu")
    public void handleMenu(HttpServletRequest request, HttpServletResponse response) {
        String from = request.getParameter("From");
        String countryCode = request.getParameter("FromCountry");
        User user = userService.getUser(from);
        String selectedOption = request.getParameter("Digits");
        response.setContentType("text/xml; charset=UTF-8");

        VoiceResponse.Builder builder = new VoiceResponse.Builder();



        if (selectedOption.equals("1")) {
            String message = generateUserMessage(user.tel, userService, customUpdateMessageService);
            user.voice_setMessage(message);
            userService.save(user);

            (new Thread(new Runnable() {
                public void run() {
                    for (String seg : splitMessage(message))
                        textToVoice.getVoiceURL(seg, user.getLang());
                }})).start();

            builder = builder.redirect(new Redirect.Builder("/playMsg").build());





        } else if (selectedOption.equals("2")) {
            String text = "Please say your report now and click hash when you are done.";
            Play play = new Play.Builder(textToVoice.getVoiceURL(text, user.getLang())).build();
            Record record = new Record.Builder().timeout(10).transcribe(false).action("/URLInFromCropThings").build();

            builder = builder.play(play).record(record);





        } else if (selectedOption.equals("3")) {
            String text = "You have entered the registration process. Please now respond to the messages you receive. Thank you.";
            Play play = new Play.Builder(textToVoice.getVoiceURL(text, user.getLang())).build();

            builder = builder.play(play);

            startRegistration(from, countryCode, userService);






        } else {
            builder = builder.redirect(new Redirect.Builder("/callIn").build());
        }



        try {
            response.getWriter().print(builder.build().toXml());
        } catch (TwiMLException | IOException e) {
            e.printStackTrace();
        }

    }


    @PostMapping("/playMsg")
    public void playMsgSegment(HttpServletRequest request, HttpServletResponse response) {
        String from = request.getParameter("From");
        User user = userService.getUser(from);
        response.setContentType("text/xml; charset=UTF-8");
        VoiceResponse.Builder builder = new VoiceResponse.Builder();


        String segment = user.voice_getSegToPlay();
        userService.save(user);
        if (segment != null) {
            String voiceURL = textToVoice.getVoiceURL(segment, user.getLang());
            builder = builder.play(new Play.Builder(voiceURL).build());
            builder = builder.redirect(new Redirect.Builder("/playMsg").build());
            
            
            
        } else
            builder = builder.hangup(new Hangup.Builder().build());


        try {
            response.getWriter().print(builder.build().toXml());
        } catch (TwiMLException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/URLInFromCropThings")
    public void handleCropURL(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("RecordingUrl");
        String from = request.getParameter("From");
        response.setContentType("text/xml; charset=UTF-8");

        User u = userService.getUser(from);


        String text = "Thank you very much, this has been reported.";
        String url3 = textToVoice.getVoiceURL(text, u.getLang());

        Play playMp3 = new Play.Builder(url3).build();
        Pause pause = new Pause.Builder().length(5).build();

        Redirect redirect = new Redirect.Builder("/callIn").build();
        VoiceResponse v = new VoiceResponse.Builder().play(playMp3).pause(pause).redirect(redirect).build();


        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            String url2 = "http://127.0.0.1/submitCropHealthReport.php?lat="+Double.toString(u.getLat())+"&lng="+Double.toString(u.getLng())+"&url="+url + "&phoneNumber="+u.tel;
            HttpGet httpGet = new HttpGet(url2);
            httpClient.execute(httpGet);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            response.getWriter().print(v.toXml());
        } catch (TwiMLException | IOException e) {
            e.printStackTrace();
        }

        // TODO: Something with this...
    }

    @GetMapping("/getMP3")
    public ResponseEntity<Resource> getMP3(
            ServletWebRequest request, @RequestParam("path") String path
            ) throws MalformedURLException, UnsupportedEncodingException {
        URL url = new URL(URLDecoder.decode(path, "UTF-8"));
        HttpURLConnection conn = null;
        String targetEtag = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            targetEtag = conn.getHeaderField("etag");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        if (request.checkNotModified(targetEtag)) {
            return null;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .cacheControl(CacheControl.maxAge(259200, TimeUnit.SECONDS))
                .eTag(targetEtag)
                .body(new UrlResource(url));
    }

}
