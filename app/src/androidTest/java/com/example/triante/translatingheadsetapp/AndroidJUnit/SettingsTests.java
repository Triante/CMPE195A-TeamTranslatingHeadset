package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.DemoActivity;
import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;

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

    final private LanguageSettings.Language defaultMyLanguage = LanguageSettings.Language.ENGLISH_US_ALLISON;
    final private LanguageSettings.Language defaultResponseLanguage = LanguageSettings.Language.SPANISH_MX_SOFIA;
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
        TranslaTaSettings.setMaxAmplitude(defaultMaxAmp);
        TranslaTaSettings.setAmplitudeThreshold(defaultThreshold);
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity());
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
    }

    @Test
    public void test_setProfanityTrue() {
        TranslaTaSettings.setProfanityFilter(true);
        boolean stillTrue = TranslaTaSettings.isProfanityFilterActive();
        assertTrue("Profanity filter set true", stillTrue);
    }

    @Test
    public void test_setProfanityFalse() {
        TranslaTaSettings.setProfanityFilter(false);
        boolean stillFalse = TranslaTaSettings.isProfanityFilterActive();
        assertFalse("Profanity filter set false", stillFalse);
    }

    @Test
    public void test_setAmplitudeThreshold(){
        int thresholdToSet = 225000;
        TranslaTaSettings.setAmplitudeThreshold(thresholdToSet);
        int thresholdRetrieved = TranslaTaSettings.getThresholdAmplitude();
        assertEquals("Threshold set properly", thresholdRetrieved, thresholdToSet, 0);
    }

    @Test
    public void test_setAmplitudeMax(){
        int maxToSet = 3500253;
        TranslaTaSettings.setMaxAmplitude(maxToSet);
        int maxRetrieved = TranslaTaSettings.getMaxAmplitude();
        assertEquals("Max amplitude set properly", maxToSet, maxRetrieved, 0);
    }

    @Test
    public void test_saveAndLoadLanguageSettings() {
        LanguageSettings.setLanguage(true, LanguageSettings.Language.FRENCH_RENEE);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.JAPANESE_EMI);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
        LanguageSettings.setLanguage(true, defaultMyLanguage);
        LanguageSettings.setLanguage(false, defaultResponseLanguage);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertEquals("Save and Load my language test", LanguageSettings.getLanguage(true), LanguageSettings.Language.FRENCH_RENEE);
        assertEquals("Save and Load response language test", LanguageSettings.getLanguage(false), LanguageSettings.Language.JAPANESE_EMI);
    }

    @Test
    public void test_saveAndLoadProfanitySettings() {
        TranslaTaSettings.setProfanityFilter(false);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
        TranslaTaSettings.setProfanityFilter(true);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertFalse("Profanity filter save and load test", TranslaTaSettings.isProfanityFilterActive());
    }

    @Test
    public void test_saveAndLoadAmplitudeSettings() {
        int threshold = 15000000;
        int max = 25000000;
        TranslaTaSettings.setMaxAmplitude(max);
        TranslaTaSettings.setAmplitudeThreshold(threshold);
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity());
        TranslaTaSettings.setMaxAmplitude(threshold);
        TranslaTaSettings.setAmplitudeThreshold(max);
        TranslaTaSettings.initiateTranslaTaSettings(mActivityRule.getActivity());
        assertEquals("Save and Load threshold settings", threshold, TranslaTaSettings.getThresholdAmplitude());
        assertEquals("Save and Load max amplitude settings", max, TranslaTaSettings.getMaxAmplitude());
    }
}
