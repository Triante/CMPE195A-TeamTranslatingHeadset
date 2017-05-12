package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.media.AudioManager;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * Created by Luis Otero on 04/23/2017.
 */

/**
 *  Handles all the playback and what device should be doing playback
 */
public class Speaker {
    private IBMTextToSpeech textToSpeechConverter; //Text-to-speech object to use base methods from IBM's Text-to-Speech service
    private Voice languageTo; //language to use for playback
    private static AudioManager audioSwitch; //manages the Bluetooth device audio routing

    /**
     * Constructor for Speaker
     * @param instance (context from main activity)
     */
    public Speaker (Context instance) {
        textToSpeechConverter = new IBMTextToSpeech(instance);

        //extract audio service from mobile device
        audioSwitch = (AudioManager)instance.getSystemService(Context.AUDIO_SERVICE);

        //Don't use speakerphone for playing audio
        audioSwitch.setSpeakerphoneOn(false);
        audioSwitch.setBluetoothScoOn(true);
    }

    /**
     *  Finds which device should perform playback (headset or the speaker) depending on which user spoke.
     *  1. User spoke, speaker does playback
     *  2. Other party spoke, headset does playback
     * @param speech (message to playback on the device)
     * @param user (which of the users spoke)
     * @return
     */
    private String playback(String speech, int user) {
        String playbacktype;

        //Other party spoke
        if (user == 1)
        {
            audioSwitch.startBluetoothSco();
            playbacktype = "HEADSET";
        }
        //User spoke
        else
        {
            playbacktype = "SPEAKER";
        }


        textToSpeechConverter.synthesize(speech, languageTo, audioSwitch);

        return playbacktype;

    }

    /**
     * Performs playback to one of the connected devices with the specified language from the language settings
     * @param speech (message to be played on the selected device)
     * @param user (which of the users spoke)
     * @return (string value to represent whether the playback succeeded or not)
     */
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
