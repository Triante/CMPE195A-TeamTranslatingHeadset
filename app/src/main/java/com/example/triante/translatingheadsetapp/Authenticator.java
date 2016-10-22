package com.example.triante.translatingheadsetapp;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

public interface Authenticator {

    public void createAuthToken();
    public boolean isExpired();
    public void getAuthToken();
}
