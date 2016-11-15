package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;

import java.io.IOException;

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
        try {
            translator = new MSTranslator();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void stopListening () throws IOException {
        microphone.end();
    }

    private class SpeechToTextRunnable implements Runnable {

        @Override
        public void run() {

        }

        private void record() {

        }

        private void stop() {

        }
    }

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
