package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.speech_to_text.v1.ISpeechDelegate;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.android.speech_to_text.v1.dto.SpeechConfiguration;
import com.ibm.watson.developer_cloud.android.text_to_speech.v1.TextToSpeech;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements ISpeechDelegate, View.OnClickListener {

    Button bSpeechRecognition, bSpeechSynthesis, bTranslate;
    EditText editTextField;
    TextView translatedTextView;
    boolean isInRecording = false;
    MSTranslator translator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            translator = new MSTranslator();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Instantiation of SpeechToText
        String sttURL = getString(R.string.SpeechRecognitionURLTokenFactory);
        String sstUsername = getString(R.string.SpeechRecognitionUsername);
        String sstPass = getString(R.string.SpeechRecognitionPassword);
        String sstServiceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
        try {
            SpeechToText.sharedInstance().initWithContext(new URI(sstServiceURL), this.getApplicationContext(), new SpeechConfiguration());
            SpeechToText.sharedInstance().setCredentials(sstUsername, sstPass);
            SpeechToText.sharedInstance().setDelegate(this);
        } catch (URISyntaxException e) {
            Toast.makeText(this.getApplicationContext(), "IBM Watson's Speech Recognition Failed to Authenticate", Toast.LENGTH_SHORT).show();
        }

        //Instantiation of TextToSpeech
        String ttsURL = getString(R.string.SpeechSynthesisURLTokenFactory);
        String ttsUsername = getString(R.string.SpeechSynthesisUsername);
        String ttsPass = getString(R.string.SpeechRecognitionPassword);
        String ttsServiceURL = "https://stream.watsonplatform.net/text-to-speech/api";
        try {
            TextToSpeech.sharedInstance().initWithContext(new URI(ttsServiceURL));
            TextToSpeech.sharedInstance().setCredentials(ttsUsername, ttsPass);
        }
        catch (URISyntaxException e) {

            Toast.makeText(this.getApplicationContext(), "IBM Watson's Speech Synthesis Failed to Authenticate", Toast.LENGTH_SHORT).show();
        }

        bSpeechRecognition = (Button) findViewById(R.id.bSpeechRecognition);
        bSpeechSynthesis = (Button) findViewById(R.id.bSpeechSynthesis);
        bTranslate = (Button) findViewById(R.id.bTranslate);
        editTextField = (EditText) findViewById(R.id.editTextDemo);
        translatedTextView = (TextView) findViewById(R.id.translatedTextView);
        editTextField.setText(ttsURL);

        bSpeechRecognition.setOnClickListener(this);
        bSpeechSynthesis.setOnClickListener(this);
        bTranslate.setOnClickListener(this);

    }

    @Override
    public void onOpen() {
        // the  connection to the STT service is successfully opened
    }

    @Override
    public void onError(String s) {
        // error interacting with the STT service
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        // the connection with the STT service was just closed
    }

    @Override
    public void onMessage(String s) {
        // a message comes from the STT service with recognition results
        final String mes = s;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                translatedTextView.setText(mes);
            }
        });

    }

    @Override
    public void onAmplitude(double v, double v1) {
        // area where user and party identification will be done
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSpeechRecognition:
                if (isInRecording) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... none) {
                            SpeechToText.sharedInstance().stopRecording();
                            return null;
                        }
                    }.execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isInRecording = false;
                            bSpeechRecognition.setText("Start Recording");
                        }
                    });

                }
                //not capturing audio
                else {
                    SpeechToText.sharedInstance().recognize();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bSpeechRecognition.setText("Stop Recording");
                            isInRecording = true;
                        }
                    });

                }
                break;
            case R.id.bSpeechSynthesis:
                String toSpeech = editTextField.getText().toString();
                TextToSpeech.sharedInstance().setVoice("en-US_MichaelVoice");
                TextToSpeech.sharedInstance().synthesize(toSpeech);
                break;
            case R.id.bTranslate:
                final String input = editTextField.getText().toString();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... none) {
                        try {
                            final String output = translator.translate(input, "en", "de");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    translatedTextView.setText(output);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();

                break;
        }
    }
}
