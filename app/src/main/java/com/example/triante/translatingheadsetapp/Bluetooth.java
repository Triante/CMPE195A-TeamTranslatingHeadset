package com.example.triante.translatingheadsetapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
    private BluetoothDevice headset;
    private BluetoothDevice speaker;
    public static final int BLUETOOTH_REQUEST = 21220;
    private BroadcastReceiver discoveryReceiver;

    public Bluetooth (MainActivity home)
    {
        main = home;

        discoveryReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (device.getAddress().equalsIgnoreCase(main.getString(R.string.headset_key)))
                    {
                        headset = device;
                        adapter.cancelDiscovery();
                        beginConnectionIntent();
                    }

                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        main.registerReceiver(discoveryReceiver, filter);
    }

    public void connect()
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
            search();
        }
    }

    public void search()
    {
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equalsIgnoreCase(main.getString(R.string.headset_key)))
                {
                    headset = device;
                    beginConnectionIntent();
                    return;
                }
                if (device.getAddress().equalsIgnoreCase(""))
                {

                }
            }

        }

        adapter.startDiscovery();

    }

    private void beginConnectionIntent()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        main.registerReceiver(connectionReceiver, filter);

        BluetoothServer phone = new BluetoothServer("Android Phone");
        phone.run();
        BluetoothClient head_piece = new BluetoothClient(headset, phone.idFromServer());
        head_piece.run();
    }

    public void unregisterReceiver ()
    {
        main.unregisterReceiver(discoveryReceiver);
    }

    private class BluetoothServer extends Thread {
        private final BluetoothServerSocket serverSocket;
        private String name;
        private UUID serverID;

        public BluetoothServer(String btName) {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            name = btName;
            serverID = UUID.randomUUID();
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = adapter.listenUsingRfcommWithServiceRecord(btName, serverID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            serverSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    try {
                        serverSocket.close();
                    }catch(IOException ioTrouble)
                    {
                        Log.e(TAG, "Could not close the connect socket", ioTrouble);
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException ioTrouble) {
                Log.e(TAG, "Could not close the connect socket", ioTrouble);
            }
        }

        public UUID idFromServer ()
        {
            return serverID;
        }
    }

    private class BluetoothClient extends Thread {
        private final BluetoothSocket clientSocket;
        private final BluetoothDevice desiredDevice;


        public BluetoothClient(BluetoothDevice device, UUID server_client_id) {
            BluetoothSocket tmp = null;
            desiredDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(server_client_id);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            clientSocket = tmp;
        }

        public void run() {
            adapter.cancelDiscovery();

            try {
                clientSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    clientSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            Toast.makeText(main, "Device connected!", Toast.LENGTH_LONG).show();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private final BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
            }
        }
    };

}
