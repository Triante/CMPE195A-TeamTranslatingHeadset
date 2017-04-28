package com.example.triante.translatingheadsetapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author by Jorge Aguiniga on 2/24/2017.
 */
 public class AmplitudeAverageCalculator implements Comparator<Integer> {
    private ArrayList<Integer> values = new ArrayList<>();
    private double maxAmp = 0;

    public synchronized void addAmpValue(double amp) {
        if (amp > maxAmp) maxAmp = amp;
        if (amp < 1000) return;
        int add = (int) amp;
        values.add(add);
    }

    public int getCount() {
        return values.size();
    }


    public synchronized double getAverageAmp() {
        if (!countAboveOne()) return 0;
        return convertAverageAmp();
    }

    public synchronized void resetAmpVariables() {
        values = new ArrayList<>();
        maxAmp = 0;
    }

    public boolean countAboveOne() {

        return (values.size() > 1);
    }

    private double convertAverageAmp() {
        double total = 0;
        int amount = 0;
        double mean = getMean();
        double minAllowed = mean - (mean * 0.3);
        for (int value: values) {
            if (minAllowed <= value) {
                total += value;
                amount++;
            }
        }
        if (amount == 0) {
            return 0;
        }
        return total / amount;
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
