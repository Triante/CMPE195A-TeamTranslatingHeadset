package com.example.triante.translatingheadsetapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsTests {

    final private LanguageSettings.Language defaultMyLanguage = LanguageSettings.Language.ENGLISH_F;
    final private LanguageSettings.Language defaultResponseLanguage = LanguageSettings.Language.SPANISH_F;
    final private int defaultThreshold = 0;
    final private int defaultMaxAmp = 0;
    final private boolean defaultProfanityFilter = true;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void setDefaultSettingValues() {
        LanguageSettings.setLanguage(true, defaultMyLanguage);
        LanguageSettings.setLanguage(false, defaultResponseLanguage);
        TranslaTaSettings.setProfanityFilter(defaultProfanityFilter);
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity(), defaultMaxAmp, defaultThreshold);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
    }

    @Test
    public void test_saveAndLoadLanguageSettings() {
        LanguageSettings.setLanguage(true, LanguageSettings.Language.FRENCH);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.JAPANESE);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
        LanguageSettings.setLanguage(true, defaultMyLanguage);
        LanguageSettings.setLanguage(false, defaultResponseLanguage);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertEquals("Save and Load my language test", LanguageSettings.getLanguage(true), LanguageSettings.Language.FRENCH);
        assertEquals("Save and Load response language test", LanguageSettings.getLanguage(false), LanguageSettings.Language.JAPANESE);
    }

    @Test
    public void test_saveAndLoadProfanitySettings() {
        TranslaTaSettings.setProfanityFilter(false);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
        TranslaTaSettings.setProfanityFilter(true);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertFalse("Profanity filter save and soad test", TranslaTaSettings.isProfanityFilterActive());
    }

    @Test
    public void test_saveAndLoadAmplitudeSettings() {
        int threshold = 15000000;
        int max = 25000000;
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity(), max, threshold);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertEquals("Save and Load threshold settings", threshold, TranslaTaSettings.getThresholdAmplitude());
        assertEquals("Save and Load max amplitude settings", max, TranslaTaSettings.getMaxAmplitude());
    }
}
