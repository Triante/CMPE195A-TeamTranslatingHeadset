package com.example.triante.translatingheadsetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private int myLanguage = 0;
    private int respLanguage = 0;

    private Spinner spinMyLang, spinRespLang;
    private Button bSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bSave = (Button) findViewById(R.id.bSave);

        spinMyLang = (Spinner) findViewById(R.id.spinMyLang);
        spinRespLang = (Spinner) findViewById(R.id.spinRespLang);
        initiateSpinnerPositionValues();
        spinMyLang.setSelection(myLanguage);
        spinRespLang.setSelection(respLanguage);

        spinMyLang.setOnItemSelectedListener(new SpinnerSelectionListener(true));
        spinRespLang.setOnItemSelectedListener(new SpinnerSelectionListener(false));
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                Toast.makeText(SettingsActivity.this, "Settings Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save() {
        switch (myLanguage) {
            case 0:
                Language.setLanguage(true, Language.LanguageSelect.ENGLISH_M);
                break;
            case 1:
                Language.setLanguage(true, Language.LanguageSelect.GBENGLISH);
                break;
            case 2:
                Language.setLanguage(true, Language.LanguageSelect.SPANISH_M);
                break;
            case 3:
                Language.setLanguage(true, Language.LanguageSelect.FRENCH);
                break;
            case 4:
                Language.setLanguage(true, Language.LanguageSelect.JAPANESE);
                break;
            case 5:
                Language.setLanguage(true, Language.LanguageSelect.PORTUGUESE);
                break;
            case 6:
                Language.setLanguage(true, Language.LanguageSelect.MANDARIN);
                break;
        }
        switch (respLanguage) {
            case 0:
                Language.setLanguage(false, Language.LanguageSelect.ENGLISH_M);
                break;
            case 1:
                Language.setLanguage(false, Language.LanguageSelect.ENGLISH_F);
                break;
            case 2:
                Language.setLanguage(false, Language.LanguageSelect.GBENGLISH);
                break;
            case 3:
                Language.setLanguage(false, Language.LanguageSelect.FRENCH);
                break;
            case 4:
                Language.setLanguage(false, Language.LanguageSelect.GERMAN_M);
                break;
            case 5:
                Language.setLanguage(false, Language.LanguageSelect.GERMAN_F);
                break;
            case 6:
                Language.setLanguage(false, Language.LanguageSelect.ITALIAN);
                break;
            case 7:
                Language.setLanguage(false, Language.LanguageSelect.JAPANESE);
                break;
            case 8:
                Language.setLanguage(false, Language.LanguageSelect.PORTUGUESE);
                break;
            case 9:
                Language.setLanguage(false, Language.LanguageSelect.SPANISH_M);
                break;
            case 10:
                Language.setLanguage(false, Language.LanguageSelect.SPANISH_F);
                break;
            case 11:
                Language.setLanguage(false, Language.LanguageSelect.MXSPANISH);
                break;
        }
    }

    private void initiateSpinnerPositionValues() {
        switch (Language.getLanguage(true)) {
            case ENGLISH_M:
                myLanguage = 0;
                break;
            case GBENGLISH:
                myLanguage = 1;
                break;
            case SPANISH_M:
                myLanguage = 2;
                break;
            case FRENCH:
                myLanguage = 3;
                break;
            case JAPANESE:
                myLanguage = 4;
                break;
            case PORTUGUESE:
                myLanguage = 5;
                break;
            case MANDARIN:
                myLanguage = 6;
                break;
        }

        switch (Language.getLanguage(false)) {
            case ENGLISH_M:
                respLanguage = 0;
                break;
            case ENGLISH_F:
                respLanguage = 1;
                break;
            case GBENGLISH:
                respLanguage = 2;
                break;
            case FRENCH:
                respLanguage = 3;
                break;
            case GERMAN_M:
                respLanguage = 4;
                break;
            case GERMAN_F:
                respLanguage = 5;
                break;
            case ITALIAN:
                respLanguage = 6;
                break;
            case JAPANESE:
                respLanguage = 7;
                break;
            case PORTUGUESE:
                respLanguage = 8;
                break;
            case SPANISH_M:
                respLanguage = 9;
                break;
            case SPANISH_F:
                respLanguage = 10;
                break;
            case MXSPANISH:
                respLanguage = 11;
                break;
        }
    }

    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener {

        private boolean isMyLanguage;

        public SpinnerSelectionListener(boolean myLanguage) {
            isMyLanguage = myLanguage;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isMyLanguage) myLanguage = position;
            else respLanguage = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
