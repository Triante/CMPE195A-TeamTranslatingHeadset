package com.example.triante.translatingheadsetapp.IntegrationTests;


import android.os.CountDownTimer;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.Microphone;
import com.example.triante.translatingheadsetapp.R;
import com.example.triante.translatingheadsetapp.Transcript;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Jorge Aguiniga on 4/23/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MicrophoneS2T {

    private Microphone mic;
    private MainActivity act;
    boolean stayIn = true;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initiateMic() {
        stayIn = true;
        act = mainActivityRule.getActivity();
        mic = new Microphone(act);
        mic.convertSpeechToText();
    }

    @Test
    public void test_NullTranscript() throws InterruptedException {
        Thread.sleep(2000);
        Transcript t = mic.retrieveConvertedTranscript();
        assertNull("Empty Transcript on no sound", t);
    }

    @Test
    public void test_WithTranscript() throws InterruptedException {
        Transcript t = mic.retrieveConvertedTranscript();
        mainActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(25000, 25000) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        stayIn = false;
                    }
                }.start();
            }
        });
        while (t == null && stayIn) {
            t = mic.retrieveConvertedTranscript();
        }
        assertNotNull("Transcript with contents when voice is heard", t);
    }

    @After
    public void endMic() throws IOException {
        mic.end();
    }

}
