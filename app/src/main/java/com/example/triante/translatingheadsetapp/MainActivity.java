package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button bSpeechRecognition, bSpeechSynthesis, bTranslate;
    private IBMSpeechToText stt;
    private IBMTextToSpeech tts;
    public EditText editTextField;
    boolean isInRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stt = new IBMSpeechToText(this);
        tts = new IBMTextToSpeech(this);


        bSpeechRecognition = (Button) findViewById(R.id.bSpeechRecognition);
        bSpeechSynthesis = (Button) findViewById(R.id.bSpeechSynthesis);
        bTranslate = (Button) findViewById(R.id.bTranslate);
        editTextField = (EditText) findViewById(R.id.editTextDemo);
        editTextField.setText("this text");

        bSpeechRecognition.setOnClickListener(this);
        bSpeechSynthesis.setOnClickListener(this);
        bTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Microsoft's Cognitive Service not implemented yet.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSpeechRecognition:
                if (isInRecording) {
                    stt.end();
                    runOnUiThread(new Runnable()
                    {
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
        }
    }
}