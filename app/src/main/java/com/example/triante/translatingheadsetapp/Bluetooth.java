package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Luis on 2/24/2017.
 */

public class Bluetooth {

    private MainActivity main;
    private BluetoothAdapter adapter;
    public static final int BLUETOOTH_REQUEST = 21220;
    private boolean isOnHeadset;
    private boolean isOnSpeaker;
    private boolean isDoneChecking;
    private MainActivity.ChatHistoryAppender error;

    public Bluetooth (MainActivity home, MainActivity.ChatHistoryAppender appender)
    {
        main = home;
        isOnHeadset = false;
        isOnSpeaker = false;
        isDoneChecking = false;
        error = appender;
    }

    public String checkConnection()
    {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            error.onAddErrorText("This device does not support Bluetooth. " +
                    "Use another device that has Bluetooth 2.0 or higher enabled.");
            return "NOSUPPORT";
        }

        if (!adapter.isEnabled())
        {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            main.startActivityForResult(enabler, BLUETOOTH_REQUEST);
            return "ADAPTERENABLEINPROGRESS";
        }
        else
        {
            search();
            return "SEARCHBEGIN";
        }
    }

    private void search()
    {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.A2DP) {
                    BluetoothA2dp btA2dp = (BluetoothA2dp) proxy;
                    List<BluetoothDevice> a2dpConnectedDevices = btA2dp.getConnectedDevices();
                    if (a2dpConnectedDevices.size() != 0) {
                        for (BluetoothDevice device : a2dpConnectedDevices) {
                            if (device.getName().equalsIgnoreCase("speaker"))
                            {
                                main.turnOn("speaker");
                                isOnSpeaker = true;
                                continue;
                            }
                        }
                    }
                    if(!isOnSpeaker)
                    {
                        error.onAddErrorText("External speaker not connected. If the speaker is connected, make sure it is only connected" +
                                " to the media channel.");
                    }
                    mBluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, btA2dp);
                }
                else if (profile == BluetoothProfile.HEADSET)
                {
                    BluetoothHeadset headset = (BluetoothHeadset) proxy;
                    List<BluetoothDevice> headsetConnectedDevices = headset.getConnectedDevices();
                    if (headsetConnectedDevices.size() != 0) {
                        for (BluetoothDevice device : headsetConnectedDevices) {
                            if (device.getAddress().equalsIgnoreCase(main.getString(R.string.headset_key))) {
                                main.turnOn("headset");
                                isOnHeadset = true;
                                break;
                            }
                        }
                    }
                    if(!isOnHeadset)
                    {
                        error.onAddErrorText("RN52 headset not connected. If the headset is connected, make sure it is only connected" +
                                " to the call/communication channel.");
                    }
                    isDoneChecking = true;
                    mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, headset);
                    onServiceDisconnected(0);
                }
            }

            public void onServiceDisconnected(int profile) {
                new CountDownTimer(500,100)
                {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (isDoneChecking) {
                            if (!isOnHeadset || !isOnSpeaker) {
                                isOnHeadset = false;
                                isOnSpeaker = false;
                                isDoneChecking = false;
                                main.turnOff("both");
                            }
                        }
                    }
                }.start();
            }
        };

        mBluetoothAdapter.getProfileProxy(main, mProfileListener, BluetoothProfile.A2DP);
        mBluetoothAdapter.getProfileProxy(main, mProfileListener, BluetoothProfile.HEADSET);

    }

    public boolean isOnHeadset()
    {
        return isOnHeadset;
    }

    public boolean isOnSpeaker()
    {
        return isOnSpeaker;
    }
}
