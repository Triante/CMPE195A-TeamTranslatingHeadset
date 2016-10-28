package com.example.triante.translatingheadsetapp;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public interface Authenticator {

    public void createAuthToken() throws IOException;
    public boolean isExpired();
    public String getAuthToken();
}
