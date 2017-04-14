package com.example.triante.translatingheadsetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

/* Activity used for allowing user to change languages*/
public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private int myLanguage = 0; //User's preferred languageSettings
    private int respLanguage = 0; //Other party's preferred languageSettings
    private boolean profanityFilter = true;

    private Spinner spinMyLang, spinRespLang; //Spinners used for displaying all possible languages
    private Button bAmplitudeSettings, bSave; //Button for saving preferences
    private SwitchCompat switchCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        /* Initializes and starts the view*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        /* Initializes the sae button*/
        bSave = (Button) findViewById(R.id.bSave);

        /* Initiates filter switch*/
        switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
        switchCompat.setOnCheckedChangeListener(this);
        
        /* Initializes the spinners used to hold the languageSettings options*/
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.MyLanguageArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinMyLang = (Spinner) findViewById(R.id.spinMyLang);
        spinMyLang.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.ResponseLanguageAvailable, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinRespLang = (Spinner) findViewById(R.id.spinRespLang);
        spinRespLang.setAdapter(adapter);
        initiateSpinnerPositionValues();
        spinMyLang.setSelection(myLanguage);
        spinRespLang.setSelection(respLanguage);
        switchCompat.setChecked(profanityFilter);
        
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

        bAmplitudeSettings = (Button) findViewById(R.id.bAmplitude_settings);
        bAmplitudeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ampActivity = new Intent(SettingsActivity.this, AmplitudeSettingsActivity.class);
                startActivity(ampActivity); //Starts new settings activity
            }
        });
    }

    /* Method to save the languageSettings settings for the session*/
    private void save() {
        switch (myLanguage) { //checks what languageSettings was chosen for the user preferred languageSettings
            case 0:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_M);
                break;
            case 1:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.GBENGLISH);
                break;
            case 2:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.SPANISH_M);
                break;
            case 3:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.FRENCH);
                break;
            case 4:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.JAPANESE);
                break;
            case 5:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.PORTUGUESE);
                break;
            case 6:
                LanguageSettings.setLanguage(true, LanguageSettings.Language.MANDARIN);
                break;
        }
        switch (respLanguage) { //checks what languageSettings was chosen for the other party's preferred languageSettings
            case 0:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_M);
                break;
            case 1:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_F);
                break;
            case 2:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.GBENGLISH);
                break;
            case 3:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.FRENCH);
                break;
            case 4:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.GERMAN_M);
                break;
            case 5:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.GERMAN_F);
                break;
            case 6:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.ITALIAN);
                break;
            case 7:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.JAPANESE);
                break;
            case 8:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.PORTUGUESE);
                break;
            case 9:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.SPANISH_M);
                break;
            case 10:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.SPANISH_F);
                break;
            case 11:
                LanguageSettings.setLanguage(false, LanguageSettings.Language.MXSPANISH);
                break;
        }
        TranslaTaSettings.setProfanityFilter(profanityFilter);
        TranslaTaSettings.saveLanguageSettings(this);
    }

    /* Defines all the options for the spinners*/
    private void initiateSpinnerPositionValues() {
        switch (LanguageSettings.getLanguage(true)) { //Sets the languageSettings values for the user's languageSettings preference
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

        switch (LanguageSettings.getLanguage(false)) { //Sets the languageSettings values for the other party's languageSettings preference
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

        profanityFilter = TranslaTaSettings.isProfanityFilterActive();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        profanityFilter = isChecked;
    }

    /* Inner class for managing the data that is retrieved from each option of the spinners*/
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener {

        private boolean isMyLanguage;

        public SpinnerSelectionListener(boolean myLanguage) {
            isMyLanguage = myLanguage;
        }
        
        /* Method to store the languageSettings chosen when user selects one of the options*/
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
