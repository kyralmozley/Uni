package textfarming.persistence;

import static textfarming.InfoMessageGenerator.splitMessage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

import textfarming.datasources.market.Crop;
import textfarming.sms.SMSSender;
import textfarming.sms.registration.LanguageQuerier;
import textfarming.sms.registration.RegistrationState;
import textfarming.sms.registration.location.LatLongAcc;

@Entity
public class User {
    //TODO: consider if it is safe to switch from String to ordinal for saving enums

    @Id
    public final String tel;

    private double lat;
    private double lng;
    /**
     * Accuracy of LatLong, in metres
     */
    private int locAccuracy;

    /**
     * A supported language code documented on "https://soundoftext.com/docs".
     */
    private String lang;

    @ElementCollection(targetClass = Crop.class)
    @Enumerated(EnumType.STRING)
    private Collection<Crop> crops;

    @Enumerated(EnumType.STRING)
    private RegistrationState regis_state;
    @Enumerated(EnumType.STRING)
    private Crop regis_cropToAsk;

    @Type(type="text")
    private String voice_message;
    private int voice_nextSegIndex;

    // Please keep the following 2 constructors private. They are intended as such.
    private User() {
        tel = null;
    }

    private User(String tel) {
        this.tel = tel;
        regis_state = RegistrationState.PENDING_LANG;
        regis_cropToAsk = Crop.values()[0];
    }

    public double getLat() {return lat;}
    public double getLng() {return lng;}
    public int getLocAccuracy() {return locAccuracy;}

    public void setLoc(LatLongAcc loc) {
        this.lat = loc.lat;
        this.lng = loc.lng;
        locAccuracy = loc.acc;
    }

    public String getLang() {return lang;}
    /**
     * @param lang A supported language code documented on "https://soundoftext.com/docs".
     * Note that Chinese Traditional is not supported by our current design, since there is only
     * one voice code available for Chinese, which is mapped to zh-CN.
     */
    public void setLang(String lang) {this.lang = lang;}

    public boolean grows(Crop crop) {
        return (crops != null) && crops.contains(crop);
    }

    public void addCrop(Crop crop) {
        if (crops == null)
            crops = new HashSet<Crop>();
        crops.add(crop);
    }

    public void emptyCrops() {
        crops = null;
    }

    public RegistrationState regis_getState() {return regis_state;}
    public void regis_setState(RegistrationState regis_state) {this.regis_state = regis_state;}
    public Crop regis_getCropToAsk() {return regis_cropToAsk;}

    public void regis_doneWithCropToAsk() {
        Crop[] vals = Crop.values();
        if (regis_cropToAsk.ordinal() == vals.length - 1)
            regis_cropToAsk = null;
        else
            regis_cropToAsk = vals[regis_cropToAsk.ordinal() + 1];
    }

    public boolean regis_finishedAskingCrops() {
        return regis_cropToAsk == null;
    }

    public void voice_setMessage(String voice_message) {
        this.voice_message = voice_message;
        voice_nextSegIndex = 0;
    }

    public String voice_getSegToPlay() {
        if (voice_message == null) return null;

        List<String> segList = splitMessage(voice_message);

        if (voice_nextSegIndex == segList.size()) {
            voice_message = null;
            voice_nextSegIndex = 0;
            return null;
        }

        return segList.get(voice_nextSegIndex++);
    }

    public static User startRegistration(String tel, String countryCode, UserService userService) {
        // Examine codebase to see if it is indeed safe to do this, i.e. just rebuild a User object
        // In particular note the Date d and jsonupdatemessage string
        List<String> potentialLangs = LanguageQuerier.getLanguages(countryCode);
        int langIndex = 0;
        for (String lang : potentialLangs)
            SMSSender.sendSMS(tel, 
                    "Thank you for using text farming. " +
                            "To select this language, send '" + (langIndex++) + "'.",
                            lang);
        return userService.save(new User(tel));
    }

}
