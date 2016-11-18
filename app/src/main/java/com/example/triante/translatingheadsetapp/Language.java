package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.io.Serializable;

import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.ENGLISH_F;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class for storing all the language preferences (language1: user language2: other party)*/
public class Language implements Serializable {

    /* Language specific enumarations to use for identification purposes*/
    public static enum LanguageSelect {
        ENGLISH_F, ENGLISH_M, GBENGLISH, FRENCH, GERMAN_F, GERMAN_M,
        ITALIAN, JAPANESE, PORTUGUESE, SPANISH_F, SPANISH_M, MXSPANISH, MANDARIN
    }
    
    private static LanguageSelect myLanguage = ENGLISH_F; //placeholder for the user's preferred language
    private static LanguageSelect responseLanguage = ENGLISH_F; //placeholder for the other party's preferred language

    /* Getter for the current language saved as the user's preferred language */
    public static String getMyLanguageCode() {
        switch (myLanguage) {
            case ENGLISH_F: //Female English voice
            case ENGLISH_M: //Male English voice
            case GBENGLISH: //Great Britain English voice
                return "en";
            case FRENCH:
                return "fr";
            case GERMAN_F: //Female German voice
            case GERMAN_M: //Male German voice
                return "de";
            case ITALIAN:
                return "it";
            case JAPANESE:
                return "ja";
            case PORTUGUESE:
                return "pt";
            case SPANISH_F: //Female Spanish voice
            case SPANISH_M: //Male Spanish voice
            case MXSPANISH: //Mexican Spanish voice
                return "es";
            case MANDARIN:
                return "zh-CHS";
            default:
                return "en";
        }
    }

    /* Getter for the current language saved as the other party's preferred language */
    public static String getResponseLanguageCode() {
        switch (responseLanguage) {
            case ENGLISH_F: //Female English voice
            case ENGLISH_M: //Male English voice
            case GBENGLISH: //Great Britain English voice
                return "en";
            case FRENCH:
                return "fr";
            case GERMAN_F: //Female German voice
            case GERMAN_M: //Male German voice
                return "de";
            case ITALIAN:
                return "it";
            case JAPANESE:
                return "jp";
            case PORTUGUESE:
                return "pg";
            case SPANISH_F: //Female Spanish voice
            case SPANISH_M: //Male Spanish voice
            case MXSPANISH:
                return "es";
            case MANDARIN:
                return "zh-CHS";
            default:
                return "en";
        }
    }

    /* Getter for the current language saved as the user's preferred language model for the translator */
    public static String getMyLanguageModel() {
        switch (myLanguage) {
            case ENGLISH_F: //Female English voice
            case ENGLISH_M: //Male English voice
                return "en-US_BroadbandModel";
            case GBENGLISH: //Great Britain English voice
                return "en-UK_BroadbandModel";
            case FRENCH:
                return "fr-FR_BroadbandModel";
            case GERMAN_F: //Female German voice
            case GERMAN_M: //Male German voice
                return "en-UK_BroadbandModel"; //return "de" not supported
            case ITALIAN:
                return "en-UK_BroadbandModel"; //return "it" not supported
            case JAPANESE:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE:
                return "pt-BR_BroadbandModel";
            case SPANISH_F: //Female Spanish voice
            case SPANISH_M: //Male Spanish voice
            case MXSPANISH: //Mexican Spanish voice
                return "es-ES_BroadbandModel";
            case MANDARIN:
                return "zh-CN_BroadbandModel";
            default:
                return "en-UK_BroadbandModel";
        }
    }

    /* Getter for the current language saved as the other party's preferred language model for the translator */
    public static String getResponseLanguageModel() {
        switch (responseLanguage) {
            case ENGLISH_F: //Female English voice
            case ENGLISH_M: //Male English voice
                return "en-US_BroadbandModel";
            case GBENGLISH: //Great Britain English voice
                return "en-UK_BroadbandModel";
            case FRENCH:
                return "fr-FR_BroadbandModel";
            case GERMAN_F: //Female German voice
            case GERMAN_M: //Male German voice
                return "en-UK_BroadbandModel"; //return "de" not supported
            case ITALIAN:
                return "en-UK_BroadbandModel"; //return "it" not supported
            case JAPANESE:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE:
                return "pt-BR_BroadbandModel";
            case SPANISH_F: //Female Spanish voice
            case SPANISH_M: //Male Spanish voice
            case MXSPANISH: //Mexican Spanish voice
                return "es-ES_BroadbandModel";
            case MANDARIN:
                return "zh-CN_BroadbandModel";
            default:
                return "en-UK_BroadbandModel";
        }
    }

    /* Getter for the voice that will be used to speak to the user*/
    public static Voice getMyLanguageVoice() {
        switch (myLanguage) {
            case ENGLISH_F: //Female English voice
                return Voice.EN_ALLISON;
            case ENGLISH_M: //Male English voice
                return Voice.EN_MICHAEL;
            case GBENGLISH: //Great Britain English voice
                return Voice.GB_KATE;
            case FRENCH:
                return Voice.FR_RENEE;
            case GERMAN_F: //Female German voice
                return Voice.DE_BIRGIT;
            case GERMAN_M: //Male German voice
                return Voice.DE_DIETER;
            case ITALIAN:
                return Voice.IT_FRANCESCA;
            case JAPANESE:
                return Voice.JA_EMI;
            case PORTUGUESE:
                return Voice.PT_ISABELA;
            case SPANISH_F: //Female Spanish voice
                return Voice.ES_LAURA;
            case SPANISH_M: //Male Spanish voice
                return Voice.ES_ENRIQUE;
            case MXSPANISH: //Mexican Spanish voice
                return Voice.ES_SOFIA;
            case MANDARIN:
               return Voice.EN_MICHAEL;  //return "zh-CHS" not supported yet
            default:
                return Voice.EN_MICHAEL;
        }
    }

    /* Getter for the voice that will be used to speak to the other party*/
    public static Voice getResponseLanguageVoice() {
        switch (responseLanguage) {
            case ENGLISH_F: //Female English voice
                return Voice.EN_ALLISON;
            case ENGLISH_M: //Male English voice
                return Voice.EN_MICHAEL;
            case GBENGLISH: //Great Britain English voice
                return Voice.GB_KATE;
            case FRENCH:
                return Voice.FR_RENEE;
            case GERMAN_F: //Female German voice
                return Voice.DE_BIRGIT;
            case GERMAN_M: //Male German voice
                return Voice.DE_DIETER;
            case ITALIAN:
                return Voice.IT_FRANCESCA;
            case JAPANESE:
                return Voice.JA_EMI;
            case PORTUGUESE:
                return Voice.PT_ISABELA;
            case SPANISH_F: //Female Spanish voice
                return Voice.ES_LAURA;
            case SPANISH_M: //Male Spanish voice
                return Voice.ES_ENRIQUE;
            case MXSPANISH: //Mexican Spanish voice
                return Voice.ES_SOFIA;
            case MANDARIN:
                return Voice.EN_MICHAEL; //return "zh-CHS" not supported yet
            default:
                return Voice.EN_MICHAEL;
        }
    }
    
    /* Sets the language that is going to be used for either the user or the other party*/
    public static void setLanguage(boolean toBeMyLanguage, LanguageSelect theLanguage) {
        if (toBeMyLanguage) {
            myLanguage = theLanguage;
        }
        else {
            responseLanguage = theLanguage;
        }
    }

    /* Getss the language that is currently being used for either the user or the other party*/
    public static LanguageSelect getLanguage(boolean isMyLanguage) {
        if (isMyLanguage) return myLanguage;
        else return responseLanguage;
    }
    
    /* Checks if the current language model can be applied to the speech-to-speech translation session*/
    public static boolean speechToSpeechPossible() {
        
        /* Languages not supported */
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
