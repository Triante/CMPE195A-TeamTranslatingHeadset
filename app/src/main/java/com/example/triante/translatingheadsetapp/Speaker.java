package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.media.AudioManager;

import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * @author by Luis Otero on 04/23/2017.
 */

/* Class that handles all the playback and what device should be doing playback*/
public class Speaker {
    private IBMTextToSpeech textToSpeechConverter; //Text-to-speech object to use base methods from IBM's Text-to-Speech service
    private Voice languageTo; //language to use for playback
    private static AudioManager audioSwitch;

    public Speaker (Context instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);

        audioSwitch = (AudioManager)instance.getSystemService(Context.AUDIO_SERVICE);
        audioSwitch.setSpeakerphoneOn(false);
        audioSwitch.setBluetoothScoOn(true);
    }

    /* Method used to find which device should perform playback (Not yet implemented)*/
    private String playback(String speech, int user) {
        String playbacktype;

        if (user == 1)
        {
            audioSwitch.startBluetoothSco();
            playbacktype = "HEADSET";
        }
        else
        {
            playbacktype = "SPEAKER";
        }


        textToSpeechConverter.synthesize(speech, languageTo, audioSwitch);

        return playbacktype;

    }

    /* Method used to perform playback to a device*/
    public String synthesizeSpeech(String speech, int user) {
        if (user == 0) {
            languageTo = LanguageSettings.getResponseLanguageVoice();
        }
        else {
            languageTo = LanguageSettings.getMyLanguageVoice();
        }

        return playback(speech, user);
    }
}
