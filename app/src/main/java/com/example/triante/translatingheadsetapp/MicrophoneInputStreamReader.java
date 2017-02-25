package com.example.triante.translatingheadsetapp;

import com.ibm.watson.developer_cloud.android.library.audio.AudioConsumer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jorge Aguiniga on 2/24/2017.
 */

public class MicrophoneInputStreamReader extends InputStream {

    private int READER_ID;
    private MultipleMicrophoneInputStream source;
    private boolean blocked = false;

    public MicrophoneInputStreamReader(MultipleMicrophoneInputStream stream) {
        source = stream;
        READER_ID = source.assignID();
    }

    @Override
    public int read() throws IOException {
        int data = source.read();
        if (blocked) return 0;
        return data;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        int data = source.read(READER_ID, buffer);
        if (blocked) return 0;
        return data;
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int data = source.read(READER_ID, buffer, byteOffset, byteCount);
        if (blocked) return 0;
        return data;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    public void setBlockStatus(boolean status) {
        blocked = status;
    }


}
