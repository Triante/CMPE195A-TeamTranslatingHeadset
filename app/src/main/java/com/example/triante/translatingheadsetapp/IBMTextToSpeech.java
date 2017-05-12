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

/**
 * Text-to-Speech conversions when text has been translated from one language to another
 */
public class IBMTextToSpeech {

    private TextToSpeech textToSpeech; //IBM text-to-speech object to access all of its base methods
    private StreamPlayer player = new StreamPlayer(); //Used for playback to an output device


    /**
     * Constructor for IBMTextToSpeech
     * @param instance (context for the main activity)
     */
    public IBMTextToSpeech (Context instance) {

        //URL for IBM's text-to-speech cloud service cloud token factory
        String ttsURL = instance.getString(R.string.SpeechSynthesisURLTokenFactory);
        
        // Initialize credentials for accessing the IBM text-to-speech cloud service
        String ttsUsername = instance.getString(R.string.SpeechSynthesisUsername);
        String ttsPass = instance.getString(R.string.SpeechSynthesisPassword);
        
         //URL for IBM's text-to-speech cloud service cloud
        String ttsServiceURL = "https://stream.watsonplatform.net/text-to-speech/api";
        
        // Begin session
        textToSpeech = new TextToSpeech();
        textToSpeech.setUsernameAndPassword(ttsUsername, ttsPass);

    }
    
    /**
     * Converts text input to speech and project it to an audio output device
     */
    public void synthesize(String speech, Voice language, AudioManager manager) {
        new SynthesizeTask(language, manager).execute(speech);
    }

    /**
     *  Inner class for performing the text-to-speech process as a separate task from the main thread
     */
    private class SynthesizeTask extends AsyncTask<String, Void, String> {

        private Voice voice; //voice to use for synthesizing current speech stream
        private AudioManager manager; //audio manager to switch Bluetooth SCO off

        /**
         * Constructor for SynthesizeTask
         * @param v (voice for the speech stream)
         * @param audio_manager (audio manager instance for handing Bluetooth event)
         */
        public SynthesizeTask(Voice v, AudioManager audio_manager) {
            voice = v;
            manager = audio_manager;
        }

        @Override
        protected String doInBackground(String... params) {

            //playback execution
            player.playStream(textToSpeech.synthesize(params[0], voice).execute());

            //stop SCO execution
            manager.stopBluetoothSco();

            //make sure SCO is stopped
            manager.setBluetoothScoOn(false);

            return "Did syntesize";
        }
    }

}
