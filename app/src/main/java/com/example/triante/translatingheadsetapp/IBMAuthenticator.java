package com.example.triante.translatingheadsetapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public class IBMAuthenticator implements Authenticator {

    public IBMAuthenticator() throws IOException {

    }

    @Override
    public void createAuthToken() {

    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String getAuthToken() {
        return  "";
    }
}
