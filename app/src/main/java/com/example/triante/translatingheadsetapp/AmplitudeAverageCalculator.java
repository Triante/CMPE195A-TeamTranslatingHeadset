package com.example.triante.translatingheadsetapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Jorge Aguiniga on 2/24/2017.
 */

public class AmplitudeAverageCalculator implements Comparator<Integer> {
    ArrayList<Integer> values = new ArrayList<>();
    private double maxAmp = 0;

    public synchronized void addAmpValue(double amp) {
        if (amp > maxAmp) maxAmp = amp;
        int add = (int) amp;
        values.add(add);
    }


    public synchronized double getAverageAmp() {
        double ave = convertAverageAmp();
        return ave;
    }

    public synchronized void resetAmpVariables() {
        values = new ArrayList<>();
        maxAmp = 0;
    }

    public boolean countAboveOne() {
        if (values.size() > 1) return true;
        return false;
    }

    private double convertAverageAmp() {
        int total = 0;
        int amount = 0;
        double mean = getMean();
        double maxAllowed = mean + (mean * 0.3);
        double minAllowed = mean - (mean * 0.3);
        for (int value: values) {
            if (minAllowed <= value && value <= maxAllowed) {
                total += value;
                amount++;
            }
        }
        int average = total / amount;
        return average;
    }

    private double getMean() {
        Collections.sort(values, this);
        int middle = values.size() / 2;
        if (middle%2 == 1) {
            return values.get(middle);
        }
        else {
            return (values.get(middle) + values.get(middle - 1))/2;
        }
    }

    @Override
    public int compare(Integer lhs, Integer rhs) {
        if (lhs < rhs) {
            return -1;
        }
        else if (lhs > rhs) {
            return 1;
        }
        return 0;
    }
}
