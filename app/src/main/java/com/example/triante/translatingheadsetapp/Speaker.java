package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
    private int headset_mode;
    private int speaker_mode;
    private AudioManager audioSwitch;

    public Speaker (DemoActivity instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);
        audioSwitch = (AudioManager)instance.getSystemService(Context.AUDIO_SERVICE);
        speaker_mode = audioSwitch.getMode();
        headset_mode = audioSwitch.MODE_IN_COMMUNICATION;

        audioSwitch.setMode(speaker_mode);
        audioSwitch.startBluetoothSco();
    }

    /* Method used to find which device should perform playback (Not yet implemented)*/
    public void playback(String speech, int user) {
        audioSwitch.setMicrophoneMute(true);
        if (user == 1)
        {
            audioSwitch.stopBluetoothSco();
            audioSwitch.setMode(headset_mode);
        }
        else {
            audioSwitch.setMode(speaker_mode);
            audioSwitch.startBluetoothSco();
        }
        textToSpeechConverter.synthesize(speech, languageTo, audioSwitch, headset_mode);
    }

    /* Method used to perform playback to a device*/
    public void synthesizeSpeech(String speech, int user) {
        if (user == 0) {
            languageTo = Language.getResponseLanguageVoice();
        }
        else {
            languageTo = Language.getMyLanguageVoice();
        }

        playback(speech, user);
        //textToSpeechConverter.synthesize(speech, languageTo);
    }
}
