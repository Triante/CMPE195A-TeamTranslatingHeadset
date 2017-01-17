package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class responsible for taking all the input from the microphone and storing it for use in the IBMSpeechToText class */
public class Microphone {
    private IBMSpeechToText speechToTextConverter; //Speech-to-text object to use the base methods
    private BluetoothDevice microphone; //Bluetooth connection details for the microphone
    private String languageFrom; //Language to recognize
    private String languageTo; //Language to translate to

    public Microphone (MainActivity instance) {
        speechToTextConverter = new IBMSpeechToText(instance);
    }

    /* Method used to start recording speech */
    public void convertSpeechToText() {
        speechToTextConverter.record();
        //return speechToTextConverter.speech();
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

    /* Method to let system know if a microphone source has been found*/
    public boolean identifySource() {
        return true;
    }
}
