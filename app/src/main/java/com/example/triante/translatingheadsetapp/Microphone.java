package com.example.triante.translatingheadsetapp;

import android.support.v7.app.AppCompatActivity;
import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/**
 * Takes all the input from the microphone and stores it for use in the IBMSpeechToText class
 */
public class Microphone {
    private IBMSpeechToText speechToTextConverter; //Speech-to-text object to use the base methods

    /**
     * Constructor for the Microphone
     * @param instance (context for the main activity)
     */
    public Microphone (AppCompatActivity instance) {
        speechToTextConverter = new IBMSpeechToText(instance);
    }

    /**
     *  Begins the recording process for the speech-to-text instance
     */
    public void convertSpeechToText() {
        speechToTextConverter.record();
    }

    /**
     * If there is speech available to pass on to the translator, format it into a Transcript and extract it
     * @return (Speech formatted into a Transcript wrapper)
     */
    public Transcript retrieveConvertedTranscript() {
        if (speechToTextConverter.isSpeechAvailable()) {
            return speechToTextConverter.speech();
        }
        else {
            return null;
        }
    }

    /**
     *  Ends the recording process for the speech-to-text instance
     */
    public void end() throws IOException {
        speechToTextConverter.end();
    }
}
