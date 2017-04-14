package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.DemoActivity;
import com.example.triante.translatingheadsetapp.IBMSpeechToText;
import com.example.triante.translatingheadsetapp.IBMTextToSpeech;
import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.Transcript;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 * For all speech not related to language, ENGLISH: "how are you"
 * For both cases related to language, SPANISH: "con mi amigo"
 *      uses in: test_transcribeSpeechFromUser() and test_transcribeSpeechFromParty()
 */

@RunWith(AndroidJUnit4.class)
public class IBMComponentsTests {

    DemoActivity act;
    private IBMSpeechToText s2t;
    private IBMTextToSpeech t2s;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateIBMComponent() {
        act = mActivityRule.getActivity();
        s2t = new IBMSpeechToText(act);
        t2s = new IBMTextToSpeech(act);
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_F);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_F);
    }

    //IBM Speech Recognition Tests
    @Test
    public void test_emptyTranscriptList() throws IOException, InterruptedException {
        s2t.record();
        Thread.sleep(3000);
        boolean toBeFalse = s2t.isSpeechAvailable();
        s2t.end();
        assertFalse("Empty Transcript List Test", toBeFalse);
    }

    @Test
    public void test_nullTranscript() throws IOException, InterruptedException {
        s2t.record();
        Thread.sleep(3000);
        Transcript t = s2t.speech();
        s2t.end();
        assertNull("Null Transcript Text when retrieving from an empty Transcript list test", t);
    }

    @Test
    public void test_captureSpeechTranscriptList() throws InterruptedException, IOException {
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you'
        boolean toBeTrue = s2t.isSpeechAvailable();
        s2t.end();
        assertTrue("Transcript List with contents test", toBeTrue);
    }

    @Test
    public void test_captureSpeech() throws InterruptedException, IOException {
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you'
        String expectedString = "how are you";
        String toBeTested = s2t.speech().getSpeech();
        s2t.end();
        assertEquals("\"How Are You\" speech recognition test", toBeTested, expectedString);
    }


    @Test void test_profanityFilterEnabled() throws InterruptedException, IOException {
        TranslaTaSettings.setProfanityFilter(true);
        s2t.record();
        Thread.sleep(10000);
        //say 'Fuck you'
        String expectedString = "****";
        String toBeTested = s2t.speech().getSpeech();
        s2t.end();
        assertEquals("\"Fuck you\" speech recognition test with profanity filter enabled", toBeTested, expectedString);
    }

    @Test void test_profanityFilterDisabled() throws InterruptedException, IOException {
        TranslaTaSettings.setProfanityFilter(true);
        s2t.record();
        Thread.sleep(10000);
        //say 'Fuck you'
        String expectedString = "fuck you";
        String toBeTested = s2t.speech().getSpeech();
        s2t.end();
        assertEquals("\"Fuck you\" speech recognition test with profanity filter enabled", toBeTested, expectedString);
    }

    @Test
    public void test_captureSpeechIdentifyUser() throws InterruptedException, IOException {
        TranslaTaSettings.setAmplitudeThreshold(0);
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you' with english set as user
        int userExpected = 0;
        int userToBeTested = s2t.speech().getUser();
        s2t.end();
        assertEquals("User chosen when amplitude is greater than threshold", userToBeTested, userExpected, 0);
    }

    @Test
    public void test_captureSpeechIdentifyParty() throws InterruptedException, IOException {
        TranslaTaSettings.setAmplitudeThreshold(Integer.MAX_VALUE);
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you' with english set as party
        int userExpected = 1;
        int userToBeTested = s2t.speech().getUser();
        s2t.end();
        assertEquals("Party chosen when amplitude is less than threshold", userToBeTested, userExpected, 0);
    }

    @Test
    public void test_transcribeSpeechFromUser() throws InterruptedException, IOException {
        LanguageSettings.setLanguage(true, LanguageSettings.Language.SPANISH_F);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.JAPANESE);
        TranslaTaSettings.setAmplitudeThreshold(0);
        s2t.record();
        Thread.sleep(10000);
        //say "con mi amigo" (with my friend) with spanish set as user
        String expectedString = "con mi amigo";
        int expectedUser = 0;
        Transcript t = s2t.speech();
        String stringToBeTested = t.getSpeech();
        int intToBeTested = t.getUser();
        s2t.end();
        assertEquals("transcript is in user language when amplitude is greater than threshold", stringToBeTested, expectedString);
        assertEquals("User should be user", intToBeTested, expectedUser, 0);
    }

    @Test
    public void test_transcribeSpeechFromParty() throws InterruptedException, IOException {
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_F);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.SPANISH_F);
        TranslaTaSettings.setAmplitudeThreshold(0);
        s2t.record();
        Thread.sleep(10000);
        //say "con mi amigo" (with my friend) with spanish set as user
        String expectedString = "con mi amigo";
        int expectedUser = 1;
        Transcript t = s2t.speech();
        String stringToBeTested = t.getSpeech();
        int intToBeTested = t.getUser();
        s2t.end();
        assertEquals("transcript is in user language when amplitude is greater than threshold", stringToBeTested, expectedString);
        assertEquals("User should be user", intToBeTested, expectedUser, 0);
    }

    @Test
    public void test_onlyUserSpeechCaptured() throws InterruptedException, IOException {
        TranslaTaSettings.setMaxAmplitude(0);
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you'
        String expectedString = "how are you";
        Transcript t = s2t.speech();
        boolean shouldBeEmpty = s2t.isSpeechAvailable();
        int expectedUser = 0;
        int userToBeTested = t.getUser();
        s2t.end();
        assertEquals("User ID should be user (0)", userToBeTested, expectedUser, 0);
        assertFalse("Empty list to verify no other transcript created", shouldBeEmpty);
    }

    @Test
    public void test_onlyPartySpeechCaptured() throws InterruptedException, IOException {
        TranslaTaSettings.setMaxAmplitude(Integer.MAX_VALUE);
        s2t.record();
        Thread.sleep(10000);
        //say 'How Are you'
        String expectedString = "how are you";
        Transcript t = s2t.speech();
        boolean shouldBeEmpty = s2t.isSpeechAvailable();
        int expectedUser = 1;
        int userToBeTested = t.getUser();
        s2t.end();
        assertEquals("User ID should be party (1)", userToBeTested, expectedUser, 0);
        assertFalse("Empty list to verify no other transcript created", shouldBeEmpty);
    }


    //IBM Speech Synthesis Tests
    @Test
    public void test_speechSynthesis1() throws InterruptedException {
        String toSpeech = "This is speech converted by IBM";
        Voice voice = Voice.EN_MICHAEL;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: ENGLISH" +
                toSpeech, true);
    }

    @Test
    public void test_speechSynthesis2() throws InterruptedException {
        String toSpeech = "Con mi amigo";
        Voice voice = Voice.ES_ENRIQUE;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: SPANISH" +
                toSpeech, true);
    }

    @Test
    public void test_speechSynthesis3() throws InterruptedException {
        String toSpeech = "このはにほんごです";
        Voice voice = Voice.JA_EMI;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: JAPANESE" +
                toSpeech, true);
    }

}
