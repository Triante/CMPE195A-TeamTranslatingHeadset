package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class to do the text-to-speech conversions when text has been translated from one language to another */
public class IBMTextToSpeech {

    TextToSpeech textToSpeech; //IBM text-to-speech object to access all of its base methods
    StreamPlayer player = new StreamPlayer(); //Used for playback to an output device


    public IBMTextToSpeech (Context instance) {
        /*URL for IBM's text-to-speech cloud service cloud token factory*/
        String ttsURL = instance.getString(R.string.SpeechSynthesisURLTokenFactory);
        
        /* Initialize credentials for accessing the IBM text-to-speech cloud service*/
        String ttsUsername = instance.getString(R.string.SpeechSynthesisUsername);
        String ttsPass = instance.getString(R.string.SpeechSynthesisPassword);
        
         /*URL for IBM's text-to-speech cloud service cloud*/
        String ttsServiceURL = "https://stream.watsonplatform.net/text-to-speech/api";
        
        /* Begin session*/
        textToSpeech = new TextToSpeech();
        textToSpeech.setUsernameAndPassword(ttsUsername, ttsPass);
    }
    
    /* Method to convert text input to speech and project it to an audio output device*/
    public void synthesize(String speech, Voice language, AudioManager manager, int mode) {
        new SynthesizeTask(language, manager, mode).execute(speech);
    }

    /* Inner class for performing the text-to-speech process as a separate task from the main thread*/
    private class SynthesizeTask extends AsyncTask<String, Void, String> {

        private Voice voice;
        private AudioManager audioSwitch;
        private int audioMode;

        public SynthesizeTask(Voice v, AudioManager theSwitch, int audio) {
            voice = v;
            audioSwitch = theSwitch;
            audioMode = audio;
        }

        @Override protected String doInBackground(String... params) {
            player.playStream(textToSpeech.synthesize(params[0], voice).execute()); //playback execution
            audioSwitch.stopBluetoothSco();
            audioSwitch.setMode(audioMode);
            return "Did syntesize";
        }
    }

}
