package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_GB_KATE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_ALLISON;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_LISA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_MICHEAL;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.FRENCH_RENEE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.JAPANESE_EMI;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.PORTUGUESE_BR_IABELA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_ES_ENRIQUE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_ES_LAURA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_MX_SOFIA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_US_SOFIA;


/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class for storing all the languageSettings preferences (language1: user language2: other party)*/
public class LanguageSettings {

    /* LanguageSettings specific enums to use for identification purposes*/
    public enum Language {
        ENGLISH_GB_KATE, ENGLISH_US_ALLISON, ENGLISH_US_LISA, ENGLISH_US_MICHEAL,
        SPANISH_ES_ENRIQUE, SPANISH_ES_LAURA, SPANISH_MX_SOFIA, SPANISH_US_SOFIA,
        FRENCH_RENEE, JAPANESE_EMI, PORTUGUESE_BR_IABELA;
    }
    
    private static Language myLanguage = ENGLISH_US_ALLISON; //placeholder for the user's preferred languageSettings
    private static Language responseLanguage = SPANISH_MX_SOFIA; //placeholder for the other party's preferred languageSettings

    /* Getter for the current languageSettings saved as the user's preferred languageSettings */
    public static String getMyLanguageCode() {
        switch (myLanguage) {
            case ENGLISH_GB_KATE:
            case ENGLISH_US_ALLISON:
            case ENGLISH_US_LISA:
            case ENGLISH_US_MICHEAL:
                return "en";
            case FRENCH_RENEE:
                return "fr";
            case JAPANESE_EMI:
                return "ja";
            case PORTUGUESE_BR_IABELA:
                return "pt";
            case SPANISH_ES_ENRIQUE:
            case SPANISH_ES_LAURA:
            case SPANISH_MX_SOFIA:
            case SPANISH_US_SOFIA:
                return "es";
            default:
                return "en";
        }
    }

    /* Getter for the current languageSettings saved as the other party's preferred languageSettings */
    public static String getResponseLanguageCode() {
        switch (responseLanguage) {
            case ENGLISH_GB_KATE:
            case ENGLISH_US_ALLISON:
            case ENGLISH_US_LISA:
            case ENGLISH_US_MICHEAL:
                return "en";
            case FRENCH_RENEE:
                return "fr";
            case JAPANESE_EMI:
                return "ja";
            case PORTUGUESE_BR_IABELA:
                return "pt";
            case SPANISH_ES_ENRIQUE:
            case SPANISH_ES_LAURA:
            case SPANISH_MX_SOFIA:
            case SPANISH_US_SOFIA:
                return "es";
            default:
                return "en";
        }
    }

    /* Getter for the current languageSettings saved as the user's preferred languageSettings model for the translator */
    public static String getMyLanguageModel() {
        switch (myLanguage) {
            case ENGLISH_GB_KATE:
                return "en-UK_BroadbandModel";
            case ENGLISH_US_ALLISON:
            case ENGLISH_US_LISA:
            case ENGLISH_US_MICHEAL:
                return "en-US_BroadbandModel";
            case FRENCH_RENEE:
                return "fr-FR_BroadbandModel";
            case JAPANESE_EMI:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE_BR_IABELA:
                return "pt-BR_BroadbandModel";
            case SPANISH_ES_ENRIQUE:
            case SPANISH_ES_LAURA:
            case SPANISH_MX_SOFIA:
            case SPANISH_US_SOFIA:
                return "es-ES_BroadbandModel";
            default:
                return "en-US_BroadbandModel";

        }
    }

    /* Getter for the current languageSettings saved as the other party's preferred languageSettings model for the translator */
    public static String getResponseLanguageModel() {
        switch (responseLanguage) {
            case ENGLISH_GB_KATE:
                return "en-UK_BroadbandModel";
            case ENGLISH_US_ALLISON:
            case ENGLISH_US_LISA:
            case ENGLISH_US_MICHEAL:
                return "en-US_BroadbandModel";
            case FRENCH_RENEE:
                return "fr-FR_BroadbandModel";
            case JAPANESE_EMI:
                return "ja-JP_BroadbandModel";
            case PORTUGUESE_BR_IABELA:
                return "pt-BR_BroadbandModel";
            case SPANISH_ES_ENRIQUE:
            case SPANISH_ES_LAURA:
            case SPANISH_MX_SOFIA:
            case SPANISH_US_SOFIA:
                return "es-ES_BroadbandModel";
            default:
                return "en-US_BroadbandModel";
        }
    }

    /* Getter for the voice that will be used to speak to the user*/
    public static Voice getMyLanguageVoice() {
        switch (myLanguage) {
            case ENGLISH_GB_KATE:
                return Voice.GB_KATE;
            case ENGLISH_US_ALLISON:
                return Voice.EN_ALLISON;
            case ENGLISH_US_LISA:
                return Voice.EN_LISA;
            case ENGLISH_US_MICHEAL:
                return Voice.EN_MICHAEL;
            case FRENCH_RENEE:
                return Voice.FR_RENEE;
            case JAPANESE_EMI:
                return Voice.JA_EMI;
            case PORTUGUESE_BR_IABELA:
                return Voice.PT_ISABELA;
            case SPANISH_ES_ENRIQUE:
                return Voice.ES_ENRIQUE;
            case SPANISH_ES_LAURA:
                return Voice.ES_LAURA;
            case SPANISH_MX_SOFIA:
                return Voice.ES_SOFIA;
            case SPANISH_US_SOFIA:
                return Voice.LA_SOFIA;
            default:
                return Voice.EN_ALLISON;
        }
    }

    /* Getter for the voice that will be used to speak to the other party*/
    public static Voice getResponseLanguageVoice() {
        switch (responseLanguage) {
            case ENGLISH_GB_KATE:
                return Voice.GB_KATE;
            case ENGLISH_US_ALLISON:
                return Voice.EN_ALLISON;
            case ENGLISH_US_LISA:
                return Voice.EN_LISA;
            case ENGLISH_US_MICHEAL:
                return Voice.EN_MICHAEL;
            case FRENCH_RENEE:
                return Voice.FR_RENEE;
            case JAPANESE_EMI:
                return Voice.JA_EMI;
            case PORTUGUESE_BR_IABELA:
                return Voice.PT_ISABELA;
            case SPANISH_ES_ENRIQUE:
                return Voice.ES_ENRIQUE;
            case SPANISH_ES_LAURA:
                return Voice.ES_LAURA;
            case SPANISH_MX_SOFIA:
                return Voice.ES_SOFIA;
            case SPANISH_US_SOFIA:
                return Voice.LA_SOFIA;
            default:
                return Voice.EN_ALLISON;
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

    /* Gets the languageSettings that is currently being used for either the user or the other party*/
    public static Language getLanguage(boolean isMyLanguage) {
        if (isMyLanguage) return myLanguage;
        else return responseLanguage;
    }

    public static int getLanguageIntCode(Language key) {
        switch (key) {
            case ENGLISH_GB_KATE:
                return 0;
            case ENGLISH_US_ALLISON:
                return 1;
            case ENGLISH_US_LISA:
                return 2;
            case ENGLISH_US_MICHEAL:
                return 3;
            case FRENCH_RENEE:
                return 4;
            case JAPANESE_EMI:
                return 5;
            case PORTUGUESE_BR_IABELA:
                return 6;
            case SPANISH_ES_ENRIQUE:
                return 7;
            case SPANISH_ES_LAURA:
                return 8;
            case SPANISH_MX_SOFIA:
                return 9;
            case SPANISH_US_SOFIA:
                return 10;
            default:
                return 1;
        }
    }

    public static Language getLanguageFromIntCode(int key) {
        switch (key) {
            case 0:
                return ENGLISH_GB_KATE;
            case 1:
                return ENGLISH_US_ALLISON;
            case 2:
                return ENGLISH_US_LISA;
            case 3:
                return ENGLISH_US_MICHEAL;
            case 4:
                return FRENCH_RENEE;
            case 5:
                return JAPANESE_EMI;
            case 6:
                return PORTUGUESE_BR_IABELA;
            case 7:
                return SPANISH_ES_ENRIQUE;
            case 8:
                return SPANISH_ES_LAURA;
            case 9:
                return SPANISH_MX_SOFIA;
            case 10:
                return SPANISH_US_SOFIA;
            default:
                return ENGLISH_US_ALLISON;
        }
    }

}
