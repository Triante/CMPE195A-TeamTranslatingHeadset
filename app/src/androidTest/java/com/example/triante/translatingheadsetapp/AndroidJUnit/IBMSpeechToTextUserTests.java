package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.TextView;

import com.example.triante.translatingheadsetapp.DemoActivity;
import com.example.triante.translatingheadsetapp.IBMSpeechToText;
import com.example.triante.translatingheadsetapp.IBMTextToSpeech;
import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.R;
import com.example.triante.translatingheadsetapp.Transcript;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 * For all speech not related to language, ENGLISH: "how are you"
 * This test case contains the test for profanity filter disabled (off)
 */

@RunWith(AndroidJUnit4.class)
public class IBMSpeechToTextUserTests {

    DemoActivity act;
    private IBMSpeechToText s2t;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateIBMComponent() {
        act = mActivityRule.getActivity();
        TranslaTaSettings.setProfanityFilter(false);
        TranslaTaSettings.setAmplitudeThreshold(100000);
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_F);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.JAPANESE);
        s2t = new IBMSpeechToText(act);
        s2t.record();
    }

    @After
    public void turnOffSpeechToText() throws IOException {
        s2t.end();
    }
    //IBM Speech Recognition User Tests

    @Test
    public void test_profanityFilterDisabled() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView t = (TextView) act.findViewById(R.id.translatedTextView);
                    t.setText(s2t.messageForTesting);
                }
            });
        }
        //say 'Fuck you'
        String expectedString = "you are a bitch";
        String toBeTested = s2t.speech().getSpeech();
        assertEquals("\"Fuck you\" speech recognition test with profanity filter enabled", toBeTested.trim().toLowerCase(), expectedString);
    }

    @Test
    public void test_captureSpeechIdentifyUser() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView t = (TextView) act.findViewById(R.id.translatedTextView);
                    t.setText(s2t.messageForTesting);
                }
            });
        }
        //say 'How are you' with english set as user
        int userExpected = 0;
        int userToBeTested = s2t.speech().getUser();
        assertEquals("User chosen when amplitude is greater than threshold", userToBeTested, userExpected, 0);
    }

    @Test
    public void test_transcribeSpeechFromUser() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView t = (TextView) act.findViewById(R.id.translatedTextView);
                    t.setText(s2t.messageForTesting);
                }
            });
        }
        //say "How are you"
        String expectedString = "how are you";
        int expectedUser = 0;
        Transcript t = s2t.speech();
        String stringToBeTested = t.getSpeech();
        int intToBeTested = t.getUser();
        assertEquals("transcript is in user language when amplitude is greater than threshold", stringToBeTested.trim().toLowerCase(), expectedString);
        assertEquals("User should be user", intToBeTested, expectedUser, 0);
    }

    @Test
    public void test_onlyUserSpeechCaptured() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView t = (TextView) act.findViewById(R.id.translatedTextView);
                    t.setText(s2t.messageForTesting);
                }
            });
        }
        //say 'How are you'
        Transcript t = s2t.speech();
        boolean shouldBeEmpty = s2t.isSpeechAvailable();
        int expectedUser = 0;
        int userToBeTested = t.getUser();
        assertEquals("User ID should be user (0)", userToBeTested, expectedUser, 0);
        assertFalse("Empty list to verify no other transcript created", shouldBeEmpty);
    }

}
