package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */


public class IBMSpeechToText  {
    private MainActivity instance;
    private String message;
    private double amplitude;

    private SpeechToText speechToText;
    private MicrophoneInputStream micInput;
    private boolean isInRecording;

    public IBMSpeechToText(MainActivity instance)
    {
        this.instance = instance;

        //Instantiation of SpeechToText
        String sstUsername = instance.getString(R.string.SpeechRecognitionUsername);
        String sstPass = instance.getString(R.string.SpeechRecognitionPassword);
        //String sstServiceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
        String sstServiceURL = "https://stream.watsonplatform.net/speech-to-text/api";
        isInRecording = false;
        speechToText = new SpeechToText();
        speechToText.setUsernameAndPassword(sstUsername, sstPass);
        speechToText.setEndPoint(sstServiceURL);
    }

    public String speech()
    {
        //end();
        return message;
    }

    public void record()
    {
        if (isInRecording) return;
        micInput =  new MicrophoneInputStream(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MicrophoneRecognizeCallback callback = new MicrophoneRecognizeCallback();
                RecognizeOptions options = getRecognizeOptions();
                speechToText.recognizeUsingWebSocket(micInput, options, callback);
            }
        }).start();
        isInRecording = true;
    }

    public void end() throws IOException {
        if (!isInRecording) return;
        micInput.close();
        isInRecording = false;

    }

    private RecognizeOptions getRecognizeOptions() {
        RecognizeOptions.Builder build = new RecognizeOptions.Builder();
        build.continuous(true);
        build.contentType(ContentType.OPUS.toString());
        String myLanguageModel = Language.getMyLanguageModel();
        build.model(myLanguageModel);
        build.interimResults(true);
        build.inactivityTimeout(2000);
        RecognizeOptions option = build.build();
        return option;
    }

    private class MicrophoneRecognizeCallback extends BaseRecognizeCallback {
        @Override
        public void onTranscription(SpeechResults speechResults) {
            if(!isInRecording) return;
            System.out.println(speechResults);
            message = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
            final String mes = message;
            instance.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        instance.translatedTextView.setText(mes);
                    }
                });
        }
    }

//    public void onMessage(String s)
//    {
//        if (s.contains("transcript") && isInRecording)
//        {
//            try
//            {
//                JSONObject object = new JSONObject(s);
//                JSONArray result = object.getJSONArray("results");
//                JSONObject alt = result.getJSONObject(0);
//                JSONArray alternatives = alt.getJSONArray("alternatives");
//                message = alternatives.getJSONObject(0).getString("transcript");
//                final String mes = message;
//                instance.runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run() {
//                        instance.translatedTextView.setText(mes);
//                    }
//                });
//            } catch (org.json.JSONException e)
//            {
//                e.getMessage();
//            }
//        }
//    }

}
