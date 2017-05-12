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
 * Class for storing all the languageSettings preferences (myLanguage: user responseLanguage: other party)
 */

public class LanguageSettings {

    /**
     *  LanguageSettings specific enums to use for identification purposes
     */
    public enum Language {
        ENGLISH_GB_KATE, ENGLISH_US_ALLISON, ENGLISH_US_LISA, ENGLISH_US_MICHEAL,
        SPANISH_ES_ENRIQUE, SPANISH_ES_LAURA, SPANISH_MX_SOFIA, SPANISH_US_SOFIA,
        FRENCH_RENEE, JAPANESE_EMI, PORTUGUESE_BR_IABELA;
    }
    
    private static Language myLanguage = ENGLISH_US_ALLISON; //placeholder for the user's preferred languageSettings
    private static Language responseLanguage = SPANISH_MX_SOFIA; //placeholder for the other party's preferred languageSettings

    /**
     * Getter string language code for the user's language to be used for Microsoft's Translator service
     * @return user string language code for Microsoft's Translator service
     */
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

    /**
     * Getter string language code for the party's language to be used for Microsoft's Translator service
     * @return party string language code for Microsoft's Translator service
     */
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

    /**
     * Getter String language model for the user's language to be used for recognizing speech in IBM's SpeechToText
     * @return user string language model for IBM's SpeechToText
     */
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

    /**
     * Getter String language model for the party's language to be used for recognizing speech in IBM's SpeechToText
     * @return party string language model for IBM's SpeechToText
     */
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

    /**
     * Getter Voice model for the user's language to be used in IBM's TexToSpeech
     * @return user Voice model for IBM's TextToSpeech
     */
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

    /**
     * Getter Voice model for the party's language to be used in IBM's TexToSpeech
     * @return party Voice model for IBM's TextToSpeech
     */
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

    /**
     * Sets the language variable for the specified user
     * @param toBeMyLanguage true if user, false if party
     * @param theLanguage the language to be set
     */
    public static void setLanguage(boolean toBeMyLanguage, Language theLanguage) {
        if (toBeMyLanguage) {
            myLanguage = theLanguage;
        }
        else {
            responseLanguage = theLanguage;
        }

        TranslaTaSettings.setLanguageSettingsCode(toBeMyLanguage, getLanguageIntCode(theLanguage));
    }

    /**
     * Gets the language of the user specified
     * @param isMyLanguage true for user, false for party
     * @return the language for the specified user
     */
    public static Language getLanguage(boolean isMyLanguage) {
        if (isMyLanguage) return myLanguage;
        else return responseLanguage;
    }

    /**
     * Gets the Language based off the integer value used for storing the user's preference into the phones
     * shared preferences (phones app memory)
     * @param language the language to be stored in shared preferences
     * @return the integer key based off the language
     */
    public static int getLanguageIntCode(Language language) {
        switch (language) {
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

    /**
     * Gets the integer key to be used to store a language value into the phones shared preferences
     * @param key the integer key for the language to be retrieved
     * @return the language based off the key
     */
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
