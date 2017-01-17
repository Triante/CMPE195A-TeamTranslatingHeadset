package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class that handles all the playback and what device should be doing playback*/
public class Speaker {
    private double ampLength; //Max amplitude value of speech from microphone (determine which party is speaking)
    private IBMTextToSpeech textToSpeechConverter; //Text-to-speech object to use base methods from IBM's Text-to-Speech service
    private BluetoothDevice earpiece; //placeholder for Bluetooth data from the headset device
    private BluetoothDevice outSpeaker; //placeholder for Bluetooth data from the external speaker
    private String languageTo; //language to use for playback

    public Speaker (MainActivity instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);
    }

    /* Method used to find which device should perform playback (Not yet implemented)*/
    public void playback() {

    }

    /* Method used to perform playback to a device*/
    public void synthesizeSpeech(String speech, int user) {
        textToSpeechConverter.synthesize(speech, "");
    }
}
