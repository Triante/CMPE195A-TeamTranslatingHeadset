package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.io.Serializable;

import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.ENGLISH_F;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class Language implements Serializable {

    public static enum LanguageSelect {
        ENGLISH_F, ENGLISH_M, GBENGLISH, FRENCH, GERMAN_F, GERMAN_M,
        ITALIAN, JAPANESE, PORTUGUESE, SPANISH_F, SPANISH_M, MXSPANISH, MANDARIN
    }
    private static LanguageSelect myLanguage = ENGLISH_F;
    private static LanguageSelect responseLanguage = ENGLISH_F;

    public static String getMyLanguageCode() {
        switch (myLanguage) {
            case ENGLISH_F:
            case ENGLISH_M:
            case GBENGLISH:
                return "en";
            case FRENCH:
                return "fr";
            case GERMAN_F:
            case GERMAN_M:
                return "de";
            case ITALIAN:
                return "it";
            case JAPANESE:
                return "ja";
            case PORTUGUESE:
                return "pt";
            case SPANISH_F:
            case SPANISH_M:
            case MXSPANISH:
                return "es";
            case MANDARIN:
                return "zh-CHS";
            default:
                return "en";
        }
    }

    public static String getResponseLanguageCode() {
        switch (responseLanguage) {
            case ENGLISH_F:
            case ENGLISH_M:
            case GBENGLISH:
                return "en";
            case FRENCH:
                return "fr";
            case GERMAN_F:
            case GERMAN_M:
                return "de";
            case ITALIAN:
                return "it";
            case JAPANESE:
                return "jp";
            case PORTUGUESE:
                return "pg";
            case SPANISH_F:
            case SPANISH_M:
            case MXSPANISH:
                return "es";
            case MANDARIN:
                return "zh-CHS";
            default:
                return "en";
        }
    }

    public static String getMyLanguageModel() {
        switch (myLanguage) {
            case ENGLISH_F:
            case ENGLISH_M:
                return "en-US_BroadbandModel";
            case GBENGLISH:
                return "en-UK_BroadbandModel";
            case FRENCH:
                return "fr-FR_BroadbandModel";
            case GERMAN_F:
            case GERMAN_M:
                //return "de"; not supported
                return "en-UK_BroadbandModel";
            case ITALIAN:
                //return "it"; not supported
                return "en-UK_BroadbandModel";
            case JAPANESE:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE:
                return "pt-BR_BroadbandModel";
            case SPANISH_F:
            case SPANISH_M:
            case MXSPANISH:
                return "es-ES_BroadbandModel";
            case MANDARIN:
                return "zh-CN_BroadbandModel";
            default:
                return "en-UK_BroadbandModel";
        }
    }

    public static String getResponseLanguageModel() {
        switch (responseLanguage) {
            case ENGLISH_F:
            case ENGLISH_M:
                return "en-US_BroadbandModel";
            case GBENGLISH:
                return "en-UK_BroadbandModel";
            case FRENCH:
                return "fr-FR_BroadbandModel";
            case GERMAN_F:
            case GERMAN_M:
                //return "de"; not supported
                return "en-UK_BroadbandModel";
            case ITALIAN:
                //return "it"; not supported
                return "en-UK_BroadbandModel";
            case JAPANESE:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE:
                return "pt-BR_BroadbandModel";
            case SPANISH_F:
            case SPANISH_M:
            case MXSPANISH:
                return "es-ES_BroadbandModel";
            case MANDARIN:
                return "zh-CN_BroadbandModel";
            default:
                return "en-UK_BroadbandModel";
        }
    }

    public static Voice getMyLanguageVoice() {
        switch (myLanguage) {
            case ENGLISH_F:
                return Voice.EN_ALLISON;
            case ENGLISH_M:
                return Voice.EN_MICHAEL;
            case GBENGLISH:
                return Voice.GB_KATE;
            case FRENCH:
                return Voice.FR_RENEE;
            case GERMAN_F:
                return Voice.DE_BIRGIT;
            case GERMAN_M:
                return Voice.DE_DIETER;
            case ITALIAN:
                return Voice.IT_FRANCESCA;
            case JAPANESE:
                return Voice.JA_EMI;
            case PORTUGUESE:
                return Voice.PT_ISABELA;
            case SPANISH_F:
                return Voice.ES_LAURA;
            case SPANISH_M:
                return Voice.ES_ENRIQUE;
            case MXSPANISH:
                return Voice.ES_SOFIA;
            case MANDARIN:
                //return "zh-CHS"; not supported yet
                return Voice.EN_MICHAEL;
            default:
                return Voice.EN_MICHAEL;
        }
    }

    public static Voice getResponseLanguageVoice() {
        switch (responseLanguage) {
            case ENGLISH_F:
                return Voice.EN_ALLISON;
            case ENGLISH_M:
                return Voice.EN_MICHAEL;
            case GBENGLISH:
                return Voice.GB_KATE;
            case FRENCH:
                return Voice.FR_RENEE;
            case GERMAN_F:
                return Voice.DE_BIRGIT;
            case GERMAN_M:
                return Voice.DE_DIETER;
            case ITALIAN:
                return Voice.IT_FRANCESCA;
            case JAPANESE:
                return Voice.JA_EMI;
            case PORTUGUESE:
                return Voice.PT_ISABELA;
            case SPANISH_F:
                return Voice.ES_LAURA;
            case SPANISH_M:
                return Voice.ES_ENRIQUE;
            case MXSPANISH:
                return Voice.ES_SOFIA;
            case MANDARIN:
                //return "zh-CHS"; not supported yet
                return Voice.EN_MICHAEL;
            default:
                return Voice.EN_MICHAEL;
        }
    }

    public static void setLanguage(boolean toBeMyLanguage, LanguageSelect theLanguage) {
        if (toBeMyLanguage) {
            myLanguage = theLanguage;
        }
        else {
            responseLanguage = theLanguage;
        }
    }

    public static LanguageSelect getLanguage(boolean isMyLanguage) {
        if (isMyLanguage) return myLanguage;
        else return responseLanguage;
    }

    public static boolean speechToSpeechPossible() {
        switch (responseLanguage) {
            case GERMAN_F:
            case GERMAN_M:
            case ITALIAN:
            case MANDARIN:
                return false;
        }
        switch (myLanguage) {
            case GERMAN_F:
            case GERMAN_M:
            case ITALIAN:
            case MANDARIN:
                return false;
        }
        return true;
    }

}
