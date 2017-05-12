package com.example.triante.translatingheadsetapp;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.AmplitudeListener;

import java.io.IOException;

/**
 * Class for rendering the Settings Ativity for the TranslaTa application
 */
public class AmplitudeSettingsActivity extends AppCompatActivity implements AmplitudeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, SeekBar.OnTouchListener{

    private ProgressBar amplitudeBar;
    private SeekBar thresholdBar, maxBar;
    private ImageView micImage;
    private TextView thresholdText, maxText, doneText;
    private Button bAutoRecord, bConfig, bDone;
    private MultipleMicrophoneInputStream micStream;
    private boolean microphoneRecording = false;
    private double currentAmp = 0;
    private int maxSetting = 0;
    private int thresholdSetting = 0;
    private final int MAX_THRESHOLD = 100000000;
    private boolean lockSeek = false;
    private boolean updateBar = false;

    /**
     * Perform initialization for the Translata's Amplitude Settings activity
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amplitude_settings);
        thresholdSetting = TranslaTaSettings.getThresholdAmplitude();
        maxSetting = TranslaTaSettings.getMaxAmplitude();

        //TextViews for displaying SeekBar progress and ImageView for showing microphone status.
        micImage = (ImageView) findViewById(R.id.imageMic);
        thresholdText = (TextView) findViewById(R.id.text_current);
        maxText = (TextView) findViewById(R.id.text_max);
        doneText = (TextView) findViewById(R.id.bDoneAmplitudeSubText);

        //SeekBars and ProgressBars
        thresholdBar = (SeekBar) findViewById(R.id.seekBar_threshold);
        thresholdBar.setOnSeekBarChangeListener(this);
        thresholdBar.setMax(MAX_THRESHOLD);
        maxBar = (SeekBar) findViewById(R.id.seekBar_max);
        maxBar.setOnSeekBarChangeListener(this);
        maxBar.setMax(MAX_THRESHOLD);
        amplitudeBar = (ProgressBar) findViewById(R.id.amplitude_bar);
        amplitudeBar.setMax(MAX_THRESHOLD);

        //Buttons
        bAutoRecord = (Button) findViewById(R.id.bRecordAmplitude);
        bAutoRecord.setOnClickListener(this);
        bConfig = (Button) findViewById(R.id.bConfigAmplitude);
        bConfig.setOnClickListener(this);
        bDone = (Button) findViewById(R.id.bDoneAmplitude);
        bDone.setOnClickListener(this);

        maxBar.setOnTouchListener(this);
        thresholdBar.setOnTouchListener(this);
        lockSeek = true;
        adjustSlidersAndBar(thresholdSetting);
    }

    /**
     * Listener function that provides the actions for the Amplitude Setting activity whener one of the following buttons are clicked:
     * bRecordAmplitude, bConfigAmplitude, bDone
     * @param v the button clicked as a view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRecordAmplitude:
                automaticCreateThresholdDialog();
                break;
            case R.id.bConfigAmplitude:
                lockSeek = false;
                bAutoRecord.setClickable(false);
                bConfig.setClickable(false);
                bDone.setVisibility(View.VISIBLE);
                bDone.setClickable(true);
                doneText.setVisibility(View.VISIBLE);
                micImage.setImageResource(R.drawable.ic_microphone_glow);
                updateBar = true;
                microphoneSwitch();
                break;
            case R.id.bDoneAmplitude:
                lockSeek = true;
                bAutoRecord.setClickable(true);
                bConfig.setClickable(true);
                bDone.setVisibility(View.INVISIBLE);
                bDone.setClickable(false);
                doneText.setVisibility(View.INVISIBLE);
                micImage.setImageResource(R.drawable.ic_microphone);
                updateBar = false;
                microphoneSwitch();
                save();
                break;
        }
    }

    /**
     * Listern function for using amplitude and volume values which are passed from another location
     * @param amplitude the amplitude value
     * @param volume the volume value
     */
    @Override
    public void onSample(double amplitude, double volume) {
        if (updateBar) {
            amplitudeBar.setProgress((int) amplitude);
        }
        currentAmp = amplitude;
    }

    /**
     * Listiner function that runs specific actions when the progress is changes in a SeekBar
     * @param seekBar the seekBar that had it's progress changed
     * @param progress the new progress value
     * @param fromUser true of the user changes the value, false otherwise
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        String p = "" + progress;
        switch (seekBar.getId()) {
            case R.id.seekBar_threshold:
                thresholdText.setText(p);
                amplitudeBar.setSecondaryProgress(progress);
                thresholdSetting = progress;
                break;
            case R.id.seekBar_max:
                maxText.setText(p);
                amplitudeBar.setMax(progress);
                thresholdBar.setMax(progress);
                maxSetting = progress;
                if (thresholdSetting > maxSetting) {
                    thresholdSetting = maxSetting;
                    thresholdBar.setProgress(maxSetting);
                    amplitudeBar.setSecondaryProgress(maxSetting);
                    thresholdText.setText(p);
                }
                break;
        }
    }

    /**
     * not used
     * @param seekBar not used
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    /**
     * Not used
     * @param seekBar not used
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    /**
     * Turns the microphone on from off, or from off to on.
     */
    private void microphoneSwitch() {
        if (microphoneRecording) {
            try {
                micStream.close();
                microphoneRecording = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            micStream = new MultipleMicrophoneInputStream(0);
            micStream.setOnAmplitudeListener(this);
            micStream.startRecording();
            microphoneRecording = true;
        }
    }

    /**
     * Creates and displays the dialog for messaging the user about creating the amplitude threshold automatically,
     * Calls automaticCreateThresholdAction when user accepts the perform the action
     */
    private void automaticCreateThresholdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.amplitude_dialog_title);
        builder.setMessage(R.string.amplitude_dialog_message);
        builder.setPositiveButton(R.string.amplitude_dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                automaticCreateThresholdAction();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.show();
    }

    /**
     * Creates and displays the dialog for notifying the user that their voice is being recorded to calculate
     * their average amplitude threshold for 10 seconds. Runs, records, and calculates the user average threshold.
     * This method is called from automaticCreateThresholdDialog. Calls automaticCreateThresholdFinish
     * when calculating is finished.
     */
    private void automaticCreateThresholdAction() {
        final AmplitudeThresholdTask task = new AmplitudeThresholdTask();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        View progressBarView = getLayoutInflater().inflate(R.layout.amplitude_recording_dialog_progress_bar, null);
        builder.setTitle(R.string.recording_dialog_title);
        builder.setView(progressBarView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        final ProgressBar recordingBar = (ProgressBar) progressBarView.findViewById(R.id.recordingProgressBar);
        recordingBar.setMax(10000);
        task.start();
        CountDownTimer timer = new CountDownTimer(10000, 100) {
           int progress = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                progress = progress + 100;
                recordingBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                task.stopRun();
                int s = task.getCount();
                double threshold = task.getAverageAmplitude();
                Log.d("Average: ", threshold + "");
                thresholdSetting = (int) threshold;
                recordingBar.setProgress(recordingBar.getMax());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                automaticCreateThresholdFinish(threshold);
            }
        };
        timer.start();
    }

    /**
     * Creates and displays a dialog to notify the user that their average threshold was created and saved.
     * This method is called from automaticCreateThresholdFinish.
     * @param threshold the calculated average value
     */
    private void automaticCreateThresholdFinish(final double threshold) {
        String part1 = getString(R.string.finish_dilog_message1);
        String part2 = getString(R.string.finish_dilog_message2);
        String message = part1 + " " + (int) threshold + part2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.finish_dialog_title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                adjustSlidersAndBar(threshold);
            }
        });
        builder.show();
    }

    /**
     * Adjusts the sliders on the screen to their positions based on the threshold value that is passed.
     * Sets the threshold value to the correct position based on the max value calculated from the
     * value passed.
     * @param threshold the based value to set the slider progress positions too
     */
    private void adjustSlidersAndBar(double threshold) {
        if (threshold > MAX_THRESHOLD) {
            threshold = MAX_THRESHOLD;
        }
        int newMax = 2 * ((int) (threshold / 1E7) + 1);
        newMax = (int) (newMax * 1E7);
        if (newMax > MAX_THRESHOLD) {
            newMax = MAX_THRESHOLD;
        }
        maxSetting = newMax;
        thresholdSetting = (int) threshold;
        thresholdBar.setMax(maxSetting);
        thresholdBar.setProgress(thresholdSetting);
        maxBar.setProgress(maxSetting);
        String text = maxSetting + "";
        maxText.setText(text);
        text = thresholdSetting + "";
        thresholdText.setText(text);
        amplitudeBar.setMax(maxSetting);
        amplitudeBar.setSecondaryProgress(thresholdSetting);

    }

    /**
     * Saves the current amplitude threshold and max values to TranslaTa's user settings
     */
    private void save () {
        TranslaTaSettings.setMaxAmplitude(maxSetting);
        TranslaTaSettings.setAmplitudeThreshold(thresholdSetting);
        TranslaTaSettings.saveAmplitudeSettings(this);
        //Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Listener function that allows the slider to be changed based on a set internal flag in the amplitude settings activity
     * Allows the sliders to be selectable and movable if the user is in manual amplitude config mode, otherwise not
     * @param v the slider being selected as a view (not used)
     * @param event The motion event om the slider (not user)
     * @return true if the sliders should not be selectable by the user, false otherwise
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return lockSeek;
    }

    /**
     * Thread to capture audio from the microphone and convert the average amplitude until the
     * thread is called to stop.
     */
    private class AmplitudeThresholdTask extends Thread {

        private boolean run = true;
        private AmplitudeAverageCalculator calculator = new AmplitudeAverageCalculator();

        /**
         * Starts the thread. begins to capture audio and add values to a amplitude average calculator
         */
        @Override
        public void run() {
            updateBar = false;
            microphoneSwitch(); //turn on mic to start recording
            while (run) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                calculator.addAmpValue(currentAmp);
                Log.d("Amplitude Recorded: ", "" + currentAmp);
            }
        }

        /**
         * Stops the thread from capturing audio and adding to the average amplitude calculator
         */
        public void stopRun() {
            if (run) {
                run = false;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                microphoneSwitch(); //turn off microphone
            }

        }

        /**
         * Retrieves the calculated average amplitude value
         * @return the average amplitude threshold
         */
        public double getAverageAmplitude() {
            return calculator.getAverageAmp();
        }

        /**
         * Retrieves the amount of values which where added to the calculator
         * @return the count of values in the calculator
         */
        public int getCount() {
            return calculator.getCount();
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity as appropriate.
     * Stops the microphone from capturing audio before it finishes the activity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!lockSeek) {
            lockSeek = true;
            microphoneSwitch();
        }
    }
}
