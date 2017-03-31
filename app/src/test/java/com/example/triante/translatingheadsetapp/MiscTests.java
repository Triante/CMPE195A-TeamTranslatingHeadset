package com.example.triante.translatingheadsetapp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */

public class MiscTests {
    private AmplitudeAverageCalculator calculator;
    private Transcript transcript;

    @Test
    public void test_calculatorCount() {
        calculator = new AmplitudeAverageCalculator();
        boolean noCount = calculator.countAboveOne();
        calculator.convertAverageAmp(10);
        boolean lessThanTwo = calculator.countAboveOne();
        calculator.convertAverageAmp(10);
        boolean greaterThanOne = calculator.countAboveOne();

        assertFalse("No values added count test", noCount);
        assertFalse("Only one value added count test", lessThanTwo);
        assertTrue("Moew than one value added", greaterThanOne);
    }

    @Test
    public void test_calculatorAverage() {
        calculator = new AmplitudeAverageCalculator();
        calculator.convertAverageAmp(100);
        calculator.convertAverageAmp(90);
        calculator.convertAverageAmp(150);
        calculator.convertAverageAmp(130);
        double average = calculator.getAverageAmp();
        assertEquals("Calculating Average Test", average, 117.5, 0.0);
    }

    @Test
    public void test_calculatorReset() {
        calculator = new AmplitudeAverageCalculator();
        int temp;
        for (int a = 0; a < 50; a++) {
            temp = (a * 7)+((a + 3) / 2);
            calculator.convertAverageAmp(temp);
        }
        calculator.resetAmpVariables();
        assertEquals("Reset calculator default average test", calculator.getAverageAmp(), 0, 0);
        assertFalse("Reset calculator default count less than one", calculator.countAboveOne());
    }

    @Test
    public void test_transcript() {
        String speech = "This is a sentence";
        int user = 0;
        transcript = new Transcript(speech, user);
        assertEquals("Correct transcript stored string", speech, transcript.getSpeech());
        assertEquals("Correct transcript stored user", user, transcript.getUser(), 0);
    }
}
