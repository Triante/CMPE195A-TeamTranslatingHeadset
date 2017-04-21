package com.example.triante.translatingheadsetapp;

import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
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

    public Bluetooth (MainActivity home)
    {
        main = home;
        isOnHeadset = false;
        isOnSpeaker = false;
    }

    public void checkConnection()
    {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(main, "This device does not support Bluetooth. " +
                    "Use another device that has Bluetooth 2.0 or higher enabled.", Toast.LENGTH_LONG).show();
        }

        if (!adapter.isEnabled()) {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            main.startActivityForResult(enabler, BLUETOOTH_REQUEST);
        }
        else
        {
            System.out.println("Begin search!");
            search();
        }
    }

    public void search()
    {
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if(isOnSpeaker && isOnHeadset)
                {
                    break;
                }
                if (device.getAddress().equalsIgnoreCase(main.getString(R.string.headset_key)))
                {
                   main.turnOn("headset");
                    isOnHeadset = true;
                   continue;
                }
                if (device.getName().equalsIgnoreCase("SL SHOWER SPKR"))
                {
                    main.turnOn("speaker");
                    isOnSpeaker = true;
                    continue;
                }
            }

        }
    }
}
