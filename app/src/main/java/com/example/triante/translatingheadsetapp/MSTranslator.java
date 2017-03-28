package com.example.triante.translatingheadsetapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/* Class for translating text from one language to another*/
public class MSTranslator {

    private String inputText; //placeholder for text in language retrieved from speech-to-text
    private String outPutText; //placeholder for translated text
    private String languageFrom; //language to translate from
    private String languageTo; //language to translate to
    private MSAuthenticator authenticator;
    private Context instance;

    public MSTranslator(DemoActivity instance) throws IOException {
        this.instance = instance;
        authenticator = new MSAuthenticator(instance);
    }

    /* Method that takes input text in one language and translates that text to another language */
    public String translate(String inputText, String languageFrom, String languageTo) throws IOException {
        
        /* Define text and languages*/
        this.inputText = inputText;
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        
        /* Define uri and access token*/
        String marketUri = instance.getString(R.string.MicrosoftTranslateTranslatorURL);
        String authToken = authenticate();
        String contextUri = "?text=" + URLEncoder.encode(inputText, "UTF-8") + "&from=" + languageFrom + "&to=" + languageTo;
        String uri = marketUri + contextUri;
        
        /* Establish an HTTP connection to Microsoft's server*/
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authToken);
        connection.setRequestMethod("GET");
        connection.connect();

        /* Checks if connection was successfully created*/
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
            
            /* Grab translated text as an XML document from the cloud*/
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            connection.disconnect();
            String translation = buffer.toString();
            
            /* Extract translated text from the XML*/
            translation = translation.replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "");
            translation = translation.replace("</string>", "");
            return translation;
        }
        return "ERROR: " + connection.getResponseCode();
    }

    /* Method used to get the existing token for the current client*/
    private String authenticate() throws IOException {
        if (authenticator.isExpired()) {
            authenticator.createAuthToken();
        }
        return  authenticator.getAuthToken();
    }

}
