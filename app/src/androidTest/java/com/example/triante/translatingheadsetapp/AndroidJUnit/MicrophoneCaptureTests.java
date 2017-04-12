package com.example.triante.translatingheadsetapp.AndroidJUnit;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.triante.translatingheadsetapp.DemoActivity;
import com.example.triante.translatingheadsetapp.MicrophoneInputStreamReader;
import com.example.triante.translatingheadsetapp.MultipleMicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.AmplitudeListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Jorge Aguiniga on 3/31/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MicrophoneCaptureTests {

    private boolean listerWorks;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Test
    public void test_amplitudeListener() throws InterruptedException {
        listerWorks = false;
        AmplitudeListener listener = new AmplitudeListener() {
            @Override
            public void onSample(double amplitude, double volume) {
                if (amplitude > 0 && volume > 0) {
                    listerWorks = true;
                }
            }
        };
        MultipleMicrophoneInputStream stream = new MultipleMicrophoneInputStream(0);
        stream.setOnAmplitudeListener(listener);
        stream.startRecording();
        //make noise
        Thread.sleep(5000);
        assertTrue("Amplitude listener recognized sound", listerWorks);
    }

    @Test
    public void test_MicrophoneInputStreamReader() throws IOException {
        MultipleMicrophoneInputStream stream = new MultipleMicrophoneInputStream(1);
        MicrophoneInputStreamReader reader1 = new MicrophoneInputStreamReader(stream);
        byte[] buffer = new byte[4096];
        int bufferSize = Math.max(16000 / 2, AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        short[] buffer2 = new short[bufferSize];
        int r = 50;
        ByteBuffer bufferBytes = ByteBuffer.allocate(r * 2); //2 bytes per short
        bufferBytes.order(ByteOrder.LITTLE_ENDIAN); //save little-endian byte from short buffer
        bufferBytes.asShortBuffer().put(buffer2, 0, r);
        byte[] data = bufferBytes.array();
        stream.consume(data, 20, 20);
        int rData1 = reader1.read(buffer);
        stream.close();

        boolean sameData = false;
        if (rData1 == r) {
            sameData = true;
        }
        assertTrue("Correct data read from input stream", sameData);
    }

    @Test
    public void test_MultipleMicrophoneInputStreamDifferentIDsForEachMicrophoneInputStreamReader() throws IOException {
        MultipleMicrophoneInputStream stream = new MultipleMicrophoneInputStream(5);
        MicrophoneInputStreamReader reader1 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader2 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader3 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader4 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader5 = new MicrophoneInputStreamReader(stream);

        int reader1ID = reader1.getReaderID();
        int reader2ID = reader2.getReaderID();
        int reader3ID = reader3.getReaderID();
        int reader4ID = reader4.getReaderID();
        int reader5ID = reader5.getReaderID();
        stream.close();

        assertEquals("Reader1 ID should be 0", reader1ID, 0, 0);
        assertEquals("Reader2 ID should be 1", reader2ID, 1, 0);
        assertEquals("Reader3 ID should be 2", reader3ID, 2, 0);
        assertEquals("Reader4 ID should be 3", reader4ID, 3, 0);
        assertEquals("Reader5 ID should be 4", reader5ID, 4, 0);
    }

    @Test
    public void test_MultipleMicrophoneInputStreamSplitsCorrectlyToEachMicrophoneInputStreamReader() throws IOException {
        MultipleMicrophoneInputStream stream = new MultipleMicrophoneInputStream(5);
        MicrophoneInputStreamReader reader1 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader2 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader3 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader4 = new MicrophoneInputStreamReader(stream);
        MicrophoneInputStreamReader reader5 = new MicrophoneInputStreamReader(stream);

        byte[] buffer = new byte[4096];
        int bufferSize = Math.max(16000 / 2, AudioRecord.getMinBufferSize(16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        short[] buffer2 = new short[bufferSize];
        int r = 50;
        ByteBuffer bufferBytes = ByteBuffer.allocate(r * 2); //2 bytes per short
        bufferBytes.order(ByteOrder.LITTLE_ENDIAN); //save little-endian byte from short buffer
        bufferBytes.asShortBuffer().put(buffer2, 0, r);
        byte[] data = bufferBytes.array();
        stream.consume(data, 20, 20);
        int rData1 = reader1.read(buffer);
        int rData2 = reader2.read(buffer);
        int rData3 = reader3.read(buffer);
        int rData4 = reader4.read(buffer);
        int rData5 = reader5.read(buffer);
        stream.close();

        boolean allEqual = false;
        if (rData1 == rData2 && rData2 == rData3 && rData3 == rData4 && rData4 == rData5) allEqual = true;

        assertTrue("All Readers read the same data", allEqual);
    }

}
