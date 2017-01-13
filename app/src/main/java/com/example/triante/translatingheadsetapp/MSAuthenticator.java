package com.example.triante.translatingheadsetapp;

import android.content.Context;
import android.util.Xml;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

/**
 * Created by Jorge Aguiniga on 10/22/2016.
 */

/* Authenticator definition for Microsoft's Cognitive Services. Used for accessing tokens to get cloud access*/
public class MSAuthenticator implements Authenticator {

    private Context instance;

    /* Market platform URL*/
    private final String DATAMARKET_ACCESS;
    
    private AdmAccessToken token; //Access token for the cloud service

    public MSAuthenticator(MainActivity instance) throws IOException {
        this.instance = instance;
        DATAMARKET_ACCESS = instance.getString(R.string.MicrosoftTranslateTokenURL);
        token = new AdmAccessToken();
    }

    /* Creates token for the current session*/
    @Override
    public void createAuthToken() throws IOException {
        /* Client credentials. Password not given for security purposes*/
        String subscriptionKey = instance.getString(R.string.MicrosoftTranslateTokenKey);
        token = token.getAdmAccessToken(subscriptionKey);
    }

    /* Checks if the curent token has expired*/
    @Override
    public boolean isExpired() {
        return true;
    }

    /* Get the token currently being used for the session*/
    @Override
    public String getAuthToken() {
        return token.access_token;
    }

    /* Inner class for token administration (retrieval and renewal)*/
    private class AdmAccessToken {
        public String access_token; //token written in string format
        public String token_type; //type of token being used
        public String expires_in; //exact time token expires
        public String scope; //token scope and access range

        /* Gets the access token from the Microsoft server*/
        public AdmAccessToken getAdmAccessToken(String subscriptionKey) throws IOException {

            /* Begin HTTP connection to the Microsoft Cognitive Services server*/
            URL url = new URL(DATAMARKET_ACCESS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            
            /* Check if the connection was established successfully*/
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                access_token = "Bearer " + buffer.toString();
            }
            return this;
        }
    }
}
