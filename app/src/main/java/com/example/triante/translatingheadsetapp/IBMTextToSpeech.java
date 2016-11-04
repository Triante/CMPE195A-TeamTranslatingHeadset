package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.text_to_speech.v1.TextToSpeech;

import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMTextToSpeech {


    public IBMTextToSpeech (Context instance)
    {

        //Instantiation of TextToSpeech
        String ttsURL = instance.getString(R.string.SpeechSynthesisURLTokenFactory);
        String ttsUsername = instance.getString(R.string.SpeechSynthesisUsername);
        String ttsPass = instance.getString(R.string.SpeechSynthesisPassword);
        String ttsServiceURL = "https://stream.watsonplatform.net/text-to-speech/api";
        try {
            TextToSpeech.sharedInstance().initWithContext(new URI(ttsServiceURL));
            TextToSpeech.sharedInstance().setCredentials(ttsUsername, ttsPass);
        }
        catch (URISyntaxException e) {

            Toast.makeText(instance.getApplicationContext(), "IBM Watson's Speech Synthesis Failed to Authenticate", Toast.LENGTH_SHORT).show();
        }

    }

    public void synthesize(String speech, String language) {
        TextToSpeech.sharedInstance().setVoice("en-US_MichaelVoice");
        TextToSpeech.sharedInstance().synthesize(speech);
    }


}
