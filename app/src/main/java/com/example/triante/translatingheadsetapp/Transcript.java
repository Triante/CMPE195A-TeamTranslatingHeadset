package com.example.triante.translatingheadsetapp;

/**
 * Created by Jorge Aguiniga on 1/16/2017.
 */

public class Transcript {
    private String speech;
    private int user;

    public Transcript(String speech, int user) {
        this.speech = speech;
        this.user = user;
    }

    public int getUser() {
        return user;
    }

    public String getSpeech() {
        return speech;
    }


}
