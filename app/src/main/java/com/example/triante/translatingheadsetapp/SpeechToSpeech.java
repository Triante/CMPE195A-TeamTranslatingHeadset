package com.example.triante.translatingheadsetapp;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class SpeechToSpeech {
    private Speaker speaker;
    private Microphone microphone;
    private MSTranslator translator;
    private String speech;
    private String languageFrom;
    private String languageTo;

    public SpeechToSpeech (MainActivity instance) {
        speaker = new Speaker(instance);
        microphone = new Microphone(instance);
        translator = new MSTranslator();
        speech = "";
    }

    private String translate (String languageFrom, String languageTo) {
        return "";
    }

    private void synthesizeSpeech (String speech) {
        speaker.synthesizeSpeech(speech);
    }

    public boolean beginListening () {
        speech = microphone.convertSpeechToText();
        synthesizeSpeech(speech);
        return true;
    }

    public void stopListening () {
        microphone.end();
    }

}
