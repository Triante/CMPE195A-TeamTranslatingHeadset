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
 *
 * Class for converting speech input into text based on a user-specified languageSettings input
 * using IBM's Speech to Text services hosted on Bluemix.
 */

public class IBMSpeechToText {
    private Context instance; //instance of the main activity to send information from this class to the activity UI
    private ArrayList<Transcript> messagesRecognized;

    private SpeechToText speechToTextUser; //IBM-specific speech-to-text object
    private SpeechToText speechToTextParty;
    private MultipleMicrophoneInputStream micDuelInput;
    private MicrophoneInputStreamReader streamOne;
    private MicrophoneInputStreamReader streamTwo;
    private AmplitudeAverageCalculator calculator;
    private boolean isInRecording; //flag for use to check if system is currently in recording mode
    private static boolean userDomLock = false;
    double amp = 0;
    double vol = 0;

    /**
     * IBM SpeechToText constructor
     * @param instance context from the main activity to access TranslaTa assets
     */
    public IBMSpeechToText(Context instance) {
        this.instance = instance;
        messagesRecognized = new ArrayList<>();

        /* Credential initialization for getting access to IBM Watson cloud service */
        String sstUsername = instance.getString(R.string.SpeechRecognitionUsername);
        String sstPass = instance.getString(R.string.SpeechRecognitionPassword);
        
        /* URL for Cloud*/
        String sstServiceURL = instance.getString(R.string.SpeechRecognitionURL);
        
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

    /**
     * Getter for retrieving the oldest speech transcript converted by the IBM Speech-to-Text object
     * @return the next speech transcript on te list, null if empty
     */
    public synchronized Transcript speech() {
        if (isMessageRecognizedEmpty()) return null;
        int index = messagesRecognized.size()-1;
        Transcript transcript = messagesRecognized.remove(index);
        return transcript;
    }

    /**
     * Checks if there are any recognized speech transcripts to be retrieved
     * @return true if recognized speech list has content, false otherwise
     */
    public synchronized boolean isSpeechAvailable() {
        if (messagesRecognized.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Helper method to add recognized speech text and it's user to a ranscript object and then storing it
     * in the recognized transcript list
     * @param temp the recognized string
     * @param isUser the user who spoke
     */
    private synchronized void addToMessagesRecognized(String temp, boolean isUser) {
        int user = 1;
        if (isUser) user = 0;
        Transcript transcript = new Transcript(temp, user);
        messagesRecognized.add(0, transcript);
        }

    /**
     * Helper function to check if the recognized transcript list is empty
     * @return true if list is empty, false otherwise
     */
    private synchronized boolean isMessageRecognizedEmpty() {
        return messagesRecognized.isEmpty();
    }

    /**
     *  Begins the speech recognition process for both the user and the party.
     */
    public void record() {
        /* Don't start recording process if it is already running */
        if (isInRecording) return;

        micDuelInput = new MultipleMicrophoneInputStream(2);
        streamOne = new MicrophoneInputStreamReader(micDuelInput);
        streamTwo = new MicrophoneInputStreamReader(micDuelInput);

        AmplitudeListener listener = new AmplitudeListener() {
            @Override
            public void onSample(double amplitude, double volume) {
                amp = amplitude;
                vol = volume;
            }
        };

        micDuelInput.setOnAmplitudeListener(listener);
        micDuelInput.startRecording();

        /*
        Two different speechToTextUser services running, one for user and another for party
        One SpeechToText Service is needed for one languageSettings and another service for the second languageSettings
        Each languageSettings will have therefore different Recognize options that only happen when a certain amplitude range is reached
        Make the recording its own separate process
        */
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

    /**
     *  Method to end the speech recognition process
     */
    public void end() throws IOException {
        /* Don't continue if process is not already running */
        if (!isInRecording) return;

        micDuelInput.close();
        isInRecording = false;
    }

    /**
     * Creates the preferences to be use during a recognize call in IBM's SpeechToText service using values retrieved from TranslaTa's language settings
     * @param user 0 to build preferences for the user, 1 for the party
     * @return the preferences for IBM's SpeechToText service
     */
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

    /**
     *  Inner class for extracting the converted speech from IBM's Speech-to-text object and
     *  storing it into an ArrayList containing all the transcripts
     */
    private class MicrophoneRecognizeCallback extends BaseRecognizeCallback {

        private boolean isUser = false;

        /**
         * Constructor for MicrophoneRecognizeCallback
         * @param user 0 if the speech transcript should be tied to the user, 1 if tied to the party
         */
        public MicrophoneRecognizeCallback(int user) {
            if (user == 0) isUser = true;
        }

        /**
         * Method to handle the transcript retrieved from IBM's SpeechToText service.
         * Based on the amplitude of the speech, determines whether the transcript was the user's or party's,
         * then adds the transcript to a list of already recognized speech.
         * @param speechResults the results from IBM's SpeechToText class
         */
        @Override
        public void onTranscription(SpeechResults speechResults) {
            /* Does not continue if the system is not recording */
            if(!isInRecording) return;
            int userAmplitudeLevel = TranslaTaSettings.getThresholdAmplitude();
            if (isUser) {
                double a = calculator.addAmpValue(amp);
                Log.d("amp", "Added Amp:     " + a);
                if (calculator.getAverageAmp() > userAmplitudeLevel - (userAmplitudeLevel * .15))
                {
                    Log.d("ampAve", "Current Average:     " + calculator.getAverageAmp());
                    userDomLock = true;
                    getOnTranscript(speechResults);
                }
                else {
                    userDomLock = false;
                }
            }
            else {
                if (calculator.getAverageAmp() <= userAmplitudeLevel - (userAmplitudeLevel * .15) && calculator.countAboveOne() && !userDomLock) {
                    Log.d("ampAve", "Current Average:     " + calculator.getAverageAmp());
                    getOnTranscript(speechResults);
                }
            }


        }

        /**
         * Helper method to add the speech trasncript to the recognized list. Only adds the transcript if
         * the speech result is final. Method also resets the calculator back to 0.
         * @param speechResults the speech results from IBM's SpeechToText service
         */
        private void getOnTranscript(SpeechResults speechResults) {
            String temp = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
            if (speechResults.getResults().get(0).isFinal()) {
                streamOne.setBlockStatus(false);
                streamTwo.setBlockStatus(false);
                addToMessagesRecognized(temp, isUser);
                int userAmplitudeLevel = TranslaTaSettings.getThresholdAmplitude();
                Log.d("finalAmpAve", "User Saved Average:     " + userAmplitudeLevel);
                Log.d("finalAmpAve", "Acceptable Range for Average:     " + (userAmplitudeLevel - (userAmplitudeLevel * .15)));
                Log.d("finalAmpAve", "Final Amplitude Average:     " + calculator.getAverageAmp());
                Log.d("finalAmpAve", "Final Amplitude Mean:     " + calculator.getMedium());
                Log.d("finalAmpAve", "Final Amplitude Count:     " + calculator.getCount());
                Log.d("finalAmpAve", "Final Amplitude Mode:     " + calculator.getMode());
                Log.d("finalAmpAve", "Final Amplitude Mode Next:     " + calculator.getModeNext());
                Log.d("finalAmpAve", "Final Amplitude Values:     " + calculator.print());
                calculator.resetAmpVariables();
                userDomLock = false;
            }

        }
    }


}
