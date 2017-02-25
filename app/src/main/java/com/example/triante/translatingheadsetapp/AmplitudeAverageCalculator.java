package com.example.triante.translatingheadsetapp;

/**
 * Created by Jorge Aguiniga on 2/24/2017.
 */

public class AmplitudeAverageCalculator {
    private double averageAmp = 0;
    private double currentAmp = 0;
    private int ampCount = 0;

    private double maxAmp = 0;


    public synchronized void convertAverageAmp(double amp) {
        ampCount++;
        currentAmp += amp;
        averageAmp = currentAmp / ampCount;
        if (amp > maxAmp) maxAmp = amp;
    }

    public synchronized double getAverageAmp() {
        return averageAmp;
    }

    public synchronized void resetAmpVariables() {
        averageAmp = 0;
        currentAmp = 0;
        ampCount = 0;
        maxAmp = 0;
    }

    public boolean countAboveOne() {
        if (ampCount > 1) return true;
        return false;
    }
}
