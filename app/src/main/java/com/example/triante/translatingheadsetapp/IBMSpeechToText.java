package com.example.triante.translatingheadsetapp;


import android.content.Context;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMSpeechToText implements ISpeechDelegate{

    public IBMSpeechToText(Context context)  {
        String sttURL = context.getString(R.string.SpeechRecognitionURLTokenFactory);
        String sstUsername = context.getString(R.string.SpeechRecognitionUsername);
        String sstPass = context.getString(R.string.SpeechRecognitionPassword);
        String sstServiceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
        try {
            SpeechToText.sharedInstance().initWithContext(new URI(sstServiceURL), context.getApplicationContext(), new SpeechConfiguration());
            SpeechToText.sharedInstance().setCredentials(sstUsername, sstPass);
            SpeechToText.sharedInstance().setDelegate(this);
        } catch (URISyntaxException e) {

        }
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onAmplitude(double v, double v1) {

    }
}
