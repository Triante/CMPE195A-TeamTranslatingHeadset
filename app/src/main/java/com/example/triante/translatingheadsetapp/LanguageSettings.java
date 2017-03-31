package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_F;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_M;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.FRENCH;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.GBENGLISH;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.GERMAN_F;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.GERMAN_M;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ITALIAN;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.JAPANESE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.MANDARIN;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.MXSPANISH;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.PORTUGUESE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_F;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_M;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class for storing all the languageSettings preferences (language1: user language2: other party)*/
public class LanguageSettings {

    /* LanguageSettings specific enumarations to use for identification purposes*/
    public enum Language {
        ENGLISH_F, ENGLISH_M, GBENGLISH, FRENCH, GERMAN_F, GERMAN_M,
        ITALIAN, JAPANESE, PORTUGUESE, SPANISH_F, SPANISH_M, MXSPANISH, MANDARIN
    }
    
    private static Language myLanguage = ENGLISH_F; //placeholder for the user's preferred languageSettings
    private static Language responseLanguage = SPANISH_F; //placeholder for the other party's preferred languageSettings

    /* Getter for the current languageSettings saved as the user's preferred languageSettings */
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

    /* Getter for the current languageSettings saved as the other party's preferred languageSettings */
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

    /* Getter for the current languageSettings saved as the user's preferred languageSettings model for the translator */
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

    /* Getter for the current languageSettings saved as the other party's preferred languageSettings model for the translator */
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
    
    /* Sets the languageSettings that is going to be used for either the user or the other party*/
    public static void setLanguage(boolean toBeMyLanguage, Language theLanguage) {
        if (toBeMyLanguage) {
            myLanguage = theLanguage;
        }
        else {
            responseLanguage = theLanguage;
        }

        TranslaTaSettings.setLanguageSettingsCode(toBeMyLanguage, getLanguageIntCode(theLanguage));
    }

    /* Getss the languageSettings that is currently being used for either the user or the other party*/
    public static Language getLanguage(boolean isMyLanguage) {
        if (isMyLanguage) return myLanguage;
        else return responseLanguage;
    }
    
    /* Checks if the current languageSettings model can be applied to the speech-to-speech translation session*/
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

    public static int getLanguageIntCode(Language key) {
        switch (key) {
            case ENGLISH_F:
                return 1;
            case ENGLISH_M:
                return 2;
            case GBENGLISH:
                return 3;
            case FRENCH:
                return 4;
            case GERMAN_F:
                return 5;
            case GERMAN_M:
                return 6;
            case ITALIAN:
                return 7;
            case JAPANESE:
                return 8;
            case PORTUGUESE:
                return 9;
            case SPANISH_F:
                return 10;
            case SPANISH_M:
                return 11;
            case MXSPANISH:
                return 12;
            case MANDARIN:
                return 13;
            default:
                return 1;
        }
    }

    public static Language getLanguageFromIntCode(int key) {
        switch (key) {
            case 1:
                return ENGLISH_F;
            case 2:
                return ENGLISH_M;
            case 3:
                return GBENGLISH;
            case 4:
                return FRENCH;
            case 5:
                return GERMAN_F;
            case 6:
                return GERMAN_M;
            case 7:
                return ITALIAN;
            case 8:
                return JAPANESE;
            case 9:
                return PORTUGUESE;
            case 10:
                return SPANISH_F;
            case 11:
                return SPANISH_M;
            case 12:
                return MXSPANISH;
            case 13:
                return MANDARIN;
            default:
                return ENGLISH_F;
        }
    }

}
