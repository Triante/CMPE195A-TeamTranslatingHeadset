package com.example.triante.translatingheadsetapp;

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

/* Main class for converting speech input into text based on a user-specified language input */
public class IBMSpeechToText {
    private MainActivity instance; //instance of the main activity to send information from this class to the activity UI
    private String message = ""; //placeholder for the speech being converted
    private ArrayList<String> messagesRecognized;
    private double amplitude; //placeholder for the peak amplitude of the speech input

    private SpeechToText speechToText; //IBM-specific speech-to-text object
    private MicrophoneInputStream micInput; //Input stream for getting the speech input from microphone
    private boolean isInRecording; //flag for use to check if system is currently in recording mode
    double amp = 0;
    double vol = 0;

    public IBMSpeechToText(MainActivity instance)
    {
        this.instance = instance;
        messagesRecognized = new ArrayList<>();

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
    public synchronized Transcript speech()
    {
        if (isMessageRecognizedEmpty()) return null;
        int index = messagesRecognized.size()-1;
        message = messagesRecognized.remove(index);
        Transcript transcript = new Transcript(message, 0);
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

    private synchronized void addToMessagesRecognized(String temp) {
        messagesRecognized.add(0, temp);
    }

    private synchronized boolean isMessageRecognizedEmpty() {
        return messagesRecognized.isEmpty();
    }

    /* Method to begin the recording process*/
    public void record()
    {
        /* Don't start recording process if it is already running */
        if (isInRecording) return;
        
        
        micInput =  new MicrophoneInputStream();
        /*
        Could be used to create two different micInputs with different AmplitudeListeners, one for user and another for party
        Therefore two different speechToText services running, one for user and another for party
        One SpeechToText Service is needed for one language and another service for the second language
        Each language will have therefore different Recognize options that only happen when a certain amplitude range is reached
        */
        AmplitudeListener listener = new AmplitudeListener() {
            @Override
            public void onSample(double amplitude, double volume) {
                amp = amplitude;
                vol = volume;
            }
        };
        micInput.setOnAmplitudeListener(listener);
        
        /* Make the recording its own separate process */
        new Thread(new Runnable() {
            @Override
            public void run() {
                MicrophoneRecognizeCallback callback = new MicrophoneRecognizeCallback(); //Create a message retrieving object
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
        //build.contentType(ContentType.OPUS.toString());
        build.contentType(ContentType.RAW.toString());
        
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

            String temp = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
            //message = temp + "\nFinal: " +  speechResults.isFinal();
            //final String mes = message;

            final String mes;
            if (speechResults.getResults().get(0).isFinal()) {
                addToMessagesRecognized(temp);
                mes = temp + "\nAve Volume: " + getAveVol(vol)
                        + "\nMax Volume: " + max2
                        + "\nAve Amplitude: " + getAveAmp(amp)
                        + "\nMax Amplitude: " + max;
                max = 0;
                max2 = 0;
                current = 0;
                count = 0;
                ave2 = 0;
                c2 = 0;
            }
            else {
                getAveAmp(amp);
                getAveVol(vol);
                mes = temp + "\nCurrent Volume: " + vol
                        + "\nMax Volume: " + max2
                        + "\nCurrent Amplitude: " + amp
                        + "\nMax Amplitude: " + max;
            }


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

    //average amplitude at close to mouth > 1.0E7
    double current = 0;
    int count = 0;
    double max = 0;
    private double getAveAmp(double a) {
        count++;
        current = current + a;
        double aveCurrent = (current + a) / count;
        if (current > max) max = current;
        return aveCurrent;
    }

    double ave2 = 0;
    double c2;
    double max2 = 0;
    private double getAveVol(double v) {
        c2++;
        ave2 = ave2 + v;
        double ave = (ave2 + v) / c2;
        if (ave2 > max2) max2 = ave2;
        return ave;
    }


}
