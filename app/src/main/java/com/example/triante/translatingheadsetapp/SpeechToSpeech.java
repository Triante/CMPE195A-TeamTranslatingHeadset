package com.example.triante.translatingheadsetapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/**
 *  Serves as the main hub for the speech-to-speech process
 *  (controls speech recognition, translation, and speech synthesis models)
 */
public class SpeechToSpeech {
    private Speaker speaker; //Speaker object for managing output device playback
    private Microphone microphone; //Microphone object for managing input device data retrieval
    private MSTranslator translator; //translator object model for using base methods from Microsoft's cognitive services
    private ArrayList<Transcript> translatedTextList; //text ready to be translated
    private SpeechToSpeechTranslationRunnable r1; //Translation service in the background
    private SpeechToSpeechSynthesizeSpeechRunnable r2; //Speech Synthesis service in the background
    private MainActivity.ChatHistoryAppender appender; //chat history from the main activity

    /**
     * Constructor for SpeechToSpeech
     * @param instance (context from the main activity)
     * @param appender (chat history from the main activity)
     */
    public SpeechToSpeech (AppCompatActivity instance, MainActivity.ChatHistoryAppender appender) {
        speaker = new Speaker(instance);
        microphone = new Microphone(instance);
        try {
            translator = new MSTranslator(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        translatedTextList = new ArrayList<>();
        this.appender = appender;
    }

    /**
     * Delegated task for translating text from one language to another
     * @param transcript (message to translate)
     */
    private void translate (final Transcript transcript) {
        new TranslateTextTask().execute(transcript);
    }

    /**
     * Delegated task to take translated text and play it back on a output device
     * @param transcript (message to play back)
     */
    private void synthesizeSpeech (final Transcript transcript) {
        speaker.synthesizeSpeech(transcript.getSpeech(), transcript.getUser());
    }

    /**
     * Delegate task to start the speech-to-text process
     * @return (speech-to-text flag for identifying whether the process failed or succeeded)
     */
    public boolean beginListening () {
        r1 = new SpeechToSpeechTranslationRunnable();
        r2 = new SpeechToSpeechSynthesizeSpeechRunnable();
        microphone.convertSpeechToText();
        new Thread(r1).start();
        new Thread(r2).start();
        return true;
    }

    /**
     * Delegated method to stop recording speech
     * @return (the stop recording process succeeds)
     * @throws IOException (catches errors when trying to stop the recording process)
     */
    public boolean stopListening () throws IOException {
        microphone.end();
        r1.stop();
        r2.stop();
        return true;
    }

    /**
     * Adds translated text to list queue
     * @param transcript (translated message in a Transcript wrapper)
     */
    private synchronized void addToTranslatedList(Transcript transcript) {
        translatedTextList.add(0, transcript);
    }

    /**
     * Retrieve translated text from the queue
     * @return (first translated message in queue)
     */
    private synchronized Transcript retrieveTranscriptFromTranslatedList() {
        int index = translatedTextList.size() - 1;
        return translatedTextList.remove(index);
    }

    /**
     * Checks if the translated text queue is empty
     * @return (true if it is empty, else otherwise)
     */
    private synchronized boolean isTranslatedTextListEmpty() {
        return translatedTextList.isEmpty();
    }

    /**
     * Inner class for handling the speech-to-text tasks separate from the UI thread
     */
    private class SpeechToSpeechTranslationRunnable implements Runnable {
        private boolean isGoing = true; //if translation is currently running

        @Override
        public void run() {
            while (isGoing) {
                //Perform text translation process
                Transcript transcript = microphone.retrieveConvertedTranscript();
                if (transcript != null)  {
                    translate(transcript);
                }
            }
        }

        /**
         * Stop the translation process
         */
        public void stop() {
            isGoing = false;
        }
    }

    /**
     * Inner class for running the speech playback from textual input
     */
    private class SpeechToSpeechSynthesizeSpeechRunnable implements Runnable {
        private boolean isGoing = true; //if playback process is currently running

        @Override
        public void run() {
            //Runs through playback process
            while (isGoing) {
                if (!isTranslatedTextListEmpty()) {
                    Transcript transcript = retrieveTranscriptFromTranslatedList();
                    synthesizeSpeech(transcript);
                }
            }
        }

        /**
         * Stops the playback process
         */
        public void stop() {
            isGoing = false;
        }
    }

    /**
     * Inner class for handing the text-to-speech tasks separate from the UI thread
     */
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

    /**
     * Inner class for translating a given text and then storing it in the complete list
     */
    private class TranslateTextTask extends AsyncTask<Transcript, Void, Transcript> {

        @Override
        protected Transcript doInBackground(Transcript... params) {
            String message = "";
            try {
                //Perform translation process on user's speech
                if (params[0].getUser() == 0) {
                    message = translator.translate(params[0].getSpeech(), LanguageSettings.getMyLanguageCode(), LanguageSettings.getResponseLanguageCode());
                    appender.onAddUserText("User (Spoken):  " + params[0].getSpeech());
                    try{
                        Thread.sleep(100);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    appender.onAddUserText("User (Translated):  " + message);
                }
                //Perform translation process on other party's speech
                else {
                    message = translator.translate(params[0].getSpeech(), LanguageSettings.getResponseLanguageCode(), LanguageSettings.getMyLanguageCode());
                    appender.onAddPartyText("Party (Spoken):  " + params[0].getSpeech());
                    try{
                        Thread.sleep(100);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    appender.onAddPartyText("Party (Translated):  " + message);
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
