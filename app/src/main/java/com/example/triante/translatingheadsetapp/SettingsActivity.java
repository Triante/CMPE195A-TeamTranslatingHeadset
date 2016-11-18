package com.example.triante.translatingheadsetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/* Activity used for allowing user to change languages*/
public class SettingsActivity extends AppCompatActivity {

    private int myLanguage = 0; //User's preferred language
    private int respLanguage = 0; //Other party's preferred language

    private Spinner spinMyLang, spinRespLang; //Spinners used for displaying all possible languages
    private Button bSave; //Button for saving preferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        /* Initializes and starts the view*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        /* Initializes the sae button*/
        bSave = (Button) findViewById(R.id.bSave);
        
        /* Initializes the spinners used to hold the language options*/
        spinMyLang = (Spinner) findViewById(R.id.spinMyLang);
        spinRespLang = (Spinner) findViewById(R.id.spinRespLang);
        initiateSpinnerPositionValues();
        spinMyLang.setSelection(myLanguage);
        spinRespLang.setSelection(respLanguage);
        
        /* Sets the on click listeners for the button and spinners */
        spinMyLang.setOnItemSelectedListener(new SpinnerSelectionListener(true));
        spinRespLang.setOnItemSelectedListener(new SpinnerSelectionListener(false));
        
        /* Defines the on click listener for the save button*/
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                Toast.makeText(SettingsActivity.this, "Settings Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Method to save the language settings for the session*/
    private void save() {
        switch (myLanguage) { //checks what language was chosen for the user preferred language
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
        switch (respLanguage) { //checks what language was chosen for the other party's preferred language
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

    /* Defines all the options for the spinners*/
    private void initiateSpinnerPositionValues() {
        switch (Language.getLanguage(true)) { //Sets the language values for the user's language preference
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

        switch (Language.getLanguage(false)) { //Sets the language values for the other party's language preference
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

    /* Inner class for managing the data that is retrieved from each option of the spinners*/
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener {

        private boolean isMyLanguage;

        public SpinnerSelectionListener(boolean myLanguage) {
            isMyLanguage = myLanguage;
        }
        
        /* Method to store the language chosen when user selects one of the options*/
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isMyLanguage) myLanguage = position;
            else respLanguage = position;
        }
        
        /* Method to contorl what happens when none of the options are selected */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //No implementation yet
        }
    }
}
