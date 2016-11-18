package com.example.triante.translatingheadsetapp;

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

    /* Client credentials. Password not given for security purposes*/
    private String client = "cmpe195a-translating-headset", pass = ""; 
    
    /* Market platform URL*/
    private final String DATAMARKET_ACCESS = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    
    private AdmAccessToken token; //Access token for the cloud service

    public MSAuthenticator() throws IOException {
        token = new AdmAccessToken();
    }

    /* Creates token for the current session*/
    @Override
    public void createAuthToken() throws IOException {
        token = token.getAdmAccessToken(client, pass);
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
        public AdmAccessToken getAdmAccessToken(String clientID, String clientSecret) throws IOException {

            AdmAccessToken accessToken = null;
            
            /* Parameters used for passing to the server to check whether client is a valid client*/
            String params = "grant_type=client_credentials&scope=http://api.microsofttranslator.com"
                    + "&client_id=" + URLEncoder.encode(clientID, "UTF-8")
                    + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") ;
            
            /* Begin HTTP connection to the Microsoft Cognitive Services server*/
            URL url = new URL(DATAMARKET_ACCESS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=" + "UTF-8");
            connection.setRequestProperty("Accept-Charset","UTF-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            
            /* Write parameters to the output stream*/
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(params);
            writer.flush();
            
            /* Check if the connection was established successfully*/
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                
                /* JSON deserialize the access token*/
                Gson gson = new Gson();
                accessToken = gson.fromJson(buffer.toString(), AdmAccessToken.class);
            }
            return accessToken;
        }
    }
}
