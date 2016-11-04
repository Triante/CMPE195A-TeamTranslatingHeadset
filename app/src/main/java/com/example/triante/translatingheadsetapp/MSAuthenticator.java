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

public class MSAuthenticator implements Authenticator {


    private String client = "cmpe195a-translating-headset", pass = "";
    private final String DATAMARKET_ACCESS = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    private AdmAccessToken token;

    public MSAuthenticator() throws IOException {
        token = new AdmAccessToken();
    }

    @Override
    public void createAuthToken() throws IOException {
        token = token.getAdmAccessToken(client, pass);
    }

    @Override
    public boolean isExpired() {
        return true;
    }

    @Override
    public String getAuthToken() {
        return token.access_token;
    }


    private class AdmAccessToken {
        public String access_token;
        public String token_type;
        public String expires_in;
        public String scope;

        public AdmAccessToken getAdmAccessToken(String clientID, String clientSecret) throws IOException {

            AdmAccessToken accessToken = null;
            String params = "grant_type=client_credentials&scope=http://api.microsofttranslator.com"
                    + "&client_id=" + URLEncoder.encode(clientID, "UTF-8")
                    + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8") ;
            URL url = new URL(DATAMARKET_ACCESS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=" + "UTF-8");
            connection.setRequestProperty("Accept-Charset","UTF-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(params);
            writer.flush();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //JSON deserialize the access token
                Gson gson = new Gson();
                accessToken = gson.fromJson(buffer.toString(), AdmAccessToken.class);
            }
            return accessToken;
        }
    }
}
