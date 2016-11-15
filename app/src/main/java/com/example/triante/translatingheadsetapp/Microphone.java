package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class Microphone {
    private IBMSpeechToText speechToTextConverter;
    private BluetoothDevice microphone;
    private String languageFrom;
    private String languageTo;

    public Microphone (MainActivity instance) {
        speechToTextConverter = new IBMSpeechToText(instance);
    }

    public String convertSpeechToText() {
        speechToTextConverter.record();
        return speechToTextConverter.speech();
    }

    public void end() throws IOException {
        speechToTextConverter.end();
    }

    public boolean identifySource() {
        return true;
    }
}
