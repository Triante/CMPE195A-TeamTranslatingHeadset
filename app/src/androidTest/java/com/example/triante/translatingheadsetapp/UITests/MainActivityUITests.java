package com.example.triante.translatingheadsetapp.UITests;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.webkit.WebView;

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
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Jorge Aguiniga on 4/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUITests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void test_connectButtonBTOFF() {
        String onNoConnect = "Connect";
        String chat = "<p style=\"color:red\">" +
                "Error: RN52 headset not connected. If the headset is connected, make sure it is only connected to the call/communication channel." +
                "Error: External speaker not connected. If the speaker is connected, make sure it is only connected to the media channel." +
                "Let's Translate with TranslaTa!" + "</p>";

        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.bConnect_main)).perform(click());
        onView(withId(R.id.bConnect_main)).check(matches(withText(onNoConnect)));
        onView(withId(R.id.headset_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.headset_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.speaker_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.speaker_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void test_connectButtonBTON() {
        String onConnect = "Translate";
        String onTranslate = "Translating";
        String chat = "<p style=\"color:red\">" +
                "Let's Translate with TranslaTa!" + "</p>";

        //step one connect bluetooth
        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.bConnect_main)).perform(click());
        onView(withId(R.id.bConnect_main)).check(matches(withText(onConnect)));
        onView(withId(R.id.headset_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.headset_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.speaker_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.speaker_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        //onView(withId(R.id.wvChatHistory)).check(matches(withText(chat)));
        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        //step two begin translation
        onView(withId(R.id.bConnect_main)).perform(click());
        onView(withId(R.id.bConnect_main)).check(matches(withText(onTranslate)));
        onView(withId(R.id.headset_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.headset_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.speaker_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.speaker_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        //onView(withId(R.id.wvChatHistory)).check(matches(withText(chat)));
        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //step three stop translation
        onView(withId(R.id.off_toolbarButton)).perform(click());
        onView(withId(R.id.bConnect_main)).check(matches(withText("Connect")));
        onView(withId(R.id.headset_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.headset_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.speaker_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.speaker_glow_mainImage)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.off_toolbarButton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

    }
}
