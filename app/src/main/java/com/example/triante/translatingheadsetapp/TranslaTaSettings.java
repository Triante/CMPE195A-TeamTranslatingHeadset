package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */

/**
 * Holds the values for all the TranslaTa settings for the specific user
 */
public class TranslaTaSettings {

    /**
     * Constructor for TranslaTaSettings
     */
    private TranslaTaSettings() {}

    private static int myLanguageCode = 0; //the language the user chose as his/her speaking language
    private static int responseLanguageCode = 0; //the language the user chose as the other party's speaking language
    private static boolean profanityFilter = true; //flag to denote whether the profanity filter is on or not
    private static int maxAmplitude = 0; //max amplitude possible for system to recognize
    private static int thresholdAmplitude = 0; //threshold for determining which of the users spoke
    private static final String SHARED_PREFERENCES_FILE_NAME = "TRANSLATA_SHARED_PREFERENCES";
    private static final String SHARED_PREFERENCES_MY_LANGUAGE = "TRANSLATA_MY_LANGUAGE";
    private static final String SHARED_PREFERENCES_RESPONSE_LANGUAGE = "TRANSLATA_RESPONSE_LANGUAGE";
    private static final String SHARED_PREFERENCES_PROFANITY_FILTER_FLAG = "TRANSLATA_PROFANITY_FILTER";
    private static final String SHARED_PREFERENCES_MAX_SETTINGS = "TRANSLATA_MAX_SETTINGS";
    private static final String SHARED_PREFERENCES_THRESHOLD_SETTINGS = "TRANSLATA_THRESHOLD_SETTINGS";

    /**
     * Checks whether the profanity filter is on or not
     * @return (true if the filter is on, false otherwise)
     */
    public static boolean isProfanityFilterActive() {
        return profanityFilter;
    }

    /**
     * Turns on or off the flag for the profanity filter
     * @param active (true if profanity is meant to be turned on, false otherwise)
     * @return (status of the profanity filter)
     */
    public static boolean setProfanityFilter(boolean active) {
        profanityFilter = active;
        return profanityFilter;
    }

    /**
     * Updates the language that the user chose for himself/herself or other party
     * @param isMyLanguage (whether it is the user's language or other party's language)
     * @param code (the language integer code value)
     * @return (whether the language modification succeeded or not)
     */
    public static boolean setLanguageSettingsCode(boolean isMyLanguage, int code) {
        if (isMyLanguage) myLanguageCode = code;
        else responseLanguageCode = code;
        return true;
    }

    /**
     * Sets up the settings variables
     * @param context (context coming from the settings activity)
     * @return (whether the settings initiation process succeeded or not)
     */
    public static boolean initiateTranslaTaSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        myLanguageCode = settings.getInt(SHARED_PREFERENCES_MY_LANGUAGE, 0);
        responseLanguageCode = settings.getInt(SHARED_PREFERENCES_RESPONSE_LANGUAGE, 0);
        LanguageSettings.Language myLanguage = LanguageSettings.getLanguageFromIntCode(myLanguageCode);
        LanguageSettings.Language responseLanguage = LanguageSettings.getLanguageFromIntCode(responseLanguageCode);
        LanguageSettings.setLanguage(true, myLanguage);
        LanguageSettings.setLanguage(false, responseLanguage);
        profanityFilter = settings.getBoolean(SHARED_PREFERENCES_PROFANITY_FILTER_FLAG, true);
        maxAmplitude = settings.getInt(SHARED_PREFERENCES_MAX_SETTINGS, 0);
        thresholdAmplitude = settings.getInt(SHARED_PREFERENCES_THRESHOLD_SETTINGS, 0);
        return true;
    }

    /**
     * Saves the language settings for both the session and to the phone's local memory
     * @param context (context coming from the settings activity)
     * @return (whether the saving process succeeded or not)
     */
    public static boolean saveLanguageSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_PREFERENCES_MY_LANGUAGE, myLanguageCode);
        editor.putInt(SHARED_PREFERENCES_RESPONSE_LANGUAGE, responseLanguageCode);
        editor.putBoolean(SHARED_PREFERENCES_PROFANITY_FILTER_FLAG, isProfanityFilterActive());
        editor.commit();
        return true;
    }

    /**
     * Saves the amplitude threshold preferences for both the session and to the phone's local memory
     * @param context (context coming from the settings activity)
     * @return (Whether the saving process succeeded or not)
     */
    public static boolean saveAmplitudeSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_PREFERENCES_MAX_SETTINGS, maxAmplitude);
        editor.putInt(SHARED_PREFERENCES_THRESHOLD_SETTINGS, thresholdAmplitude);
        editor.commit();
        return true;
    }

    /**
     * Accessor for the max amplitude possible
     * @return (the max amplitude value)
     */
    public static int getMaxAmplitude() {
        return maxAmplitude;
    }

    /**
     * Accessor for the current amplitude threshold
     * @return (the amplitude threshold)
     */
    public static int getThresholdAmplitude() {
        return thresholdAmplitude;
    }

    /**
     * Mutator for the max amplitude
     * @param maxAmp (max amplitude value)
     */
    public static void setMaxAmplitude(int maxAmp) {
        maxAmplitude = maxAmp;
    }

    /**
     * Mutator for the amplitude threshold
     * @param thresholdAmp (amplitude threshold value)
     */
    public static void setAmplitudeThreshold(int thresholdAmp) {
        thresholdAmplitude = thresholdAmp;
    }

}
