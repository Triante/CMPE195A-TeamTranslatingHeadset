package com.example.triante.translatingheadsetapp;

/**
 * Created by Jorge Aguiniga on 1/16/2017.
 */

/**
 * Wrapper for speech input based on containing a message and the user who spoke it
 */
public class Transcript {
    private String speech; //speech input from one of the users
    private int user; //the user that spoke

    /**
     * Construct for Transcript
     * @param speech (speech input from one of the users)
     * @param user (the user that just spoke)
     */
    public Transcript(String speech, int user) {
        this.speech = speech;
        this.user = user;
    }

    /**
     * Accessor for getting the user that just spoke
     * @return (the user that spoke)
     */
    public int getUser() {
        return user;
    }

    /**
     * Accessor for getting the speech input
     * @return (the speech input just retrieved from the microphone)
     */
    public String getSpeech() {
        return speech;
    }


}
