package com.example.triante.translatingheadsetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import java.io.IOException;

/**
 * Main Activity for showing the application
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolBar; //toolbar button
    private WebView hBox; //chat history view
    private Button bConnect, bOff; //Connect and Off buttons
    private ImageView headsetImage, headsetGlowImage, speakerImage, speakerGlowImage; //headset and speaker images
    private static boolean onHeadset = false; //if headset is on
    private static boolean onSpeaker = false; //if speaker is on
    private static boolean isTranslating = false; //if app is in translating state
    private Bluetooth btconnection; //checks for Bluetooth headset and speaker
    private SpeechToSpeech s2s; //begins the speech-to-speech system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set-up settings
        TranslaTaSettings.initiateTranslaTaSettings(this);

        //initialize speech-to-speech system
        s2s = new SpeechToSpeech(this, new ChatHistoryAppender() {
            @Override
            public void onAddUserText(String text) {
                final String fText = text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addUserTextToWebView(fText);
                    }
                });

            }

            @Override
            public void onAddPartyText(String text) {
                final String fText = text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addPartyTextToWebView(fText);
                    }
                });
            }

            @Override
            public void onAddErrorText(String text) {
                final String fText = text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addErrorTextToWebView(fText);
                    }
                });
            }

        });

        //initialize all views for the main activity
        myToolBar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(myToolBar);
        bConnect = (Button) findViewById(R.id.bConnect_main);
        bConnect.setText(R.string.button_connect);
        bConnect.setOnClickListener(this);
        bOff = (Button) findViewById(R.id.off_toolbarButton);
        bOff.setOnClickListener(this);
        bOff.setVisibility(View.INVISIBLE);
        bOff.setClickable(false);
        headsetImage = (ImageView) findViewById(R.id.headset_mainImage);
        headsetGlowImage = (ImageView) findViewById(R.id.headset_glow_mainImage);
        speakerImage = (ImageView) findViewById(R.id.speaker_mainImage);
        speakerGlowImage = (ImageView) findViewById(R.id.speaker_glow_mainImage);

        //initialize the Bluetooth connection check
        btconnection = new Bluetooth(this,  new ChatHistoryAppender() {
            @Override
            public void onAddUserText(String text) {
            }

            @Override
            public void onAddPartyText(String text) {
            }

            @Override
            public void onAddErrorText(String text) {
                final String fText = text;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addErrorTextToWebView(fText);
                    }
                });
            }

        });

        //Initialize the web view for chat history
        hBox = (WebView) findViewById(R.id.wvChatHistory);
        hBox.loadUrl("file:///android_asset/chathtml.html");
        hBox.getSettings().setJavaScriptEnabled(true);
        hBox.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // When connect button is pressed
            case R.id.bConnect_main:

                //Condition to start checking for Bluetooth devices
                if(bConnect.getText().toString().equalsIgnoreCase("Connect")) {
                    //btconnection.checkConnection();
                    turnOn("speaker");
                    turnOn("headset");
                    bConnect.setText("Translate");
                }
                //condition if user wants to start the translating stage
                else if (bConnect.getText().toString().equalsIgnoreCase("Translate"))
                {
                    String t = "Translating";
                    bConnect.setText(t);
                    isTranslating = true;
                    bOff.setVisibility(View.VISIBLE);
                    bOff.setClickable(true);
                    s2s.beginListening();
                }
                break;

            // When off button is pressed, reset everything
            case R.id.off_toolbarButton:
                onHeadset = false;
                onSpeaker = false;
                changeSignals(headsetGlowImage, false);
                changeSignals(speakerGlowImage, false);
                changeSignals(headsetImage, true);
                changeSignals(speakerImage, true);
                bOff.setVisibility(View.INVISIBLE);
                bOff.setClickable(false);
                bConnect.setText(R.string.button_connect);
                if(isTranslating) {
                    try {
                        isTranslating = false;
                        s2s.stopListening();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //When one of the options from the default settings button is pressed
        switch (item.getItemId()) {

            // TranslaTa settings option
            case R.id.action_settings:
                Intent options = new Intent(this, SettingsActivity.class);
                if (isTranslating) {
                    bOff.performClick();
                }
                startActivity(options); //Starts new settings activity
                break;

            //Demo Activity
            case R.id.action_demo:
                Intent demo = new Intent(this, DemoActivity.class);
                if (isTranslating) {
                    bOff.performClick();
                }
                startActivity(demo); //Starts new settings activity
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        //When user enters app again after minimizing
        if (onHeadset && onSpeaker) {
            String t = "Translate";
            bConnect.setText(t);
        }
        if (isTranslating) {
            String t = "Translating";
            bConnect.setText(t);
        }
        super.onResume();
    }

    /**
     * Turn on the image for one of the devices (headset or speaker)
     * @param device (device whose image on the screen needs to be turned on)
     */
    public void turnOn (String device)
    {
        if (device.equalsIgnoreCase("headset") && !onHeadset)
        {
            changeSignals(headsetGlowImage, true);
            changeSignals(headsetImage, false);
            onHeadset = true;
        }
        else if (device.equalsIgnoreCase("speaker"))
        {
            changeSignals(speakerGlowImage, true);
            changeSignals(speakerImage, false);
            onSpeaker = true;
        }
        else
        {
            return;
        }

        if (onHeadset && onSpeaker)
        {
            String t = "Translate";
            bConnect.setText(t);
        }
    }

    /**
     * Turns off the highlight from the speaker and headset images
     * @param profile (the device or devices that need to be turned off)
     */
    public void turnOff(String profile)
    {
        if (profile.equalsIgnoreCase("headset"))
        {
            changeSignals(headsetGlowImage, false);
            changeSignals(headsetImage, true);
            onHeadset = false;
        }
        else if (profile.equalsIgnoreCase("speaker"))
        {
            changeSignals(speakerGlowImage, false);
            changeSignals(speakerImage, true);
            onSpeaker = false;
        }
        else if (profile.equalsIgnoreCase("both"))
        {
            if(onHeadset)
            {
                changeSignals(headsetGlowImage, false);
                changeSignals(headsetImage, true);
            }
            if(onSpeaker)
            {
                changeSignals(speakerGlowImage, false);
                changeSignals(speakerImage, true);
            }
            onHeadset = false;
            onSpeaker = false;
        }
    }

    /**
     * Changes the headset/speaker image from plain image to highlighted image and vice versa
     * @param view (image to highlight or return to normal)
     * @param toOn (flag to signal whether to highlight or return image to normal)
     */
    private void changeSignals(final ImageView view, final boolean toOn) {

        // Turn on the highlight, play the turn on animation
        if (toOn) {
            AlphaAnimation swap = new AlphaAnimation(0,1);
            swap.setDuration(500);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(swap);
        }
        //Turn off the highlight, play the turn off animation
        else {
            AlphaAnimation swap = new AlphaAnimation(1,0);
            swap.setDuration(500);
            view.startAnimation(swap);
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    view.setVisibility(View.INVISIBLE);
                }
            }.start();
        }
    }

    /**
     * Adds the user text to the chat history whenever the user speaks
     * @param text (speech input from the user)
     */
    private void addUserTextToWebView(String text) {
        String userT = "User:  " + text;
        String url = "javascript:addUserText('"+ userT +"')";
        hBox.loadUrl(url);
    }

    /**
     * Adds the other party text to the chat history whenever the other party speaks
     * @param text (speech input from the other party
     */
    private void addPartyTextToWebView(String text) {
        String partyT = "Party:  " + text;
        String url = "javascript:addPartyText('"+ partyT +"')";
        hBox.loadUrl(url);
    }

    /**
     * Adds error messages to the chat history when checking for Bluetooth devices
     * @param text (error message to print to chat history)
     */
    private void addErrorTextToWebView(String text)
    {
        String errorT = "Error:  " + text;
        String url = "javascript:addErrorText('"+ errorT +"')";
        hBox.loadUrl(url);
    }

    /**
     * Chat History interface for letting the user and other party know what they have said and what has been translated
     */
    public interface ChatHistoryAppender {

        void onAddUserText(String text);
        void onAddPartyText(String text);
        void onAddErrorText(String text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Bluetooth enabling request handling
        if(requestCode == Bluetooth.BLUETOOTH_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                btconnection.checkConnection();
            }
        }
    }
}
