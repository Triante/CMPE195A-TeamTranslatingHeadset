package com.example.triante.translatingheadsetapp.IntegrationTests;


import android.content.Context;
import android.media.AudioManager;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.IBMTextToSpeech;
import com.example.triante.translatingheadsetapp.LanguageSettings;
import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.Speaker;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Jorge Aguiniga on 4/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class SpeakerT2S {

    private Speaker testSpeaker;
    private IBMTextToSpeech testSpeech;
    private AudioManager testManager;
    private Voice myVoice;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initialize()
    {
        testSpeaker = new Speaker(mainActivityRule.getActivity());
        testSpeech = new IBMTextToSpeech(mainActivityRule.getActivity());
        testManager = (AudioManager)mainActivityRule.getActivity().getSystemService(Context.AUDIO_SERVICE);
        myVoice = LanguageSettings.getMyLanguageVoice();
    }

    @Test
    public void test_Delegation()
    {
        testSpeech.synthesize("Colorful is a positive word", myVoice, testManager);
    }

    @Test
    public void test_PartySpeechOutput()
    {
        testSpeaker.synthesizeSpeech("Lleno de color es una palabra positiva", 0);
    }

    @Test
    public void test_UserSpeechOutput()
    {
        testSpeaker.synthesizeSpeech("Colorful is a positive word", 0);
    }

}
