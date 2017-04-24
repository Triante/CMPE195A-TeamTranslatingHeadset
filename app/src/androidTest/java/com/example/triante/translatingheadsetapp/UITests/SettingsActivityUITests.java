package com.example.triante.translatingheadsetapp.UITests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.R;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * @author by Jorge Aguiniga on 4/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class SettingsActivityUITests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void test_Layout() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.spinMyVoice)).check(matches(isDisplayed()));
        onView(withId(R.id.spinMyLang)).check(matches(isDisplayed()));
        onView(withId(R.id.spinRespLang)).check(matches(isDisplayed()));
        onView(withId(R.id.spinResVoice)).check(matches(isDisplayed()));
        onView(withId(R.id.bSave)).check(matches(isDisplayed()));
        onView(withId(R.id.myLangText)).check(matches(isDisplayed()));
        onView(withId(R.id.userLangSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.userVoiceText)).check(matches(isDisplayed()));
        onView(withId(R.id.userVoiceSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.partyLangText)).check(matches(isDisplayed()));
        onView(withId(R.id.partyLangSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.partyVoiceText)).check(matches(isDisplayed()));
        onView(withId(R.id.partyVoiceSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.profanityTitleText)).check(matches(isDisplayed()));
        onView(withId(R.id.profanitySubText)).check(matches(isDisplayed()));
        onView(withId(R.id.switchCompat)).check(matches(isDisplayed()));
        onView(withId(R.id.bAmplitude_settings)).check(matches(isDisplayed()));
        onView(withId(R.id.bAmplitudeSettingsDesc)).check(matches(isDisplayed()));
    }

    @Test
    public void test_LanguageInitialization() {
        String myLanguage = "English";
        String myVoice = "Micheal (US)";
        String pLanguage = "Spanish";
        String pVoice = "Enrique (Castilian)";
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_US_MICHEAL);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.SPANISH_ES_ENRIQUE);
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.spinMyLang)).check(matches(withSpinnerText(containsString(myLanguage))));
        onView(withId(R.id.spinMyVoice)).check(matches(withSpinnerText(containsString(myVoice))));
        onView(withId(R.id.spinRespLang)).check(matches(withSpinnerText(containsString(pLanguage))));
        onView(withId(R.id.spinResVoice)).check(matches(withSpinnerText(containsString(pVoice))));
    }

    @Test
    public void test_UserLanguageSelect() {
        String langToSelect = "Spanish";
        String voiceToSelect = "Laura (Castilian)";
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_US_ALLISON);
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.spinMyLang)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(langToSelect))).perform(click());
        onView(withId(R.id.spinMyVoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(voiceToSelect))).perform(click());
        onView(withId(R.id.spinMyLang)).check(matches(withSpinnerText(containsString(langToSelect))));
        onView(withId(R.id.spinMyVoice)).check(matches(withSpinnerText(containsString(voiceToSelect))));
    }

    @Test
    public void test_PartyLanguageSelect() {
        String langToSelect = "Spanish";
        String voiceToSelect = "Laura (Castilian)";
        LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_US_ALLISON);
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.spinRespLang)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(langToSelect))).perform(click());
        onView(withId(R.id.spinResVoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(voiceToSelect))).perform(click());
        onView(withId(R.id.spinRespLang)).check(matches(withSpinnerText(containsString(langToSelect))));
        onView(withId(R.id.spinResVoice)).check(matches(withSpinnerText(containsString(voiceToSelect))));
    }

    @Test
    public void test_ProfanityFilterInitialization() {
        TranslaTaSettings.setProfanityFilter(true);
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.switchCompat)).check(matches(isChecked()));
    }

    @Test
    public void test_ProfanityFilterSwitch() {
        TranslaTaSettings.setProfanityFilter(true);
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.switchCompat)).perform(click());
        onView(withId(R.id.switchCompat)).check(matches(not(isChecked())));
    }

    @Test
    public void test_SaveButtonAction() {
        String langUserToSelect = "French";
        String langPartyToSelect = "Spanish";
        String voicePartyToSelect = "Sofia (US)";
        TranslaTaSettings.setProfanityFilter(true);
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_US_ALLISON);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_US_ALLISON);
        TranslaTaSettings.saveLanguageSettings(mActivityRule.getActivity());
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.spinMyLang)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(langUserToSelect))).perform(click());
        onView(withId(R.id.spinRespLang)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(langPartyToSelect))).perform(click());
        onView(withId(R.id.spinResVoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(voicePartyToSelect))).perform(click());
        onView(withId(R.id.switchCompat)).perform(click());
        onView(withId(R.id.bSave)).perform(click());
        LanguageSettings.Language userLang = LanguageSettings.getLanguage(true);
        LanguageSettings.Language partyLang = LanguageSettings.getLanguage(false);
        boolean shouldBeFalse = TranslaTaSettings.isProfanityFilterActive();
        assertEquals("User Language saved test", LanguageSettings.Language.FRENCH_RENEE, userLang);
        assertEquals("Party Language saved test", LanguageSettings.Language.SPANISH_US_SOFIA, partyLang);
        assertFalse("Profanity saved", shouldBeFalse);
    }

    @Test
    public void test_SettingsNavigation() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.bAmplitude_settings)).perform(click());
        onView(withId(R.id.amplitudeSettingsTitleText)).check(matches(isDisplayed()));
    }



}
