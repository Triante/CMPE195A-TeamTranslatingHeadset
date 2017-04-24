package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.os.CountDownTimer;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.Bluetooth;
import com.example.triante.translatingheadsetapp.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Jorge Aguiniga on 4/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class BluetoothTests {
    private MainActivity main;
    private Bluetooth btClass;
    private String errorText;
    private final String RESULT_NO_ADAPTER = "NOSUPPORT";
    private final String RESULT_BT_OFF = "ADAPTERENABLEINPROGRESS";
    private final String RESULT_BT_ON = "SEARCHBEGIN";
    private final String RESULT_NO_ADAPTER_ERROR = "This device does not support Bluetooth. Use another device that has Bluetooth 2.0 or higher enabled.";
    private final String RESULT_NO_DEVICES = "External speaker not connected. If the speaker is connected, make sure it is only connected to the media channel.RN52 headset not connected. If the headset is connected, make sure it is only connected to the call/communication channel.";
    private final String RESULT_SPEAKER_ONLY = "RN52 headset not connected. If the headset is connected, make sure it is only connected to the call/communication channel.";
    private final String RESULT_HEADSET_ONLY = "External speaker not connected. If the speaker is connected, make sure it is only connected to the media channel.";
    private final String RESULT_BOTH_CONNECTED = "";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void startBluetooth() {
        main = mainActivityRule.getActivity();
        btClass = new Bluetooth(main, new MainActivity.ChatHistoryAppender() {
            @Override
            public void onAddUserText(String text) {
            }

            @Override
            public void onAddPartyText(String text) {
            }

            @Override
            public void onAddErrorText(String text) {
                errorText += text;

            }

        });
        errorText = "";

    }

    /**
     * Only checks if detecting no Bluetooth adapter gives the correct error codes. Can only be tested in one of two ways:
     *      1. Use an Android device with no Bluetooth adapter and run the test as is
     *      2. Use an Android device with Bluetooth adapter, change Bluetooth variable "adapter" to have the value "null" assigned to it, run the test
     */
    @Test
    public void test_noadapter() {
        String result = btClass.checkConnection();
        assertEquals("Check No Adapter Return Code", RESULT_NO_ADAPTER, result);
        assertEquals("Check No Adapter Error Code", RESULT_NO_ADAPTER_ERROR, errorText);
        errorText = "";
    }

    @Test
    public void test_bluetoothOff() {
        String result = btClass.checkConnection();
        assertEquals("Check Bluetooth Off Return Code", RESULT_BT_OFF, result);
    }

    @Test
    public void test_bluetoothOn() {
        String result = btClass.checkConnection();
        assertEquals("Check Bluetooth On Return Code", RESULT_BT_ON, result);
    }

    @Test
    public void test_headsetOnOnly() {
        test_bluetoothOn();
        while(errorText == "")
        {
            //wait
        }
        assertEquals("Check Bluetooth Headset On Only Error Code", RESULT_HEADSET_ONLY, errorText);
        errorText = "";
    }

    @Test
    public void test_speakerOnOnly() {
        test_bluetoothOn();
        while(errorText == "")
        {
            //wait
        }
        assertEquals("Check Bluetooth Speaker On Only Error Code", RESULT_SPEAKER_ONLY, errorText);
        errorText = "";
    }

    @Test
    public void test_bothDevicesOn() {
        test_bluetoothOn();
        assertEquals("Check Bluetooth Devices On Error Code", RESULT_BOTH_CONNECTED, errorText);
        errorText = "";
    }

    @Test
    public void test_noDevicesOn() {
        test_bluetoothOn();
        while(errorText == "" || errorText.equals(RESULT_HEADSET_ONLY) || errorText.equals(RESULT_SPEAKER_ONLY))
        {
            //wait
        }
        assertEquals("Check Bluetooth Devices Off Error Code", RESULT_NO_DEVICES, errorText);
        errorText = "";
    }




}
