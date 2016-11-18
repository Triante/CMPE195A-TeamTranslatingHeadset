package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Main class for converting speech input into text based on a user-specified language input */
public class IBMSpeechToText  {
    private MainActivity instance; //instance of the main activity to send information from this class to the activity UI
    private String message; //placeholder for the speech being converted
    private double amplitude; //placeholder for the peak amplitude of the speech input

    private SpeechToText speechToText; //IBM-specific speech-to-text object
    private MicrophoneInputStream micInput; //Input stream for getting the speech input from microphone
    private boolean isInRecording; //flag for use to check if system is currently in recording mode

    public IBMSpeechToText(MainActivity instance)
    {
        this.instance = instance;

        /* Credential initialization for getting access to IBM Watson cloud service */
        String sstUsername = instance.getString(R.string.SpeechRecognitionUsername);
        String sstPass = instance.getString(R.string.SpeechRecognitionPassword);
        
        /* URL for Cloud*/
        String sstServiceURL = "https://stream.watsonplatform.net/speech-to-text/api";
        
        /* Initialize all other speech-to-text components (flags, library objects, etc.) */
        isInRecording = false;
        speechToText = new SpeechToText();
        speechToText.setUsernameAndPassword(sstUsername, sstPass);
        speechToText.setEndPoint(sstServiceURL);
    }

    /* Getter for extracting current speech converted by the IBM Speech-to-Text object */
    public String speech()
    {
        return message;
    }

    /* Method to begin the recording process*/
    public void record()
    {
        /* Don't start recording process if it is already running */
        if (isInRecording) return;
        
        
        micInput =  new MicrophoneInputStream(true);
        
        /* Make the recording its own separate process */
        new Thread(new Runnable() {
            @Override
            public void run() {
                MicrophoneRecognizeCallback callback = new MicrophoneRecognizeCallback(); //Creat a message retrieving object
                RecognizeOptions options = getRecognizeOptions(); //Create an option object for speech preferences
                speechToText.recognizeUsingWebSocket(micInput, options, callback); //Initialize recognizer with defined preferences
            }
        }).start();
        isInRecording = true;
    }

    /* Method to end the speech recognition process*/
    public void end() throws IOException {
        /* Don't continue if process is not already running */
        if (!isInRecording) return;
        
        micInput.close();
        isInRecording = false;

    }
    
    /* Retrieve preferences for the speech-to-text process*/
    private RecognizeOptions getRecognizeOptions() {
        /* Create and initialize an options builder for setting up speech-to-text preferences */
        RecognizeOptions.Builder build = new RecognizeOptions.Builder();
        build.continuous(true);
        build.contentType(ContentType.OPUS.toString());
        
        /* Set the language for speech-to-text */
        String myLanguageModel = Language.getMyLanguageModel();
        build.model(myLanguageModel);
        build.interimResults(true);
        build.inactivityTimeout(2000);
        RecognizeOptions option = build.build();
        return option;
    }

    /* Inner class for extracting the converted speech from the Speech-to-text object */
    private class MicrophoneRecognizeCallback extends BaseRecognizeCallback {
        @Override
        public void onTranscription(SpeechResults speechResults) {
            /* Does not continue if the system is not recording */
            if(!isInRecording) return;
            
            /* Extracts the converted speech and stores it in a string */
            System.out.println(speechResults);
            message = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
            final String mes = message;
            
            /* Writes the text to a text field on the current activity UI */
            instance.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        instance.translatedTextView.setText(mes);
                    }
                });
        }
    }
}
