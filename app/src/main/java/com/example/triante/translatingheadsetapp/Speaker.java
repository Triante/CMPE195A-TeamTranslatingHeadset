package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class Speaker {
    private double ampLength;
    private IBMTextToSpeech textToSpeechConverter;
    private BluetoothDevice earpiece;
    private BluetoothDevice outSpeaker;
    private String languageFrom;
    private String languageTo;

    public Speaker (MainActivity instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);
    }

    public void playback() {

    }

    public void synthesizeSpeech(String speech) {
        textToSpeechConverter.synthesize(speech, languageTo);
    }
}
