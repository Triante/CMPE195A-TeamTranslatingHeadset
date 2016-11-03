package com.example.triante.translatingheadsetapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.Authenticator;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class MSTranslator {

    private String inputText;
    private String outPutText;
    private String languageFrom;
    private String languageTo;
    private MSAuthenticator authenticator;

    public MSTranslator() throws IOException {
        authenticator = new MSAuthenticator();
    }

    public String translate(String inputText, String languageFrom, String languageTo) throws IOException {
        this.inputText = inputText;
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        String marketUri = "http://api.microsofttranslator.com/v2/Http.svc/Translate?text=";
        String contextUri = URLEncoder.encode(inputText, "UTF-8") + "&from=" + languageFrom + "&to=" + languageTo;
        String authToken = authenticate();
        String uri = marketUri + contextUri;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authToken);
        connection.setRequestMethod("GET");
        connection.connect();
        System.out.println();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            connection.disconnect();
            String translation = buffer.toString();
            translation = translation.replace("<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">", "");
            translation = translation.replace("</string>", "");
            return translation;
        }
        return "ERROR: " + connection.getResponseCode();
    }

    private String authenticate() throws IOException {
        if (authenticator.isExpired()) {
            authenticator.createAuthToken();
        }
        return  "Bearer" + " " + authenticator.getAuthToken();
    }

}
