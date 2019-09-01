package textfarming.sms;

import static textfarming.InfoMessageGenerator.generateUserMessage;
import static textfarming.persistence.User.startRegistration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import textfarming.persistence.CustomUpdateMessageService;
import textfarming.persistence.User;
import textfarming.persistence.UserService;
import textfarming.sms.registration.LanguageQuerier;
import textfarming.sms.registration.RegistrationState;
import textfarming.sms.registration.location.LatLongAcc;
import textfarming.sms.registration.location.nametocoords.NameToCoords;
import textfarming.sms.registration.location.what3words.What3Words;

@RestController
public class SMSController {

    public static final String REGIS_PROMPT = "00";

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUpdateMessageService customUpdateMessageService;

    @Autowired
    private RestTemplate restTemplate;


    //TODO: detailed exception handling, ill-formatted incoming messages
    //TODO: change code across codebase to use RestTemplate rather than HttpClient
    @PostMapping("/messageIn")
    public void handleSMS(HttpServletRequest request){
        String body = request.getParameter("Body");
        String source = request.getParameter("From");
        String countryCode = request.getParameter("FromCountry");

        if (!userService.contains(source)) {
            startRegistration(source, countryCode, userService);
            return;
        }

        User user = userService.getUser(source);
        if (user.regis_getState().equals(RegistrationState.REGISTERED)) {
            if (body.equals(REGIS_PROMPT)) {
                user = startRegistration(user.tel, countryCode, userService);
            } else {


                customUpdateMessageService.trimMessages();
                SMSSender.sendSMS(user.tel, 
                        generateUserMessage(user.tel, userService,
                                customUpdateMessageService),
                        user.getLang());
                SMSSender.sendSMS(user.tel, 
                        "To restart registration, send '" + REGIS_PROMPT + "'.",
                        user.getLang());



            }
        } else if (user.regis_getState().equals(RegistrationState.PENDING_LANG)) {
            // -------------Save language config-----------------------
            String lang = null;
            List<String> potentialLangs = LanguageQuerier.getLanguages(countryCode);
            try {
                if (LanguageQuerier.isSupported(body))
                    lang = body;
                else
                    lang = potentialLangs.get(Integer.parseInt(body));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {}

            if (lang == null) {
                for (String potentialLang : potentialLangs) {
                    SMSSender.sendSMS(user.tel, 
                            "Could not find language, please try again.",
                            potentialLang);
                }
            } else {
                user.setLang(lang);
                // ---------Saved language config----------------------




                // ---------Request location config--------------------
                user.regis_setState(RegistrationState.PENDING_LOC);
                SMSSender.sendSMS(user.tel, 
                        "Please text in your 3 words (in English), or the "
                                + "name of your nearest town or village.", 
                                lang);
                // ---------Requested location config------------------




            }
        } else if (user.regis_getState().equals(RegistrationState.PENDING_LOC)) {
            // -------------Save location config-----------------------
            LatLongAcc w3w_attempt = What3Words.getLoc(body, restTemplate);
            LatLongAcc name_attempt = NameToCoords.getLoc(body, countryCode, restTemplate);
            if (w3w_attempt == null && name_attempt == null) {
                SMSSender.sendSMS(user.tel, 
                        "Could not find location, please try again.", 
                        user.getLang());
            } else {
                if (w3w_attempt != null) {
                    user.setLoc(w3w_attempt);
                } else {
                    user.setLoc(name_attempt);
                }
                // ---------Saved location config----------------------






                // ---------Request first crop config------------------
                user.regis_setState(RegistrationState.PENDING_CROP);
                askAboutCrop(user);
                // ---------Requested first crop config----------------




            }
        } else if (user.regis_getState().equals(RegistrationState.PENDING_CROP)) {
            // -------------Save current crop config-------------------
            if (body.equals("1"))
                user.addCrop(user.regis_getCropToAsk());
            user.regis_doneWithCropToAsk();
            // -------------Saved current crop config------------------





            // -------------Request next crop config-------------------
            if (!user.regis_finishedAskingCrops()) {
                askAboutCrop(user);
            } else {
                // ---------Requested next crop config-----------------





                // ---------Saved all crop config----------------------






                // ---------Finalise registration----------------------
                user.regis_setState(RegistrationState.REGISTERED);
                SMSSender.sendSMS(user.tel, "Registration completed! "
                        + "Text 'Hi' to get updates.", user.getLang());
                // ---------Finalised registration---------------------
            }
        }
        userService.save(user);
    }

    private void askAboutCrop(User user) {
        SMSSender.sendSMS(user.tel, 
                "If you plant " + user.regis_getCropToAsk() + ", send '1'."
                        + " Otherwise, send '0'.", 
                        user.getLang());
    }

}
