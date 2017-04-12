package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.util.Log;

import com.ibm.watson.developer_cloud.android.library.audio.AmplitudeListener;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Main class for converting speech input into text based on a user-specified languageSettings input */
public class IBMSpeechToText {
    private Context instance; //instance of the main activity to send information from this class to the activity UI
    private String message = ""; //placeholder for the speech being converted
    private ArrayList<Transcript> messagesRecognized;
    private double amplitude; //placeholder for the peak amplitude of the speech input
    private double userAmplitudeLevel = 2E7;

    private SpeechToText speechToTextUser; //IBM-specific speech-to-text object
    private SpeechToText speechToTextParty;
    //private MicrophoneInputStream micInput; //Input stream for getting the speech input from microphone
    private MicrophoneInputStream micInput2;
    private MultipleMicrophoneInputStream micDuelInput;
    private MicrophoneInputStreamReader streamOne;
    private MicrophoneInputStreamReader streamTwo;
    private AmplitudeAverageCalculator calculator;
    private boolean isInRecording; //flag for use to check if system is currently in recording mode
    double amp = 0;
    double vol = 0;

    public IBMSpeechToText(Context instance) {
        this.instance = instance;
        messagesRecognized = new ArrayList<>();

        /* Credential initialization for getting access to IBM Watson cloud service */
        String sstUsername = instance.getString(R.string.SpeechRecognitionUsername);
        String sstPass = instance.getString(R.string.SpeechRecognitionPassword);
        
        /* URL for Cloud*/
        String sstServiceURL = "https://stream.watsonplatform.net/speech-to-text/api";
        
        /* Initialize all other speech-to-text components (flags, library objects, etc.) */
        isInRecording = false;
        speechToTextUser = new SpeechToText();
        speechToTextUser.setUsernameAndPassword(sstUsername, sstPass);
        speechToTextUser.setEndPoint(sstServiceURL);

        speechToTextParty = new SpeechToText();
        speechToTextParty.setUsernameAndPassword(sstUsername, sstPass);
        speechToTextParty.setEndPoint(sstServiceURL);

        calculator = new AmplitudeAverageCalculator();
    }

    /* Getter for extracting current speech converted by the IBM Speech-to-Text object */
    public synchronized Transcript speech() {
        if (isMessageRecognizedEmpty()) return null;
        int index = messagesRecognized.size()-1;
        Transcript transcript = messagesRecognized.remove(index);
        return transcript;
    }

    public synchronized boolean isSpeechAvailable() {
        if (messagesRecognized.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    private synchronized void addToMessagesRecognized(String temp, boolean isUser) {
        int user = 1;
        if (isUser) user = 0;
        Transcript transcript = new Transcript(temp, user);
        messagesRecognized.add(0, transcript);
        }

    private synchronized boolean isMessageRecognizedEmpty() {
        return messagesRecognized.isEmpty();
    }

    /* Method to begin the recording process*/
    public void record() {
        /* Don't start recording process if it is already running */
        if (isInRecording) return;
        
        
        //micInput =  new MicrophoneInputStream();
        micDuelInput = new MultipleMicrophoneInputStream(2);
        streamOne = new MicrophoneInputStreamReader(micDuelInput);
        streamTwo = new MicrophoneInputStreamReader(micDuelInput);
        /*
        Could be used to create two different micInputs with different AmplitudeListeners, one for user and another for party
        Therefore two different speechToTextUser services running, one for user and another for party
        One SpeechToText Service is needed for one languageSettings and another service for the second languageSettings
        Each languageSettings will have therefore different Recognize options that only happen when a certain amplitude range is reached
        */
        AmplitudeListener listener = new AmplitudeListener() {
            @Override
            public void onSample(double amplitude, double volume) {
                amp = amplitude;
                vol = volume;
            }
        };
        //micInput.setOnAmplitudeListener(listener);
        micDuelInput.setOnAmplitudeListener(listener);
        micDuelInput.startRecording();
        /* Make the recording its own separate process */
        new Thread(new Runnable() {
            @Override
            public void run() {
                MicrophoneRecognizeCallback callback = new MicrophoneRecognizeCallback(0); //Create a message retrieving object
                RecognizeOptions options = getRecognizeOptions(0); //Create an option object for speech preferences
                speechToTextUser.recognizeUsingWebSocket(streamOne, options, callback); //Initialize recognizer with defined preferences
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                MicrophoneRecognizeCallback callback = new MicrophoneRecognizeCallback(1); //Create a message retrieving object
                RecognizeOptions options = getRecognizeOptions(1); //Create an option object for speech preferences
                speechToTextParty.recognizeUsingWebSocket(streamTwo, options, callback); //Initialize recognizer with defined preferences
            }
        }).start();
        isInRecording = true;
    }

    /* Method to end the speech recognition process*/
    public void end() throws IOException {
        /* Don't continue if process is not already running */
        if (!isInRecording) return;
        
        //micInput.close();
        micDuelInput.close();
        isInRecording = false;

    }
    
    /* Retrieve preferences for the speech-to-text process*/
    private RecognizeOptions getRecognizeOptions(int user) {
        /* Create and initialize an options builder for setting up speech-to-text preferences */
        RecognizeOptions.Builder build = new RecognizeOptions.Builder();
        build.continuous(true);
        //build.contentType(ContentType.OPUS.toString());
        build.contentType(ContentType.RAW.toString());
        
        /* Set the languageSettings for speech-to-text */
        String languageModel;
        if (user == 0) {
            languageModel = LanguageSettings.getMyLanguageModel();
        }
        else {
            languageModel = LanguageSettings.getResponseLanguageModel();
        }
        build.model(languageModel);
        build.interimResults(true);
        build.inactivityTimeout(2000);
        build.profanityFilter(TranslaTaSettings.isProfanityFilterActive());
        RecognizeOptions option = build.build();
        return option;
    }

    /* Inner class for extracting the converted speech from the Speech-to-text object */
    private class MicrophoneRecognizeCallback extends BaseRecognizeCallback {

        private boolean isUser = false;

        public MicrophoneRecognizeCallback(int user) {
            if (user == 0) isUser = true;
        }


        @Override
        public void onTranscription(SpeechResults speechResults) {
            /* Does not continue if the system is not recording */
            if(!isInRecording) return;
            if (isUser) {
                calculator.addAmpValue(amp);
                Log.d("amp", "Current Amp:     " + amp);
                if (calculator.getAverageAmp() > userAmplitudeLevel - (userAmplitudeLevel * .15))
                {
                    Log.d("ampAve", "Current Average:     " + calculator.getAverageAmp());
                    //streamOne.setBlockStatus(false);
                    //streamTwo.setBlockStatus(true);
                    getOnTranscript(speechResults);
                }
            }
            else {
                if (calculator.getAverageAmp() <= userAmplitudeLevel - (userAmplitudeLevel * .15)) {
                    Log.d("ampAve", "Current Average:     " + calculator.getAverageAmp());
                    //streamOne.setBlockStatus(true);
                    //streamTwo.setBlockStatus(false);
                    getOnTranscript(speechResults);
                }
            }
            //if (!isUser) getOnTranscript(speechResults);


        }

        private void getOnTranscript(SpeechResults speechResults) {
            String temp = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
            //message = temp + "\nFinal: " +  speechResults.isFinal();
            //final String mes = message;

            String mes;
            if (isUser) mes = "User Speech:   " + temp + "\nAverageAmp:   " + calculator.getAverageAmp();
            else mes = "Party Speech:   " + temp + "\nAverageAmp:   " + calculator.getAverageAmp();
            if (speechResults.getResults().get(0).isFinal()) {
                mes = mes + "\nFINAL";
                streamOne.setBlockStatus(false);
                streamTwo.setBlockStatus(false);
                addToMessagesRecognized(temp, isUser);
                mes = mes + "\nDID ENTER RESET CALC";
                calculator.resetAmpVariables();
            }


            /* Writes the text to a text field on the current activity UI */
            final String message = mes;
        }
    }
}
