package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button bSpeechRecognition, bSpeechSynthesis, bTranslate;
    private IBMSpeechToText stt;
    private IBMTextToSpeech tts;
    public EditText editTextField;
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

        stt = new IBMSpeechToText(this);
        tts = new IBMTextToSpeech(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSpeechRecognition:
                if (isInRecording) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... none) {
                            stt.end()
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
                tts.synthesize(editTextField.getText().toString(), "");
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
