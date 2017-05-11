package com.example.triante.translatingheadsetapp.IntegrationTests;

import android.os.CountDownTimer;
import android.support.test.rule.ActivityTestRule;

import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.SpeechToSpeech;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Jorge Aguiniga on 4/23/2017.
 */

public class S2STranslation {

    private SpeechToSpeech s2s;
    private String spokenText;
    private String translatedText;
    private String ErrorText;
    private boolean stayIn = true;
    private boolean first = true;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initiateSpeechToSpeech() {
        MainActivity act = mainActivityRule.getActivity();
        spokenText = "";
        translatedText = "";
        ErrorText = "";
        stayIn = true;
        first = true;
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_US_ALLISON);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.SPANISH_MX_SOFIA);
        s2s = new SpeechToSpeech(act, new ChatAppender());
        s2s.beginListening();
    }

    @After
    public void endSpeechToSpeech() throws IOException {
        s2s.stopListening();
    }

    @Test
    public void test_noSpeech() throws InterruptedException {
        Thread.sleep(10000);
        assertTrue("Manual test, make sure that no output was done", true);
    }

    @Test
    public void test_noSpeechFromUser() throws InterruptedException {
        String spoken = "User (Spoken):  my car is at the beach";
        String generated = "User (Translated):  mi coche está en la playa";
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(30000, 30000) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        stayIn = false;
                    }
                }.start();
            }
        });
        while (stayIn) {}
        Thread.sleep(5000);
        assertTrue("Manual test, check if spoken english was returned in spanish", true);
        assertEquals("User speech spoken english", spoken.toLowerCase(), spokenText.toLowerCase());
        assertEquals("Synthesized speech as spanish", generated.toLowerCase(), translatedText.toLowerCase());
    }

    @Test
    public void test_noSpeechFromParty() throws InterruptedException {
        String spoken = "Party (Spoken):  hoy vamos a la playa";
        String generated = "Party (Translated):  Today we go to the beach";
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(30000, 30000) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        stayIn = false;
                    }
                }.start();
            }
        });
        while (stayIn) {}
        Thread.sleep(5000);
        assertTrue("Manual test, check if spoken english was returned in spanish", true);
        assertEquals("User speech spoken english", spoken.toLowerCase(), spokenText);
        assertEquals("Synthesized speech as spanish", generated.toLowerCase(), translatedText);
    }




    class ChatAppender implements MainActivity.ChatHistoryAppender {

        @Override
        public void onAddUserText(String text) {
            if (first) {
                spokenText = text;
            }
            else  {
                translatedText = text;
            }

        }

        @Override
        public void onAddPartyText(String text) {
            if (first) {
                spokenText = text;
            }
            else  {
                translatedText = text;
            }
        }

        @Override
        public void onAddErrorText(String text) {
            ErrorText = text;
        }
    }
}
