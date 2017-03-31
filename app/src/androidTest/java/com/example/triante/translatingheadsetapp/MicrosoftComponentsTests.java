package com.example.triante.translatingheadsetapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 */


@RunWith(AndroidJUnit4.class)
public class MicrosoftComponentsTests {

    private MSAuthenticator authenticator;
    private MSTranslator translator;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateMSComponents() throws IOException {
        authenticator = new MSAuthenticator(mActivityRule.getActivity());
        translator = new MSTranslator(mActivityRule.getActivity());
    }

    @Test
    public void test_tokenExpired() throws IOException {
        authenticator = new MSAuthenticator(mActivityRule.getActivity());
        boolean toBeExpired = authenticator.isExpired();
        assertTrue("Expired AuthToken test", toBeExpired);
    }

    @Test
    public void test_tokenValid() throws IOException {
        authenticator.createAuthToken();
        boolean toBeValid = authenticator.isExpired();
        assertFalse("Valid AuthToken test", toBeValid);
    }

    @Test
    public void test_Translation() throws IOException {
        String english = "en";
        String spanish = "es";

        String e1 = "Hi my name is Jorge.";
        String e2 = "Want to go outside today?";
        String e3 = "What is greater than three?";

        String s1 = "Hola mi nombre es Jorge.";
        String s2 = "¿Quieres salir hoy?";
        String s3 = "¿Qué es mayor que tres?";

        String result1 = translator.translate(e1, english, spanish);
        String result2 = translator.translate(e2, english, spanish);
        String result3 = translator.translate(e3, english, spanish);

        assertEquals("Sentence one translated from english to spanish test", result1, s1);
        assertEquals("Sentence two translated from english to spanish test", result2, s2);
        assertEquals("Sentence three translated from english to spanish test", result3, s3);

    }

}
