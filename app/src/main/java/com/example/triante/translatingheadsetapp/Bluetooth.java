package com.example.triante.translatingheadsetapp;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.CountDownTimer;
import java.util.List;

/**
 * Created by Luis on 2/24/2017.
 */

/**
 * Bluetooth class responsible for checking that a Bluetooth headset and Bluetooth speaker are connected to the mobile
 * device that is hosting this application. The Bluetooth headset needs to be connected to the 'Call Audio' channel. The
 * Bluetooth speaker needs to be connected to the 'Media Audio' channel.
 */
public class Bluetooth {

    private MainActivity main; //used for accessing features in the main activity UI
    private BluetoothAdapter adapter; //used for managing Bluetooth events
    public static final int BLUETOOTH_REQUEST = 21220; //Bluetooth Request number
    private boolean isOnHeadset; //used to identify that the headset is connected
    private boolean isOnSpeaker; //used to identify that the speaker is connected
    private boolean isDoneChecking; //used to identify when the system has checked for both devices
    private MainActivity.ChatHistoryAppender error; //used for updating chat history with error messages

    /**
     * Constructor for Bluetooth class
     * @param home (main activity instance for controlling UI)
     * @param appender (chat history instance for updating it with error messages)
     */
    public Bluetooth (MainActivity home, MainActivity.ChatHistoryAppender appender)
    {
        main = home;
        isOnHeadset = false;
        isOnSpeaker = false;
        isDoneChecking = false;
        error = appender;
    }

    /**
     * Checks for Bluetooth connectivity on the mobile device. It first checks if the phone can connect to Bluetooth, then
     * it checks if Bluetooth is enabled. If Bluetooth is not enabled, a new intent is made to enable it.
     * @return (String that let's system know whether the device supports Bluetooth or not)
     */
    public String checkConnection()
    {
        //get default adapter settings
        adapter = BluetoothAdapter.getDefaultAdapter();

        //No Bluetooth support on mobile device
        if (adapter == null) {
            error.onAddErrorText("This device does not support Bluetooth. " +
                    "Use another device that has Bluetooth 2.0 or higher enabled.");
            return "NOSUPPORT";
        }

        //Bluetooth is not enabled, needs to be enabled
        if (!adapter.isEnabled())
        {
            //Begin enable intent
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

    /**
     * Searches for both devices on the connected list of devices
     * If the Bluetooth headset is connected, main activity is alerted to light up headset image
     * If the Bluetooth speaker is connected, main activity is alerted to light up speaker image
     * If one or neither of the devices is connected, error messages are printed onto the chat history
     */
    private void search()
    {
        //Bluetooth adapter from phone
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Initialize connection listening service
        final BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {

            //device is connected event handler
            public void onServiceConnected(int profile, BluetoothProfile proxy) {

                //Speaker device (Media Audio)
                if (profile == BluetoothProfile.A2DP) {
                    BluetoothA2dp btA2dp = (BluetoothA2dp) proxy;
                    List<BluetoothDevice> a2dpConnectedDevices = btA2dp.getConnectedDevices(); //get all connected devices
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
                    //error message when speaker is not connected
                    if(!isOnSpeaker)
                    {
                        error.onAddErrorText("External speaker not connected. If the speaker is connected, make sure it is only connected" +
                                " to the media channel.");
                    }
                    mBluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, btA2dp);
                }
                //headset device (Call Audio)
                else if (profile == BluetoothProfile.HEADSET)
                {
                    BluetoothHeadset headset = (BluetoothHeadset) proxy;
                    List<BluetoothDevice> headsetConnectedDevices = headset.getConnectedDevices();
                    if (headsetConnectedDevices.size() != 0) {
                        for (BluetoothDevice device : headsetConnectedDevices) {
                            if (device.getName().equalsIgnoreCase("RN52-0201")) {
                                main.turnOn("headset");
                                isOnHeadset = true;
                                break;
                            }
                        }
                    }

                    //error message when headset is not connected
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

            //Handler when listening service is finished
            public void onServiceDisconnected(int profile) {
                new CountDownTimer(500,100)
                {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        //reset everything if both devices are not connected
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

        //Start searching for speaker
        mBluetoothAdapter.getProfileProxy(main, mProfileListener, BluetoothProfile.A2DP);

        //Start searching for headset
        mBluetoothAdapter.getProfileProxy(main, mProfileListener, BluetoothProfile.HEADSET);

    }

}
