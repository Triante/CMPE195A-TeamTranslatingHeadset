package com.example.triante.translatingheadsetapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolBar;
    private WebView hBox;
    private Button bConnect, bOff;
    private ImageView headsetImage, headsetGlowImage, speakerImage, speakerGlowImage;
    private static boolean onHeadset = false;
    private static boolean onSpeaker = false;
    private static boolean isTranslating = false;
    private Bluetooth btconnection;
    private SpeechToSpeech s2s;

    private int test = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TranslaTaSettings.initiateTranslaTaSettings(this);
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
        });
        myToolBar = (Toolbar) findViewById(R.id.mainActivity_toolbar);
        setSupportActionBar(myToolBar);
        bConnect = (Button) findViewById(R.id.bConnect_main);
        bConnect.setOnClickListener(this);
        bOff = (Button) findViewById(R.id.off_toolbarButton);
        bOff.setOnClickListener(this);
        bOff.setVisibility(View.INVISIBLE);
        bOff.setClickable(false);
        headsetImage = (ImageView) findViewById(R.id.headset_mainImage);
        headsetGlowImage = (ImageView) findViewById(R.id.headset_glow_mainImage);
        speakerImage = (ImageView) findViewById(R.id.speaker_mainImage);
        speakerGlowImage = (ImageView) findViewById(R.id.speaker_glow_mainImage);
        btconnection = new Bluetooth(this);

        hBox = (WebView) findViewById(R.id.wvChatHistory);
        hBox.loadUrl("file:///android_asset/chathtml.html");
        hBox.getSettings().setJavaScriptEnabled(true);
        hBox.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
//                while (test < 15) {
//                    addUserTextToWebView("Is this question number "+ test +  "?");
//                    addPartyTextToWebView("Yes, this is answer number " + test + "?");
//                    test++;
//                    hBox.scrollTo(0, 0);
//                }
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
            case R.id.bConnect_main:

                if(!onHeadset && !onSpeaker) {
                    btconnection.checkConnection();
                }
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
            case R.id.off_toolbarButton:
                onHeadset = false;
                onSpeaker = false;
                isTranslating = false;
                changeSignals(headsetGlowImage, false);
                changeSignals(speakerGlowImage, false);
                changeSignals(headsetImage, true);
                changeSignals(speakerImage, true);
                bOff.setVisibility(View.INVISIBLE);
                bOff.setClickable(false);
                bConnect.setText(R.string.button_connect);
                try {
                    s2s.stopListening();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent options = new Intent(this, SettingsActivity.class);
                if (isTranslating) {
                    bOff.performClick();
                }
                startActivity(options); //Starts new settings activity
                break;
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
    private void changeSignals(final ImageView view, final boolean toOn) {
        if (toOn) {
            AlphaAnimation swap = new AlphaAnimation(0,1);
            swap.setDuration(500);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(swap);
        }
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

    private void addUserTextToWebView(String text) {
        String userT = "User:  " + text;
        String url = "javascript:addUserText('"+ userT +"')";
        hBox.loadUrl(url);
    }

    private void addPartyTextToWebView(String text) {
        String partyT = "Party:  " + text;
        String url = "javascript:addPartyText('"+ partyT +"')";
        hBox.loadUrl(url);
    }



    interface ChatHistoryAppender {

        void onAddUserText(String text);
        void onAddPartyText(String text);

    }

}
