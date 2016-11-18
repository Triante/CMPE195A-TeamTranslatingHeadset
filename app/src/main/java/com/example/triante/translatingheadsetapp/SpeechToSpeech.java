package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class that serves as the main hub for the speech-to-speech process 
    (controls speech recognition, translation, and speech synthesis models)*/
public class SpeechToSpeech {
    private Speaker speaker; //Speaker object for managing output device playback
    private Microphone microphone; //Microphone object for managing input device data retrieval
    private MSTranslator translator; //translator object model for using base methods from Microsoft's cognitive services
    private String speech; //speech gathered from microphone
    private String languageFrom; //language to recognize/translate from
    private String languageTo; //language to translate to/playback

    public SpeechToSpeech (MainActivity instance) {
        
        /* Initialize all objects*/
        speaker = new Speaker(instance);
        microphone = new Microphone(instance);
        try {
            translator = new MSTranslator();
        } catch (IOException e) {
            e.printStackTrace();
        }
        speech = "";
    }

    /* Delegated task for translating text from one language to another*/
    private String translate (String languageFrom, String languageTo) {
        return ""; //currently inactive (translator does not work seamlessly with other objects yet)
    }

    /* Delegated task to take translated speech and play it back to whoever is waiting for a response*/
    private void synthesizeSpeech (String speech) {
        speaker.synthesizeSpeech(speech);
    }

    /* Delegated task to start the speech recognition process*/
    public boolean beginListening () {
        speech = microphone.convertSpeechToText();
        synthesizeSpeech(speech);
        return true;
    }

    /* Delegated method to stop recording speech*/
    public void stopListening () throws IOException {
        microphone.end();
    }

    /* Inner class for handing the speech-to-text tasks separate from the UI thread (not yet working)*/
    private class SpeechToTextRunnable implements Runnable {

        @Override
        public void run() {

        }

        private void record() {

        }

        private void stop() {

        }
    }

     /* Inner class for handing the text-to-speech tasks separate from the UI thread (not yet working)*/
    private class TextToTranslatedSpeechRunnable extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
