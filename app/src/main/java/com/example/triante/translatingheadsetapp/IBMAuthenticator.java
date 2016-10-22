package com.example.triante.translatingheadsetapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMAuthenticator implements Authenticator {

    private String client, pass;
    private final String DATAMARKET_ACCESS = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    public IBMAuthenticator() throws IOException {


        URL url = new URL("https://api.datamarket.azure.com/Bing/MicrosoftTranslator/v1/Translate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(50000);
        connection.setConnectTimeout(65000);
        connection.setRequestMethod("GET");
        connection.connect();
    }

    @Override
    public void createAuthToken() {

    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void getAuthToken() {

    }
}
