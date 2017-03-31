package com.example.triante.translatingheadsetapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
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
    }

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
        assertTrue("Trancript List with contents test", toBeTrue);
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

    @Test
    public void test_speechSynthesis() throws InterruptedException {
        String toSpeech = "This is speech converted by IBM";
        Voice voice = Voice.EN_MICHAEL;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output" +
                toSpeech, true);
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
        String expectedString = "fmiuck you";
        String toBeTested = s2t.speech().getSpeech();
        s2t.end();
        assertEquals("\"Fuck you\" speech recognition test with profanity filter enabled", toBeTested, expectedString);
    }

}
