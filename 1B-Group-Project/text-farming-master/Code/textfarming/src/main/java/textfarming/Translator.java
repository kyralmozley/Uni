package textfarming;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.apache.commons.text.StringEscapeUtils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.api.services.translate.model.TranslationsListResponse;

//look into whether we want to switch to newest API format
//note fact that current use of api is ugly, unofficial, and brute-force
//TODO: figure out if we should change code to follow google official product page
// (needs enviroment vars -- tricky)
public class Translator {

    private static Translate translate;

    static {
        try {
            translate = new Translate.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), 
                    JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName("text-farming2")
                    .setTranslateRequestInitializer(
                            new TranslateRequestInitializer(
                                    "API_KEY")).build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static String translate(String englishText, String textToVoiceCode) {
        if (isEnglish(textToVoiceCode)) return englishText;
        TranslationsListResponse response = null;
        try {
            response = translate.translations().list(
                    Arrays.asList(englishText),
                    getTextToTextCode(textToVoiceCode)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StringEscapeUtils.unescapeHtml4(
                response.getTranslations().get(0).getTranslatedText());
    }


    private static String getTextToTextCode(String textToVoiceCode) {
        if (textToVoiceCode.equals("cmn-Hant-TW"))
            return "zh-CN";
        if (textToVoiceCode.equals("fil-PH"))
            return "tl";
        if (textToVoiceCode.equals("nb-NO"))
            return "no";
        return textToVoiceCode.split("-")[0];
    }

    //TODO: examine all the use of language codes across the system and clean things up
    //TODO: for example, change all variable names of "textToVoiceCode" back to simply lang
    // but before doing that, document very clearly that ALL language codes we pass around
    // should be textToVoiceCodes
    // Again, TODO: change "textToVoiceCode" everywhere in the project back to "lang"
    // But before doing that, TODO: document that we should only use literals from soundtotext
    private static boolean isEnglish(String textToVoiceCode) {
        return textToVoiceCode.equals("en-AU") 
                || textToVoiceCode.equals("en-GB") || textToVoiceCode.equals("en-US");
    }
}
