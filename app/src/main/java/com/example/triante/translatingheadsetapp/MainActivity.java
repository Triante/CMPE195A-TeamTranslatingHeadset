package com.example.triante.translatingheadsetapp;

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
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar myToolBar;
    private Button bConnect, bOff;
    private ImageView headsetImage, headsetGlowImage, speakerImage, speakerGlowImage;
    private static boolean isGlow = false;
    private static boolean isTranslating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TranslaTaSettings.initiateTranslaTaSettings(this);
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
                /*
               if (!isGlow) {
                   isGlow = true;
                   changeSignals(headsetGlowImage, true);
                   changeSignals(speakerGlowImage, true);
                   changeSignals(headsetImage, false);
                   changeSignals(speakerImage, false);
                   String t = "Translate\n" + LanguageSettings.getMyLanguageCode() + " to/from " + LanguageSettings.getResponseLanguageCode();
                   bConnect.setText(t);
               }
                else {
                   bOff.setVisibility(View.VISIBLE);
                   bOff.setClickable(true);
                   String t = "Translating\n" + LanguageSettings.getMyLanguageCode() + " to/from " + LanguageSettings.getResponseLanguageCode();
                   bConnect.setText(t);
                   isTranslating = true;
               }
               */
                break;
            case R.id.off_toolbarButton:
                isGlow = false;
                isTranslating = false;
                changeSignals(headsetGlowImage, false);
                changeSignals(speakerGlowImage, false);
                changeSignals(headsetImage, true);
                changeSignals(speakerImage, true);
                bOff.setVisibility(View.INVISIBLE);
                bOff.setClickable(false);
                bConnect.setText(R.string.button_connect);
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
                startActivity(options); //Starts new settings activity
                break;
            case R.id.action_demo:
                Intent demo = new Intent(this, DemoActivity.class);
                startActivity(demo); //Starts new settings activity
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (isGlow) {
            String t = "Translate\n" + LanguageSettings.getMyLanguageCode() + " to/from " + LanguageSettings.getResponseLanguageCode();
            bConnect.setText(t);
        }
        if (isTranslating) {
            String t = "Translating\n" + LanguageSettings.getMyLanguageCode() + " to/from " + LanguageSettings.getResponseLanguageCode();
            bConnect.setText(t);
        }
        super.onResume();
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


}
