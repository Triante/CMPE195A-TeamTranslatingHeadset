package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

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

    public void end() {
        speechToTextConverter.end();
    }

    public boolean identifySource() {
        return true;
    }
}
