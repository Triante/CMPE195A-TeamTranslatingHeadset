package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class that handles all the playback and what device should be doing playback*/
public class Speaker {
    private double ampLength; //Max amplitude value of speech from microphone (determine which party is speaking)
    private IBMTextToSpeech textToSpeechConverter; //Text-to-speech object to use base methods from IBM's Text-to-Speech service
    private BluetoothDevice earpiece; //placeholder for Bluetooth data from the headset device
    private BluetoothDevice outSpeaker; //placeholder for Bluetooth data from the external speaker
    private Voice languageTo; //language to use for playback

    public Speaker (DemoActivity instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);
    }

    /* Method used to find which device should perform playback (Not yet implemented)*/
    public void playback() {

    }

    /* Method used to perform playback to a device*/
    public void synthesizeSpeech(String speech, int user) {
        if (user == 0) {
            languageTo = Language.getResponseLanguageVoice();
        }
        else {
            languageTo = Language.getMyLanguageVoice();
        }
        textToSpeechConverter.synthesize(speech, languageTo);
    }
}
