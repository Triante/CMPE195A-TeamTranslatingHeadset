package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Language;

import java.io.IOException;

/* Main activity. Home to the UI for accessing the buttons for speech recognition, translation, and speech synthesis*/
public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    public Button bSpeechRecognition, bSpeechSynthesis, bTranslate, bTest, bSettings; //Main UI buttons
    private Toolbar myToolBar;
    private IBMSpeechToText stt; //Speech-to-text model
    private IBMTextToSpeech tts; //Text-to-speech model
    public TextView editTextField;
    public TextView translatedTextView; //Shows translated text
    boolean isInRecording = false; //flag for checking if the system is currently recording speech
    MSTranslator translator; //Translator model
    LanguageSettings languageSettings; //LanguageSettings model
    private SpeechToSpeech speechToSpeech;
    private boolean isSTS = false;
    MSAuthenticator auth;
    private boolean isOn = false;
    private AudioManager audioSwitch;
    private int speaker_mode;
    private int headset_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Create view for activity*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        myToolBar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(myToolBar);

        /* Attempt to initialize Translator model*/
        try {
            translator = new MSTranslator(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Initialize IBM speech-to-text and text-to-speech*/
        stt = new IBMSpeechToText(this);
        tts = new IBMTextToSpeech(this);
        speechToSpeech = new SpeechToSpeech(this, new MainActivity.ChatHistoryAppender() {
            @Override
            public void onAddUserText(String text) {

            }

            @Override
            public void onAddPartyText(String text) {

            }
        });

        /* Initialize buttons*/
        bSpeechRecognition = (Button) findViewById(R.id.bSpeechRecognition);
        bSpeechSynthesis = (Button) findViewById(R.id.bSpeechSynthesis);
        bTranslate = (Button) findViewById(R.id.bTranslate);
        bSettings = (Button) findViewById(R.id.bOptions);
        editTextField = (TextView) findViewById(R.id.editTextDemo);
        translatedTextView = (TextView) findViewById(R.id.translatedTextView);
        editTextField.setClickable(false);
        
        /* Declare all the on click listeners for the buttons*/
        bSpeechRecognition.setOnClickListener(this);
        bSpeechSynthesis.setOnClickListener(this);
        bTranslate.setOnClickListener(this);
        bSettings.setOnClickListener(this);
        bTest = (Button) findViewById(R.id.bTest);
        bTest.setOnClickListener(this);
        bTest.setText("S2S off");


        /*
        audioSwitch = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        speaker_mode = audioSwitch.getMode();
        headset_mode = AudioManager.MODE_IN_COMMUNICATION;
        audioSwitch.setSpeakerphoneOn(false);
        audioSwitch.setBluetoothScoOn(true);
        */
    }

    /* Method to manage all the on click listeners for the buttons*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSpeechRecognition: //Speech Recognition button listener
                
                /* Checks if the system is currently recording speech */

                if (isInRecording) {
                    
                    /* New task created to end the recording session */
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
                    
                    /* New task created to switch the state of the button from "Stop Recording" to "Start Recording" */
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bSpeechRecognition.setText("Start Recording");
                            isInRecording = false;
                        }
                    });

                }
                /* If the system is currently not recording */
                else {
                    stt.record(); //Start recording
                    
                    /* New task created to change button state from "Start Recording" to "Stop Recording" */
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
            case R.id.bSpeechSynthesis: //Speech synthesis button listener
                //tts.synthesize(translatedTextView.getText().toString(), Language.getResponseLanguageVoice()); //Performs speech synthesis on IBMTextToSpeech

                    if (!isOn) {
                        audioSwitch.startBluetoothSco();
                        audioSwitch.setMicrophoneMute(false);
                        isOn = true;
                        System.out.println(audioSwitch.isMicrophoneMute());
                    } else
                    {
                        audioSwitch.stopBluetoothSco();
                        audioSwitch.setMode(speaker_mode);
                        audioSwitch.setBluetoothScoOn(false);
                        isOn = false;
                        System.out.println(audioSwitch.isMicrophoneMute());
                    }



                break;
            case R.id.bTranslate: //Translator button listener
                final String input = translatedTextView.getText().toString(); //Gets contents of the translated text view
                
                /* New taks created to perform the translation*/
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... none) {
                        try {
                            String myCode = LanguageSettings.getMyLanguageCode(); //User's preferred languageSettings model
                            String RespCode = LanguageSettings.getResponseLanguageCode(); //Other party's preferred languageSettings model
                            final String output = translator.translate(input, myCode, RespCode); //translated text
                            
                            /* New task created to set translated text to the translated text view*/
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
            case R.id.bTest: //Testing method (not yet finished)
                if (isSTS) {
                    try {
                        speechToSpeech.stopListening();
                        bTest.setText("S2S off");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isSTS = false;
                }
                else {
                    speechToSpeech.beginListening();
                    bTest.setText("S2S on");
                    isSTS = true;
                }
                break;
            case R.id.bOptions: //Settings button listener
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
