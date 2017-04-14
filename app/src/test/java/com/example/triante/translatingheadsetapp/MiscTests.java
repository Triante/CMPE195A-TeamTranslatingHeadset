package com.example.triante.translatingheadsetapp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */

public class MiscTests {
    private AmplitudeAverageCalculator calculator;

    //calculator tests

    @Test
    public void test_calculatorCount() {
        calculator = new AmplitudeAverageCalculator();
        boolean noCount = calculator.countAboveOne();
        calculator.addAmpValue(10);
        boolean lessThanTwo = calculator.countAboveOne();
        calculator.addAmpValue(10);
        boolean greaterThanOne = calculator.countAboveOne();

        assertFalse("No values added count test", noCount);
        assertFalse("Only one value added count test", lessThanTwo);
        assertTrue("Moew than one value added", greaterThanOne);
    }

    @Test
    public void test_calculatorAverage() {
        calculator = new AmplitudeAverageCalculator();
        calculator.addAmpValue(100);
        calculator.addAmpValue(90);
        calculator.addAmpValue(150);
        calculator.addAmpValue(130);
        double average = calculator.getAverageAmp();
        assertEquals("Calculating Average Test", average, 117.5, 0.0);
    }

    @Test
    public void test_calculatorReset() {
        calculator = new AmplitudeAverageCalculator();
        int temp;
        for (int a = 0; a < 50; a++) {
            temp = (a * 7)+((a + 3) / 2);
            calculator.addAmpValue(temp);
        }
        calculator.resetAmpVariables();
        assertEquals("Reset calculator default average test", calculator.getAverageAmp(), 0, 0);
        assertFalse("Reset calculator default count less than one", calculator.countAboveOne());
    }

    @Test
    public void test_transcript_speech() {
        String speech1 = "This is a sentence";
        String speech2 = "This is a different sentence";
        String speech3 = "Esta esta en espanol";
        String speech4 = "Kono wa nihongo desu";
        int user = 0;
        Transcript transcript1 = new Transcript(speech1, user);
        Transcript transcript2= new Transcript(speech2, user);
        Transcript transcript3 = new Transcript(speech3, user);
        Transcript transcript4 = new Transcript(speech4, user);
        assertEquals("Correct transcript stored string", speech1, transcript1.getSpeech());
        assertEquals("Correct transcript stored string", speech2, transcript2.getSpeech());
        assertEquals("Correct transcript stored string", speech3, transcript3.getSpeech());
        assertEquals("Correct transcript stored string", speech4, transcript4.getSpeech());
    }

    @Test
    public void test_transcript_user() {
        int user1 = 1;
        int user2 = 1;
        int user3 = 0;
        int user4 = 1;
        Transcript transcript1 = new Transcript("", user1);
        Transcript transcript2 = new Transcript("", user2);
        Transcript transcript3 = new Transcript("", user3);
        Transcript transcript4 = new Transcript("", user4);
        assertEquals("Correct transcript stored user", user1, transcript1.getUser(), 0.0);
        assertEquals("Correct transcript stored user", user2, transcript2.getUser(), 0.0);
        assertEquals("Correct transcript stored user", user3, transcript3.getUser(), 0.0);
        assertEquals("Correct transcript stored user", user4, transcript4.getUser(), 0.0);

    }
}
