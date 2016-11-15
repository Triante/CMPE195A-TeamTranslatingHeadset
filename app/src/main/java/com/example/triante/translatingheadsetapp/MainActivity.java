package com.example.triante.translatingheadsetapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button bSpeechRecognition, bSpeechSynthesis, bTranslate, bTest, bSettings;
    private IBMSpeechToText stt;
    private IBMTextToSpeech tts;
    public TextView editTextField;
    public TextView translatedTextView;
    boolean isInRecording = false;
    MSTranslator translator;
    Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            translator = new MSTranslator();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stt = new IBMSpeechToText(this);
        tts = new IBMTextToSpeech(this);
        //language = new Language(this);

        bSpeechRecognition = (Button) findViewById(R.id.bSpeechRecognition);
        bSpeechSynthesis = (Button) findViewById(R.id.bSpeechSynthesis);
        bTranslate = (Button) findViewById(R.id.bTranslate);
        bSettings = (Button) findViewById(R.id.bOptions);
        editTextField = (TextView) findViewById(R.id.editTextDemo);
        translatedTextView = (TextView) findViewById(R.id.translatedTextView);
        editTextField.setClickable(false);

        bSpeechRecognition.setOnClickListener(this);
        bSpeechSynthesis.setOnClickListener(this);
        bTranslate.setOnClickListener(this);
        bSettings.setOnClickListener(this);

        bTest = (Button) findViewById(R.id.bTest);
        bTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSpeechRecognition:
                if (isInRecording) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... none) {
                            try {
                                stt.end();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bSpeechRecognition.setText("Start Recording");
                            isInRecording = false;
                        }
                    });
                }
                else {
                    stt.record();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            bSpeechRecognition.setText("Stop Recording");
                            isInRecording = true;
                        }
                    });
                }
                break;
            case R.id.bSpeechSynthesis:
                tts.synthesize(translatedTextView.getText().toString(), "");
                break;
            case R.id.bTranslate:
                final String input = translatedTextView.getText().toString();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... none) {
                        try {
                            String myCode = Language.getMyLanguageCode();
                            String RespCode = Language.getResponseLanguageCode();
                            final String output = translator.translate(input, myCode, RespCode);
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
            case R.id.bTest:
                //language.getSupportedSpeech();
                break;
            case R.id.bOptions:
                Intent options = new Intent(this, SettingsActivity.class);
                startActivity(options);
                break;
        }
    }
}