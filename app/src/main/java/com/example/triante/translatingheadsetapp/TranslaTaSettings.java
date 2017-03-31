package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */

public class TranslaTaSettings {

    private TranslaTaSettings() {}

    private static int myLanguageCode = 0;
    private static int responseLanguageCode = 0;
    private static boolean profanityFilter = true;
    private static int maxAmplitude = 0;
    private static int thresholdAmplitude = 0;

    private static final String SHARED_PREFERENCES_FILE_NAME = "TRANSLATA_SHARED_PREFERENCES";
    private static final String SHARED_PREFERENCES_MY_LANGUAGE = "TRANSLATA_MY_LANGUAGE";
    private static final String SHARED_PREFERENCES_RESPONSE_LANGUAGE = "TRANSLATA_RESPONSE_LANGUAGE";
    private static final String SHARED_PREFERENCES_PROFANITY_FILTER_FLAG = "TRANSLATA_PROFANITY_FILTER";
    private static final String SHARED_PREFERENCES_MAX_SETTINGS = "TRANSLATA_MAX_SETTINGS";
    private static final String SHARED_PREFERENCES_THRESHOLD_SETTINGS = "TRANSLATA_THRESHOLD_SETTINGS";

    public static boolean isProfanityFilterActive() {
        return profanityFilter;
    }

    public static boolean setProfanityFilter(boolean active) {
        profanityFilter = active;
        return profanityFilter;
    }

    public static boolean setLanguageSettingsCode(boolean isMyLanguage, int code) {
        if (isMyLanguage) myLanguageCode = code;
        else responseLanguageCode = code;
        return true;
    }

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

    public static boolean saveLanguageSettings(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SHARED_PREFERENCES_MY_LANGUAGE, myLanguageCode);
        editor.putInt(SHARED_PREFERENCES_RESPONSE_LANGUAGE, responseLanguageCode);
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

}
