package com.example.triante.translatingheadsetapp;

import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static com.example.triante.translatingheadsetapp.R.id.bSpeechRecognition;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMSpeechToText implements ISpeechDelegate {
    private MainActivity instance;
    private boolean isInRecording;
    private String message;
    private double amplitude;

    public IBMSpeechToText(MainActivity instance)
    {

        this.instance = instance;

        //Instantiation of SpeechToText
        String sstUsername = instance.getString(R.string.SpeechRecognitionUsername);
        String sstPass = instance.getString(R.string.SpeechRecognitionPassword);
        String sstServiceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
        isInRecording = false;

        try
        {
            SpeechToText.sharedInstance().initWithContext(new URI(sstServiceURL), instance.getApplicationContext(), new SpeechConfiguration());
            SpeechToText.sharedInstance().setCredentials(sstUsername, sstPass);
            SpeechToText.sharedInstance().setDelegate(this);
        } catch (URISyntaxException e)
        {
            Toast.makeText(instance.getApplicationContext(), "IBM Watson's Speech Recognition Failed to Authenticate", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onOpen()
    {

    }

    @Override
    public void onError(String s)
    {

    }

    @Override
    public void onClose(int i, String s, boolean b)
    {

    }

    @Override
    public void onMessage(String s)
    {
        if (s.contains("transcript") && isInRecording)
        {
            try
            {
                JSONObject object = new JSONObject(s);
                JSONArray result = object.getJSONArray("results");
                JSONObject alt = result.getJSONObject(0);
                JSONArray alternatives = alt.getJSONArray("alternatives");
                message = alternatives.getJSONObject(0).getString("transcript");
                final String mes = message;
                instance.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                       instance.editTextField.setText(mes);
                    }
                });
            } catch (org.json.JSONException e)
            {
                e.getMessage();
            }
        }
    }

    @Override
    public void onAmplitude(double v, double v1)
    {
        amplitude = v;
    }

    public String speech()
    {
        //end();
        return message;
    }

    public void record()
    {
        if (!isInRecording)
        {
            SpeechToText.sharedInstance().recognize();
            isInRecording = true;
        }
    }

    public void end()
    {
        if (isInRecording)
        {
            SpeechToText.sharedInstance().stopRecording();
            isInRecording = false;
        }
    }
}
