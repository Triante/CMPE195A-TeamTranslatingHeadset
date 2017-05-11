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
    private ArrayList<Integer> mode;
    private final int TO_MODE_WITH = 10000000;

    public AmplitudeAverageCalculator() {
        initMode();
    }

    public synchronized double addAmpValue(double amp) {
        if (amp > maxAmp) maxAmp = amp;
        if (amp < 1000) return 0;
        int add = (int) amp;
        values.add(add);
        int temp = (int) amp;
        int i = temp/TO_MODE_WITH;
        if (i > 9) {
            i = 9;
        }
        int v = mode.get(i);
        v++;
        mode.set(i, v);
        return amp;
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
        initMode();
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

    public double getMean() {
        if (values.size() < 2) return 0;
        Collections.sort(values, this);
        int middle = values.size() / 2;
        if (middle%2 == 1) {
            return values.get(middle);
        }
        else {
            return (values.get(middle) + values.get(middle - 1))/2;
        }
    }

    public int getMode() {
        int max = 0;
        for (int i = 0; i < mode.size(); i++) {
            if (max < mode.get(i)) {
                max = mode.get(i);
            }
        }
        return max;
    }

    public int getModeNext() {
        int max = 0;
        int last = 0;
        for (int i = 0; i < mode.size(); i++) {
            if (max < mode.get(i)) {
                last = max;
                max = mode.get(i);
            }
        }
        return last;
    }

    public String print() {
        return values.toString() + "   " + mode.toString() +  "  done";
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

    private void initMode() {
        mode = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mode.add(0);
        }
    }
}
