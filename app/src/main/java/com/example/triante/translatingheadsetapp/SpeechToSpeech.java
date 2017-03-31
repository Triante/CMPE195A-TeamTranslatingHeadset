package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;

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
    private String languageFrom; //languageSettings to recognize/translate from
    private String languageTo; //languageSettings to translate to/playback
    private ArrayList<Transcript> translatedTextList; //text ready to be translated
    private Executor executor;
    private SpeechToSpeechTranslationRunnable r1;
    private SpeechToSpeechSynthesizeSpeechRunnable r2;

    public SpeechToSpeech (DemoActivity instance) {
        /* Initialize all objects*/
        speaker = new Speaker(instance);
        microphone = new Microphone(instance);
        try {
            translator = new MSTranslator(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        speech = "";
        translatedTextList = new ArrayList<>();
    }

    /* Delegated task for translating text from one languageSettings to another*/
    private void translate (final Transcript transcript) {
        new TranslateTextTask().execute(transcript);
    }

    /* Delegated task to take translated speech and play it back to whoever is waiting for a response*/
    private void synthesizeSpeech (final Transcript transcript) {
        speaker.synthesizeSpeech(transcript.getSpeech(), transcript.getUser());
    }

    /* Delegated task to start the speech recognition process*/
    public boolean beginListening () {
        r1 = new SpeechToSpeechTranslationRunnable();
        r2 = new SpeechToSpeechSynthesizeSpeechRunnable();
        microphone.convertSpeechToText();
        new Thread(r1).start();
        new Thread(r2).start();
        return true;
    }

    /* Delegated method to stop recording speech*/
    public void stopListening () throws IOException {
        microphone.end();
        r1.stop();
        r2.stop();

    }

    private synchronized void addToTranslatedList(Transcript transcript) {
        translatedTextList.add(0, transcript);
    }

    private synchronized Transcript retrieveTranscriptFromTranslatedList() {
        int index = translatedTextList.size() - 1;
        return translatedTextList.remove(index);
    }

    private synchronized boolean isTranslatedTextListEmpty() {
        return translatedTextList.isEmpty();
    }

    /* Inner class for handing the speech-to-text tasks separate from the UI thread (not yet working)*/
    private class SpeechToSpeechTranslationRunnable implements Runnable {
        private boolean isGoing = true;

        @Override
        public void run() {
            while (isGoing) {
                Transcript transcript = microphone.retrieveConvertedTranscript();
                if (transcript != null)  {
                    translate(transcript);
                }
            }
        }

        public void stop() {
            isGoing = false;
        }
    }

    private class SpeechToSpeechSynthesizeSpeechRunnable implements Runnable {
        private boolean isGoing = true;

        @Override
        public void run() {
            while (isGoing) {
                if (!isTranslatedTextListEmpty()) {
                    Transcript transcript = retrieveTranscriptFromTranslatedList();
                    synthesizeSpeech(transcript);
                }
            }
        }

        public void stop() {
            isGoing = false;
        }
    }

     /* Inner class for handing the text-to-speech tasks separate from the UI thread (not yet working)*/
    private class TextToTranslatedSpeechRunnable extends AsyncTask<Transcript, Void, String> {

        @Override
        protected String doInBackground(Transcript... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /* Inner class for translating a given text and then storing it in the complete list*/
    private class TranslateTextTask extends AsyncTask<Transcript, Void, Transcript> {

        @Override
        protected Transcript doInBackground(Transcript... params) {
            String message = "";
            try {
                if (params[0].getUser() == 0) {
                    message = translator.translate(params[0].getSpeech(), LanguageSettings.getMyLanguageCode(), LanguageSettings.getResponseLanguageCode());
                }
                else {
                    message = translator.translate(params[0].getSpeech(), LanguageSettings.getResponseLanguageCode(), LanguageSettings.getMyLanguageCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Transcript transcript = new Transcript(message, params[0].getUser());
            return transcript;
        }

        @Override
        protected void onPostExecute(Transcript mes) {
            addToTranslatedList(mes);
        }
    }
}
