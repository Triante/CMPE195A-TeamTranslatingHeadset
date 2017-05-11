package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class responsible for taking all the input from the microphone and storing it for use in the IBMSpeechToText class */
public class Microphone {
    private IBMSpeechToText speechToTextConverter; //Speech-to-text object to use the base methods

    public Microphone (AppCompatActivity instance) {
        speechToTextConverter = new IBMSpeechToText(instance);
    }

    /* Method used to start recording speech */
    public void convertSpeechToText() {
        speechToTextConverter.record();
    }

    public Transcript retrieveConvertedTranscript() {
        if (speechToTextConverter.isSpeechAvailable()) {
            return speechToTextConverter.speech();
        }
        else {
            return null;
        }
    }

    /* Method to end the speech recording process */
    public void end() throws IOException {
        speechToTextConverter.end();
    }
}
