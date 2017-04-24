package com.example.triante.translatingheadsetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_GB_KATE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_ALLISON;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_LISA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.ENGLISH_US_MICHEAL;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.FRENCH_RENEE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.JAPANESE_EMI;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.PORTUGUESE_BR_IABELA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_ES_ENRIQUE;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_ES_LAURA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_MX_SOFIA;
import static com.example.triante.translatingheadsetapp.LanguageSettings.Language.SPANISH_US_SOFIA;

/* Activity used for allowing user to change languages*/
public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private int myLanguage = 0; //User's preferred languageSettings
    private int respLanguage = 0; //Other party's preferred languageSettings
    private int myVoice = 0;
    private int respVoice = 0;
    private boolean profanityFilter = true;
    private boolean ignoreFirstRunForMyLanguage = true;
    private boolean ignoreFirstRunForRespLanguage = true;

    private Spinner spinMyLang, spinRespLang, spinMyVoice, spinRespVoice; //Spinners used for displaying all possible languages
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
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.LanguageArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinMyLang = (Spinner) findViewById(R.id.spinMyLang);
        spinMyLang.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.LanguageArray, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinRespLang = (Spinner) findViewById(R.id.spinRespLang);
        spinRespLang.setAdapter(adapter);

        spinMyVoice = (Spinner) findViewById(R.id.spinMyVoice);
        spinMyVoice.setAdapter(adapter);
        spinRespVoice = (Spinner) findViewById(R.id.spinResVoice);
        spinRespVoice.setAdapter(adapter);

        initiateSpinnerPositionValues();
        Log.d("Load", myLanguage + ":" + myVoice);
        Log.d("Load", respLanguage + ":" + respVoice);
        setVoiceSpinner(myLanguage, true);
        spinMyVoice.setSelection(myVoice);
        setVoiceSpinner(respLanguage, false);
        spinRespVoice.setSelection(respVoice);
        spinMyLang.setSelection(myLanguage);
        spinRespLang.setSelection(respLanguage);
        switchCompat.setChecked(profanityFilter);
        Log.d("Load After Init", myLanguage + ":" + myVoice);
        Log.d("Load After Init", respLanguage + ":" + respVoice);


        
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

        /* Sets the on click listeners for the button and spinners */
        spinMyLang.setOnItemSelectedListener(new SpinnerSelectionListener(0));
        spinMyVoice.setOnItemSelectedListener(new SpinnerSelectionListener(1));
        spinRespLang.setOnItemSelectedListener(new SpinnerSelectionListener(2));
        spinRespVoice.setOnItemSelectedListener(new SpinnerSelectionListener(3));
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("spinMyVoice", myVoice + "");
        Log.d("spinRespVoice", respVoice + "");
        Log.d("Load Pos After Init", spinMyVoice.getSelectedItemPosition() + "");
        Log.d("Load Pos After Init", spinRespVoice.getSelectedItemPosition() + "");

    }

    /* Method to save the languageSettings settings for the session*/
    private void save() {
        //save user language preferences
        if (myLanguage == 0) {
            if (myVoice == 0) {
                LanguageSettings.setLanguage(true, ENGLISH_US_ALLISON);
            }
            else if (myVoice == 1) {
                LanguageSettings.setLanguage(true, ENGLISH_US_LISA);
            }
            else if (myVoice == 2) {
                LanguageSettings.setLanguage(true, ENGLISH_US_MICHEAL);
            }
            else if (myVoice == 3) {
                LanguageSettings.setLanguage(true, ENGLISH_GB_KATE);
            }
        }
        else if (myLanguage == 1) {
            if (myVoice == 0) {
                LanguageSettings.setLanguage(true, SPANISH_MX_SOFIA);
            }
            else if (myVoice == 1) {
                LanguageSettings.setLanguage(true, SPANISH_US_SOFIA);
            }
            else if (myVoice == 2) {
                LanguageSettings.setLanguage(true, SPANISH_ES_LAURA);
            }
            else if (myVoice == 3) {
                LanguageSettings.setLanguage(true, SPANISH_ES_ENRIQUE);
            }
        }
        else if (myLanguage == 2) LanguageSettings.setLanguage(true, FRENCH_RENEE);
        else if (myLanguage == 3) LanguageSettings.setLanguage(true, JAPANESE_EMI);
        else if (myLanguage == 4) LanguageSettings.setLanguage(true, PORTUGUESE_BR_IABELA);

        //save user language preferences
        if (respLanguage == 0) {
            if (respVoice == 0) {
                LanguageSettings.setLanguage(false, ENGLISH_US_ALLISON);
            }
            else if (respVoice == 1) {
                LanguageSettings.setLanguage(false, ENGLISH_US_LISA);
            }
            else if (respVoice == 2) {
                LanguageSettings.setLanguage(false, ENGLISH_US_MICHEAL);
            }
            else if (respVoice == 3) {
                LanguageSettings.setLanguage(false, ENGLISH_GB_KATE);
            }
        }
        else if (respLanguage == 1) {
            if (respVoice == 0) {
                LanguageSettings.setLanguage(false, SPANISH_MX_SOFIA);
            }
            else if (respVoice == 1) {
                LanguageSettings.setLanguage(false, SPANISH_US_SOFIA);
            }
            else if (respVoice == 2) {
                LanguageSettings.setLanguage(false, SPANISH_ES_LAURA);
            }
            else if (respVoice == 3) {
                LanguageSettings.setLanguage(false, SPANISH_ES_ENRIQUE);
            }
        }
        else if (respLanguage == 2) LanguageSettings.setLanguage(false, FRENCH_RENEE);
        else if (respLanguage == 3) LanguageSettings.setLanguage(false, JAPANESE_EMI);
        else if (respLanguage == 4) LanguageSettings.setLanguage(false, PORTUGUESE_BR_IABELA);


        Log.d("Saved", myLanguage + ":" + myVoice);

        Log.d("Saved", respLanguage + ":" + respVoice);

        TranslaTaSettings.setProfanityFilter(profanityFilter);
        TranslaTaSettings.saveLanguageSettings(this);
    }

    /* Defines all the options for the spinners*/
    private void initiateSpinnerPositionValues() {
        switch (LanguageSettings.getLanguage(true)) { //Sets the languageSettings values for the user's languageSettings preference
            case ENGLISH_GB_KATE:
                myVoice = 3;
                myLanguage = 0;
                break;
            case ENGLISH_US_ALLISON:
                myVoice = 0;
                myLanguage = 0;
                break;
            case ENGLISH_US_LISA:
                myVoice = 1;
                myLanguage = 0;
                break;
            case ENGLISH_US_MICHEAL:
                myVoice = 2;
                myLanguage = 0;
                break;
            case FRENCH_RENEE:
                myLanguage = 2;
                break;
            case JAPANESE_EMI:
                myLanguage = 3;
                break;
            case PORTUGUESE_BR_IABELA:
                myLanguage = 4;
                break;
            case SPANISH_ES_ENRIQUE:
                myLanguage = 1;
                myVoice = 3;
                break;
            case SPANISH_ES_LAURA:
                myLanguage = 1;
                myVoice = 2;
                break;
            case SPANISH_MX_SOFIA:
                myLanguage = 1;
                myVoice = 1;
                break;
            case SPANISH_US_SOFIA:
                myLanguage = 1;
                myVoice = 0;
                break;
            default:
                myLanguage = 0;
                myVoice = 0;
                break;
        }

        switch (LanguageSettings.getLanguage(false)) { //Sets the languageSettings values for the other party's languageSettings preference
            case ENGLISH_GB_KATE:
                respVoice = 3;
                respLanguage = 0;
                break;
            case ENGLISH_US_ALLISON:
                respVoice = 0;
                respLanguage = 0;
                break;
            case ENGLISH_US_LISA:
                respVoice = 1;
                respLanguage = 0;
                break;
            case ENGLISH_US_MICHEAL:
                respVoice = 2;
                respLanguage = 0;
                break;
            case FRENCH_RENEE:
                respLanguage = 2;
                break;
            case JAPANESE_EMI:
                respLanguage = 3;
                break;
            case PORTUGUESE_BR_IABELA:
                respLanguage = 4;
                break;
            case SPANISH_ES_ENRIQUE:
                respLanguage = 1;
                respVoice = 3;
                break;
            case SPANISH_ES_LAURA:
                respLanguage = 1;
                respVoice = 2;
                break;
            case SPANISH_MX_SOFIA:
                respLanguage = 1;
                respVoice = 1;
                break;
            case SPANISH_US_SOFIA:
                respLanguage = 1;
                respVoice = 0;
                break;
            default:
                respLanguage = 0;
                respVoice = 0;
                break;
        }

        profanityFilter = TranslaTaSettings.isProfanityFilterActive();

    }

    private void setVoiceSpinner(int lang, boolean myLang) {
        ArrayAdapter adapter;
        if (lang == 0) {
            adapter = ArrayAdapter.createFromResource(this, R.array.EnglishVoiceArray, R.layout.spinner_item);
        }
        else if (lang == 1) {
            adapter = ArrayAdapter.createFromResource(this, R.array.SpanishVoiceArray, R.layout.spinner_item);
        }
        else if (lang == 2) {
            adapter = ArrayAdapter.createFromResource(this, R.array.FrenchVoiceArray, R.layout.spinner_item);
        }
        else if (lang == 3) {
            adapter = ArrayAdapter.createFromResource(this, R.array.JapaneseVoiceArray, R.layout.spinner_item);
        }
        else {
            adapter = ArrayAdapter.createFromResource(this, R.array.PortugueseVoiceArray, R.layout.spinner_item);
        }
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        if (myLang) {
            spinMyVoice.setAdapter(adapter);
        }
        else {
            spinRespVoice.setAdapter(adapter);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        profanityFilter = isChecked;
    }

    /* Inner class for managing the data that is retrieved from each option of the spinners*/
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener {

        /**
         * 0: my lang
         * 1: my voice
         * 2: party lang
         * 3: party voice
         */
        private int spinnerID;

        public SpinnerSelectionListener(int ID) {
            spinnerID = ID;
        }
        
        /* Method to store the languageSettings chosen when user selects one of the options*/
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (spinnerID == 0)
            {
                if (ignoreFirstRunForMyLanguage) {
                    ignoreFirstRunForMyLanguage = false;
                }
                else {
                    myLanguage = position;
                    myVoice = 0;
                    setVoiceSpinner(myLanguage, true);
                }
            }
            else if (spinnerID == 1) myVoice = position;
            else if (spinnerID == 2)
            {
                if (ignoreFirstRunForRespLanguage) {
                    ignoreFirstRunForRespLanguage = false;
                }
                else {
                    respLanguage = position;
                    respVoice = 0;
                    setVoiceSpinner(respLanguage, false);
                }
            }
            else respVoice = position;
        }
        
        /* Method to contorl what happens when none of the options are selected */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //No implementation yet
        }
    }
}
