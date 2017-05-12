package com.example.triante.translatingheadsetapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author by Jorge Aguiniga on 2/24/2017.
 * Class for adding values into a list and finding the average, mode, and medium of the list of values.
 */
 public class AmplitudeAverageCalculator implements Comparator<Integer> {
    private ArrayList<Integer> values = new ArrayList<>();
    private double maxAmp = 0;
    private ArrayList<Integer> mode;
    private final int TO_MODE_WITH = 10000000;

    /**
     * Constructor for AmplitudeCalculatorClass. Initializes a new instance of the calculator.
     */
    public AmplitudeAverageCalculator() {
        initMode();
    }

    /**
     * Adds a new amplitude value into the calculator, rejects any values under 1000
     * @param amp the amplitude value to be added
     * @return returns the input value if added successfully, -1 if the value was rejected.
     */
    public synchronized double addAmpValue(double amp) {
        if (amp > maxAmp) maxAmp = amp;
        if (amp < 1000) return -1;
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

    /**
     * Retrieves the amount of values currently stored in the calculator
     * @return the amount of values in the calculator
     */
    public int getCount() {
        return values.size();
    }

    /**
     * Calculates the average value from the set of values stored in the calculator
     * @return the average value
     */
    public synchronized double getAverageAmp() {
        if (!countAboveOne()) return 0;
        return convertAverageAmp();
    }

    /**
     * Clears the calculator. All values stored in the calculator get removed.
     */
    public synchronized void resetAmpVariables() {
        values = new ArrayList<>();
        maxAmp = 0;
        initMode();
    }

    /**
     * Checks if there is more than one value added in the calculator
     * @return true if calculator has more than one value, false otherwise
     */
    public boolean countAboveOne() {

        return (values.size() > 1);
    }

    /**
     * Helper method to convert the average
     * @return the average value, 0 if there is no values in the calculator
     */
    private double convertAverageAmp() {
        double total = 0;
        int amount = 0;
        double mean = getMedium();
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

    /**
     * Calculates the statistical medium, the most middle value in the calculator
     * @return the medium value
     */
    public double getMedium() {
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

    /**
     * Calculates the statistical mode, the value that appears the most from the values in the calculator.
     * @return the mode value divided by 10000000
     */
    public int getMode() {
        int max = 0;
        for (int i = 0; i < mode.size(); i++) {
            if (max < mode.get(i)) {
                max = mode.get(i);
            }
        }
        return max;
    }

    /**
     * Calculates the the value that appears second to most from the list of values in the calculator.
     * @return the second to most mode value
     */
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

    /**
     * Prints out the values stored in the calculator and the mode results in two separate arrays
     * @return String made up of store values and mode results in arrays
     */
    public String print() {
        return values.toString() + "   " + mode.toString() +  "  done";
    }

    /**
     * Compares two values to determine the ordering of a list
     * @param lhs the first value
     * @param rhs the second value
     * @return returns -1 if lhs < rhs, 0 if they're the same, 1 if lhs > rhs
     */
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

    /**
     * Helper method to create and initialize the mode array with with size 10 and all 0's
     */
    private void initMode() {
        mode = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mode.add(0);
        }
    }
}
