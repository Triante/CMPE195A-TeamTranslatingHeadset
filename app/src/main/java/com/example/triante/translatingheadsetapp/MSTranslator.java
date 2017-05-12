package com.example.triante.translatingheadsetapp;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/**
 *  Translates text from one language to another. Based on Microsoft Translate services
 */
public class MSTranslator {

    private String inputText; //placeholder for text in languageSettings retrieved from speech-to-text
    private String outPutText; //placeholder for translated text
    private String languageFrom; //languageSettings to translate from
    private String languageTo; //languageSettings to translate to
    private MSAuthenticator authenticator; //authenticating class for getting access to Microsoft's services
    private Context instance; //context to run translator on

    /**
     * Constructor for MSTranslator
     * @param instance (context coming from the main activity)
     * @throws IOException (thrown if the authenticator fails)
     */
    public MSTranslator(Context instance) throws IOException {
        this.instance = instance;
        authenticator = new MSAuthenticator(instance);
    }

    /**
     * Translates string input from the incoming language to the outgoing language
     * @param inputText (the text to be translated
     * @param languageFrom (the incoming language)
     * @param languageTo (the outgoing language)
     * @return (if translate process completed sucessfully or not)
     * @throws IOException (thrown when there was an error either accessing permission to Microsoft's services or performing the translation)
     */
    public String translate(String inputText, String languageFrom, String languageTo) throws IOException {
        
        // Define text and languages
        this.inputText = inputText;
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        
        // Define uri and access token
        String marketUri = instance.getString(R.string.MicrosoftTranslateTranslatorURL);
        String authToken = authenticate();
        String contextUri = "?text=" + URLEncoder.encode(inputText, "UTF-8") + "&from=" + languageFrom + "&to=" + languageTo;
        String uri = marketUri + contextUri;
        
        // Establish an HTTP connection to Microsoft's server
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authToken);
        connection.setRequestMethod("GET");
        connection.connect();

        // Checks if connection was successfully created
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
            
            // Grab translated text as an XML document from the cloud
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            connection.disconnect();
            String translation = buffer.toString();
            
            // Extract translated text from the XML
            translation = translation.replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "");
            translation = translation.replace("</string>", "");
            return translation;
        }
        return "ERROR: " + connection.getResponseCode();
    }

    /**
     * Gets the existing token for the current client
     * @return (the authentication token)
     * @throws IOException (thrown when accessing the token fails)
     */
    private String authenticate() throws IOException {
        if (authenticator.isExpired()) {
            authenticator.createAuthToken();
        }
        return  authenticator.getAuthToken();
    }

}
