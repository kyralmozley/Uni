package textfarming.sms.registration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageQuerier {

    private static final Set<String> SUPPORTED_LANG = new HashSet<>(); 

    static {
        String supported_Lang = "af-ZA,sq,ar-AE,hy,bn-BD,bn-IN,bs,my,ca-ES,"
                + "cmn-Hant-TW,hr-HR,cs-CZ,da-DK,nl-NL,en-AU,en-GB,en-US,eo,"
                + "fil-PH,fi-FI,fr-FR,fr-CA,de-DE,el-GR,hi-IN,hu-HU,is-IS,id-ID,"
                + "it-IT,ja-JP,km,ko-KR,la,lv,mk,ne,nb-NO,pl-PL,pt-BR,ro-RO,ru-RU,"
                + "sr-RS,si,sk-SK,es-MX,es-ES,sw,sv-SE,ta,th-TH,tr-TR,uk-UA,vi-VN,cy";
        for (String lang : supported_Lang.split(","))
            SUPPORTED_LANG.add(lang);
    }

    public static boolean isSupported(String textToVoiceCode) {
        return SUPPORTED_LANG.contains(textToVoiceCode);
    }

    //TODO: implement this fully using some API.
    //For now: it is a stub, simply returns the languages we would like to demo.
    public static List<String> getLanguages(String countryCode) {
        List<String> result = new ArrayList<>(2);
        result.add("en-GB");
        result.add("sw");
        return result;
    }
}
