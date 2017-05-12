package com.example.triante.translatingheadsetapp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ibm.watson.developer_cloud.android.library.audio.AmplitudeListener;
import com.ibm.watson.developer_cloud.android.library.audio.AudioConsumer;
import com.ibm.watson.developer_cloud.android.library.audio.opus.OggOpusEnc;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;

/**
 * Modified by Jorge Aguiniga on 2/14/2017.
 *
 * Based and modified from MicrophoneInputStream.java from IBM Watson Developer Cloud Android-SDK
 * to support being able to handle multiple objects to read from the microphone rather than one based
 * on the original source code provided in the IBM Watson Develop Cloud Android-SDK API.
 * https://github.com/watson-developer-cloud/android-sdk/blob/master/library/src/main/java/com/ibm/watson/developer_cloud/android/library/audio/MicrophoneInputStream.java
 * Taken on 2/24/2017
 *
 * Copyright IBM Corporation 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific languageSettings governing permissions and
 * limitations under the License.
 */

public final class MultipleMicrophoneInputStream extends InputStream implements AudioConsumer {

    private static final String TAG = MultipleMicrophoneInputStream.class.getName();

    public final ContentType CONTENT_TYPE;
    private final MicrophoneCaptureThread captureThread;
    private final PipedOutputStream[] os;
    private final PipedInputStream[] is;
    private AmplitudeListener amplitudeListener;
    private boolean hasAmplitudeLister = false;
    private int currentUnusedID = 0;

    /**
     * Constructor for MultipleMicrophoneInputStream
     * @param amount the amount of MicrophoneInputStreamReaders that are going to read from this stream
     */
    public MultipleMicrophoneInputStream(int amount) {
        captureThread = new MicrophoneCaptureThread(this, false);
        CONTENT_TYPE = ContentType.RAW;
        is = new PipedInputStream[amount];
        os = new PipedOutputStream[amount];
        for (int i = 0; i < amount; i++) {
            os[i] = new PipedOutputStream();
            is[i] = new PipedInputStream();
            try {
                is[i].connect(os[i]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Constructor for MultipleMicrophoneInputStream
     * @param amount the amount of MicrophoneInputStreamReaders that are going to read from this stream
     * @param opusEncoded true if the audio will be Opus encoded, false otherwise
     */
    public MultipleMicrophoneInputStream(int amount, boolean opusEncoded) {
        captureThread = new MicrophoneCaptureThread(this, opusEncoded);
        if (opusEncoded) {
            CONTENT_TYPE = ContentType.OPUS;
        }
        else {
            CONTENT_TYPE = ContentType.RAW;
        }
        is = new PipedInputStream[amount];
        os = new PipedOutputStream[amount];
        for (int i = 0; i < amount; i++) {
            os[i] = new PipedOutputStream();
            is[i] = new PipedInputStream();
            try {
                is[i].connect(os[i]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Starts the audio capture thread
     */
    public void startRecording() {
        captureThread.start();
    }

    /**
     * Not Supported, Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)
     * @return UnsupportedOperationException
     * @throws UnsupportedOperationException
     */
    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)");
    }

    /**
     * Not Supported, Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)
     * @return UnsupportedOperationException
     * @throws UnsupportedOperationException
     */
    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        throw new UnsupportedOperationException("Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)");
    }

    /**
     * Reads the bytes from byteOffset to byteCount from captured audio and stores them in the byte array buffer
     * @param readerID the MicrophoneInputStreamReader's ID to read from the correct buffer
     * @return the number of bytes actually read or -1 if the end of the stream
     * @throws IOException
     */
    public int read(int readerID, byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return is[readerID].read(buffer, byteOffset, byteCount);
    }

    /**
     * Not Supported, Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)
     * @return UnsupportedOperationException
     * @throws UnsupportedOperationException
     */
    @Override
    public int read(byte[] buffer) throws IOException {
        throw new UnsupportedOperationException("Call read(int readerID, byte[]) or read(int readerID, byte[] buffer, int byteOffset, int byteCount)");
        //return read(buffer, 0, buffer.length);
    }

    /**
     * Reads the bytes from captured audio and stores them in the byte array buffer
     * @param readerID the MicrophoneInputStreamReader's ID to read from the correct buffer
     * @return the number of bytes actually read or -1 if the end of the stream
     * @throws IOException
     */
    public int read(int readerID, byte[] buffer) throws IOException {
        return read(readerID, buffer, 0, buffer.length);
    }

    /**
     * Ends the audio capture thread and safely closes the input stream
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        captureThread.end();
        for (int i = 0; i < os.length; i++) {
            is[i].close();
            os[i].close();
        }
    }

    /**
     * Consumes the data from the audio capture and adds it to different buffers to allow the MicrophoneInputStreamReaders
     * to read from one audio capture thread
     * @param data the data to be consumed
     * @param amplitude the amplitude of the audio data
     * @param volume the volume of the audio data
     */
    @Override
    public void consume(byte[] data, double amplitude, double volume) {
        try {
            for (int i = 0; i < os.length; i++) {
                os[i].write(data);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Consumes the data from the audio capture and adds it to different buffers to allow the MicrophoneInputStreamReaders
     * to read from one audio capture thread
     * @param data the data to be consumed
     */
    @Override
    public void consume(byte[] data) {
        try {
            for (int i = 0; i < os.length; i++) {
                os[i].write(data);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Set the amplitude listener to allow other classes to handle the amplitude and volume data
     * @param listener the amplitude listener
     */
    public void setOnAmplitudeListener(AmplitudeListener listener) {
        amplitudeListener = listener;
        hasAmplitudeLister = true;
    }

    /**
     * Returns the next ID available to be assigned for a MicrophoneInputStreamReaders allowing it
     * to read from a specified buffer in the MultipleMicrophoneInputStream. The amount of IDs issued
     * are equal to the amount specified when MultipleMicrophoneInputStream was initialized.
     * @return the next integer ID available to read from a buffer
     * @throws Exception If a new ID is attempted to be assigned after all have been used, an exception will be thrown
     */
    public int assignID() {
        try {
            if (currentUnusedID == os.length) {
                throw new Exception("An extra MicrophoneInputStreamReader was assigned over the limit assigned: " + os.length);
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        int id = currentUnusedID;
        currentUnusedID++;
        return id;
    }

    /**
     * Based and modified from MicrophoneCaptureThread.java from IBM Watson Developer Cloud Android-SDK
     * to support being able to handle multiples objects to read from the microphone rather than one based
     * on the original source code provided in the IBM Watson Develop Cloud Android-SDK API.
     * https://github.com/watson-developer-cloud/android-sdk/blob/master/library/src/main/java/com/ibm/watson/developer_cloud/android/library/audio/MicrophoneCaptureThread.java
     * Taken on 2/24/2017
     *
     * Copyright IBM Corporation 2016
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific languageSettings governing permissions and
     * limitations under the License.
     */
    private class MicrophoneCaptureThread extends Thread {
        private final String TAG = MicrophoneCaptureThread.class.getName();
        private static final int SAMPLE_RATE = 16000;

        private boolean opusEncoded;
        private OggOpusEnc encoder;

        private final AudioConsumer consumer;
        private boolean stop;

        /**
         * Constructor for the MicrophoneCaptureThread
         * @param consumer the object (MultipleMicrophoneInputStream) that will consume the audio data
         * @param opusEncoded true if the audio needs to be opusEncoded, false otherwise
         */
        public MicrophoneCaptureThread(AudioConsumer consumer, boolean opusEncoded) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
            this.consumer = consumer;
            this.opusEncoded = opusEncoded;
        }

        /**
         * Captures audio from the mobile devices microphone. The audio data is consumed by the
         * AudioConsumer object to be used outside of the audio capture thread. The audio amplitude and
         * volume data is also passed to the listener from the audio capture thread.
         */
        @Override
        public void run() {
            int bufferSize = Math.max(SAMPLE_RATE / 2, AudioRecord.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
            short[] buffer = new short[bufferSize]; // use short to hold 16-bit PCM encoding

            AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            record.startRecording();

            if (opusEncoded) {
                try {
                    encoder = new OggOpusEnc(consumer);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            while (!stop) {
                int r = record.read(buffer, 0, buffer.length);
                // calculate amplitude and volume
                long v = 0;
                for (int i = 0; i < r; i++) {
                    v += buffer[i] * buffer[i];
                }
                double amplitude = v / (double) r;
                double volume = 0;
                if (amplitude > 0) {
                    volume = 10 * Math.log10(amplitude);
                }
                if (hasAmplitudeLister) {
                    amplitudeListener.onSample(amplitude, volume);
                }
                // convert short buffer to bytes
                ByteBuffer bufferBytes = ByteBuffer.allocate(r * 2); //2 bytes per short
                bufferBytes.order(ByteOrder.LITTLE_ENDIAN); //save little-endian byte from short buffer
                bufferBytes.asShortBuffer().put(buffer, 0, r);
                byte[] bytes = bufferBytes.array();

                if (opusEncoded) {
                    try {
                        encoder.onStart(); //must be called before writing
                        encoder.encodeAndWrite(bytes);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    consumer.consume(bytes, amplitude, volume);
                }
            }
            if (encoder != null) {
                encoder.close();
            }
            record.stop();
            record.release();
        }
        /**
         * Gracefully stops recording microphone data. Make sure this is called when data no longer needs
         * to be collected to ensure this thread and its resources are properly cleaned up.
         */
        public void end() {
            stop = true;
            while (!stop) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }
}
