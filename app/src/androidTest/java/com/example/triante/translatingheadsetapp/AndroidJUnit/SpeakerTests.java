package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.content.Context;
import android.media.AudioManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.Bluetooth;
import com.example.triante.translatingheadsetapp.DemoActivity;
import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.Speaker;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Jorge Aguiniga on 4/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class SpeakerTests {
    private Speaker spkClass;
    private final String RESULT_USER_PLAYBACK = "SPEAKER";
    private final String RESULT_PARTY_PLAYBACK = "HEADSET";
    private AudioManager manager;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void startSpeaker() {
        spkClass = new Speaker(mainActivityRule.getActivity());
        manager = (AudioManager)mainActivityRule.getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Test
    public void test_userSpoke() {
        String playback = spkClass.synthesizeSpeech("I am going to the park", 0);
        assertEquals("Check Playback to Speaker", RESULT_USER_PLAYBACK, playback);
    }

    @Test
    public void test_partySpoke() {
        String playback = spkClass.synthesizeSpeech("I am going to the park", 1);
        assertEquals("Check Playback to Speaker", RESULT_PARTY_PLAYBACK, playback);
    }
}
