package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

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
    public static int headset_mode;
    public static int speaker_mode;
    private static AudioManager audioSwitch;
    private static MediaRecorder recorder;

    public Speaker (DemoActivity instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);

        audioSwitch = (AudioManager)instance.getSystemService(Context.AUDIO_SERVICE);
        recorder = new MediaRecorder();
        audioSwitch.setSpeakerphoneOn(false);
        speaker_mode = audioSwitch.getMode();
        audioSwitch.setBluetoothScoOn(true);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

    }

    /* Method used to find which device should perform playback (Not yet implemented)*/
    public void playback(String speech, int user) {


        if (user == 1)
        {
            audioSwitch.startBluetoothSco();
        }


        textToSpeechConverter.synthesize(speech, languageTo, audioSwitch);
        System.out.println(audioSwitch.getMode() + " " + AudioManager.MODE_NORMAL);

    }

    /* Method used to perform playback to a device*/
    public void synthesizeSpeech(String speech, int user) {
        if (user == 0) {
            languageTo = LanguageSettings.getResponseLanguageVoice();
        }
        else {
            languageTo = LanguageSettings.getMyLanguageVoice();
        }

        playback(speech, user);
    }
}
