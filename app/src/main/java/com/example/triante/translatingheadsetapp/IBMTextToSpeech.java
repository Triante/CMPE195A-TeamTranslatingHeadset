package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMTextToSpeech {

    TextToSpeech textToSpeech;
    StreamPlayer player = new StreamPlayer();


    public IBMTextToSpeech (Context instance)
    {
        //Instantiation of TextToSpeech
        String ttsURL = instance.getString(R.string.SpeechSynthesisURLTokenFactory);
        String ttsUsername = instance.getString(R.string.SpeechSynthesisUsername);
        String ttsPass = instance.getString(R.string.SpeechSynthesisPassword);
        String ttsServiceURL = "https://stream.watsonplatform.net/text-to-speech/api";
        textToSpeech = new TextToSpeech();
        textToSpeech.setUsernameAndPassword(ttsUsername, ttsPass);
    }

    public void synthesize(String speech, String language) {
        new SynthesizeTask().execute(speech);
    }

    private class SynthesizeTask extends AsyncTask<String, Void, String> {

        @Override protected String doInBackground(String... params) {
            Voice responseVoice = Language.getResponseLanguageVoice();
            player.playStream(textToSpeech.synthesize(params[0], responseVoice).execute());
            return "Did syntesize";
        }
    }

}
