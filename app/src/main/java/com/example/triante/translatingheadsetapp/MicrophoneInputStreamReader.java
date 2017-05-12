package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.android.library.audio.AudioConsumer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jorge Aguiniga on 2/24/2017.
 * Custom InputStream class to read from a MultipleMicrophoneInputStream class
 */

public class MicrophoneInputStreamReader extends InputStream {

    private int READER_ID;
    private MultipleMicrophoneInputStream source;
    private boolean blocked = false;

    /**
     * Constructor for the MicrophoneInputStreamReader, gets assigned an reader ID based on the MultipleMicrophoneInputStream passed.
     * @param stream MultipleMicrophoneInputStream to read from
     */
    public MicrophoneInputStreamReader(MultipleMicrophoneInputStream stream) {
        source = stream;
        READER_ID = source.assignID();
    }

    /**
     * Reads a single byte from the provided MultipleMicrophoneInputStream
     * @return the read byte, -1 if at end of stream
     * @throws IOException
     */
    @Override
    public int read() throws IOException {
        int data = source.read();
        if (blocked) return 0;
        return data;
    }

    /**
     * Reads the bytes from provided MultipleMicrophoneInputStream and stores them in the byte array buffer
     * @return the number of bytes actually read or -1 if the end of the stream
     * @throws IOException
     */
    @Override
    public int read(byte[] buffer) throws IOException {
        int data = source.read(READER_ID, buffer);
        if (blocked) return 0;
        return data;
    }

    /**
     * Reads the bytes from byteOffset to byteCount from provided MultipleMicrophoneInputStream and stores them in the byte array buffer
     * @return the number of bytes actually read or -1 if the end of the stream
     * @throws IOException
     */
    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int data = source.read(READER_ID, buffer, byteOffset, byteCount);
        if (blocked) return 0;
        return data;
    }

    /**
     * Safely closes the input stream reader
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
    }

    /**
     * Sets the block flag which allows the reader to read or not
     * @param status true if this reader is blocked from reading the MultipleMicrophoneInputStream,
     *               false if allowed to read (block not set)
     */
    public void setBlockStatus(boolean status) {
        blocked = status;
    }

    /**
     * Gets the reader ID which was given by the MultipleMicrophoneInputStream
     * @return the stream reader's ID
     */
    public int getReaderID() {
        return READER_ID;
    }


}
