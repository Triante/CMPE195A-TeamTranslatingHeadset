package com.example.triante.translatingheadsetapp.AndroidJUnit;

        import android.support.test.InstrumentationRegistry;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.util.Log;

        import com.example.triante.translatingheadsetapp.DemoActivity;
        import com.example.triante.translatingheadsetapp.IBMSpeechToText;
        import com.example.triante.translatingheadsetapp.IBMTextToSpeech;
        import com.example.triante.translatingheadsetapp.LanguageSettings;
        import com.example.triante.translatingheadsetapp.Transcript;
        import com.example.triante.translatingheadsetapp.TranslaTaSettings;
        import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

        import org.junit.After;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import static org.junit.Assert.*;

        import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class IBMTextToSpeechTests {

    DemoActivity act;
    private IBMTextToSpeech t2s;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateIBMComponent() {
        act = mActivityRule.getActivity();
        t2s = new IBMTextToSpeech(act);
    }

    //IBM Speech Synthesis Tests
    @Test
    public void test_speechSynthesis1() throws InterruptedException {
        String toSpeech = "This is speech converted by IBM";
        Voice voice = Voice.EN_MICHAEL;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: ENGLISH" +
                toSpeech, true);
    }

    @Test
    public void test_speechSynthesis2() throws InterruptedException {
        String toSpeech = "Con mi amigo";
        Voice voice = Voice.ES_ENRIQUE;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: SPANISH" +
                toSpeech, true);
    }

    @Test
    public void test_speechSynthesis3() throws InterruptedException {
        String toSpeech = "このはにほんごです";
        Voice voice = Voice.JA_EMI;
        t2s.synthesize(toSpeech, voice);
        Thread.sleep(10000);
        assertTrue("Audio cannot be JUnit tested. Verify manually that the following sentence was output: JAPANESE" +
                toSpeech, true);
    }

}
