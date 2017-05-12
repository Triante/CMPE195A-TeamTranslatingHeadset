package com.example.triante.translatingheadsetapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jorge Aguiniga on 10/22/2016.
 * Authenticator definition for Microsoft's Cognitive Services. Used for accessing tokens to get cloud access
 */
public class MSAuthenticator implements Authenticator {

    private Context instance;

    /* Market platform URL*/
    private final String DATAMARKET_ACCESS;
    
    private AdmAccessToken token; //Access token for the cloud service

    /**
     * Constructor for MSAuthenticator, initializes with an expired token.
     * @param instance context from the main activity to access TranslaTa assets
     * @throws IOException
     */
    public MSAuthenticator(Context instance) throws IOException {
        this.instance = instance;
        DATAMARKET_ACCESS = instance.getString(R.string.MicrosoftTranslateTokenURL);
        token = new AdmAccessToken();
    }

    /**
     *  Creates a new authentication token which is valid for 9 minutes
     */
    @Override
    public void createAuthToken() throws IOException {
        /* Client credentials. Password not given for security purposes*/
        String subscriptionKey = instance.getString(R.string.MicrosoftTranslateTokenKey);
        token = token.getAdmAccessToken(subscriptionKey);
    }

    /**
     * Checks if the current token has expired
     * @return true if the token is expired, false otherwise
     */
    @Override
    public boolean isExpired() {
        if (token.expires_in < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    /**
     * Get the token currently being used for the session. Call isExpired beforehand to verify that the token is still valid.
     * @return the authentication token
     */
    @Override
    public String getAuthToken() {
        return token.access_token;
    }

    /**
     *  Inner class for to create and hold the authentication token to use Microsoft's services.
     */
    private class AdmAccessToken {
        private final long NINE_MINUTES = 540000; // time before until token expires
        public String access_token = ""; //token written in string format
        public String token_type; //type of token being used
        public long expires_in = 0; //exact time token expires
        public String scope; //token scope and access range

        /**
         * Gets the access token from the Microsoft server
         * @param subscriptionKey the server key used for authenticating a Azure account
         * @return An instance of AdmAccessToken with a valid token
         * @throws IOException
         */
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
                expires_in = System.currentTimeMillis() + NINE_MINUTES;
            }
            return this;
        }
    }
}
