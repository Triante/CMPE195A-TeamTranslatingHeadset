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
        doneText = (TextView) findViewById(R.id.text_done);

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

    @Override
    public void onSample(double amplitude, double volume) {
        if (updateBar) {
            amplitudeBar.setProgress((int) amplitude);
        }
        currentAmp = amplitude;
    }

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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //do nothing
    }

    private void microphoneSwitch() {
        if (microphoneRecording) {
            try {
                micStream.close();
                microphoneRecording = false;
                //micImage.setImageResource(R.drawable.ic_microphone);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            micStream = new MultipleMicrophoneInputStream(0);
            micStream.setOnAmplitudeListener(this);
            micStream.startRecording();
            microphoneRecording = true;
            //micImage.setImageResource(R.drawable.ic_microphone_glow);
        }
    }

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

    private void save () {
        TranslaTaSettings.setMaxAmplitude(maxSetting);
        TranslaTaSettings.setAmplitudeThreshold(thresholdSetting);
        TranslaTaSettings.saveAmplitudeSettings(this);
        //Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return lockSeek;
    }


    private class AmplitudeThresholdTask extends Thread {

        private boolean run = true;
        private AmplitudeAverageCalculator calculator = new AmplitudeAverageCalculator();

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

        public double getAverageAmplitude() {
            return calculator.getAverageAmp();
        }
        public int getCount() {
            return calculator.getCount();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!lockSeek) {
            lockSeek = true;
            microphoneSwitch();
        }
    }
}
