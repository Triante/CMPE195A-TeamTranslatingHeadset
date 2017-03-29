package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import java.io.Serializable;

import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.ENGLISH_F;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.ENGLISH_M;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.FRENCH;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.GBENGLISH;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.GERMAN_F;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.GERMAN_M;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.ITALIAN;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.JAPANESE;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.MANDARIN;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.MXSPANISH;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.PORTUGUESE;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.SPANISH_F;
import static com.example.triante.translatingheadsetapp.Language.LanguageSelect.SPANISH_M;

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
    private static LanguageSelect responseLanguage = SPANISH_F; //placeholder for the other party's preferred language
    private static boolean profanityFilter = true;
    private static int maxAmplitude = 0;
    private static int thresholdAmplitude = 0;

    private static final String SHARED_PREFERENCES_FILE_NAME = "TRANSLATA_SHARED_PREFERENCES";
    private static final String SHARED_PREFERENCES_MY_LANGUAGE = "TRANSLATA_MY_LANGUAGE";
    private static final String SHARED_PREFERENCES_RESPONSE_LANGUAGE = "TRANSLATA_RESPONSE_LANGUAGE";
    private static final String SHARED_PREFERENCES_PROFANITY_FILTER_FLAG = "TRANSLATA_PROFANITY_FILTER";
    private static final String SHARED_PREFERENCES_MAX_SETTINGS = "TRANSLATA_MAX_SETTINGS";
    private static final String SHARED_PREFERENCES_THRESHOLD_SETTINGS = "TRANSLATA_THRESHOLD_SETTINGS";

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

    public static boolean isProfanityFilterActive() {
        return profanityFilter;
    }

    public static boolean setProfanityFilter(boolean active) {
        profanityFilter = active;
        return profanityFilter;
    }

    public static boolean enableProfanity(int user) {
        LanguageSelect key;
        if (user == 0) {
            key = myLanguage;
        }
        else {
            key = responseLanguage;
        }
        switch (key) {
            case ENGLISH_F:
            case ENGLISH_M:
            case GBENGLISH:
                if (isProfanityFilterActive()) {
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean initiateSavedLanguageSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        int myLang = settings.getInt(SHARED_PREFERENCES_MY_LANGUAGE, 0);
        int respLang = settings.getInt(SHARED_PREFERENCES_RESPONSE_LANGUAGE, 0);
        myLanguage = getLanguageFromIntCode(myLang);
        responseLanguage = getLanguageFromIntCode(respLang);
        profanityFilter = settings.getBoolean(SHARED_PREFERENCES_PROFANITY_FILTER_FLAG, true);
        maxAmplitude = settings.getInt(SHARED_PREFERENCES_MAX_SETTINGS, 0);
        thresholdAmplitude = settings.getInt(SHARED_PREFERENCES_THRESHOLD_SETTINGS, 0);
        return true;
    }

    public static boolean saveLanguageSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        int myLang = getLanguageIntCode(myLanguage);
        int respLang = getLanguageIntCode(responseLanguage);
        editor.putInt(SHARED_PREFERENCES_MY_LANGUAGE, myLang);
        editor.putInt(SHARED_PREFERENCES_RESPONSE_LANGUAGE, respLang);
        editor.putBoolean(SHARED_PREFERENCES_PROFANITY_FILTER_FLAG, isProfanityFilterActive());
        editor.commit();
        return true;
    }

    public static boolean saveAmplitudeSettings(Context context, int maxAmp, int thresholdAmp) {
        maxAmplitude = maxAmp;
        thresholdAmplitude = thresholdAmp;
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_PREFERENCES_MAX_SETTINGS, maxAmplitude);
        editor.putInt(SHARED_PREFERENCES_THRESHOLD_SETTINGS, thresholdAmplitude);
        editor.commit();
        return true;
    }

    public static int getMaxAmplitude() {
        return maxAmplitude;
    }

    public static int getThresholdAmplitude() {
        return thresholdAmplitude;
    }


    private static int getLanguageIntCode(LanguageSelect key) {
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

    private static LanguageSelect getLanguageFromIntCode(int key) {
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
