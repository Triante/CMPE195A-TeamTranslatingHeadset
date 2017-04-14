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

/**
 * Created by Jorge Aguiniga on 3/30/2017.
 * For all speech not related to language, ENGLISH: "how are you"
 * For both cases related to language, SPANISH: "con mi amigo"
 * For
 *      uses in: test_transcribeSpeechFromUser() and test_transcribeSpeechFromParty()
 */

@RunWith(AndroidJUnit4.class)
public class IBMSpeechToTextGeneralTests {

    DemoActivity act;
    private IBMSpeechToText s2t;
    private int SPEAK_SLEEP_TIME = 15000;
    private int SHORT_RECORD_TIME = 500;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateIBMComponent() {
        act = mActivityRule.getActivity();
        s2t = new IBMSpeechToText(act);
        LanguageSettings.setLanguage(true, LanguageSettings.Language.ENGLISH_F);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_F);
        s2t.record();
    }

    @After
    public void turnOffSpeechToText() throws IOException {
        s2t.end();
    }
    //IBM Speech Recognition Tests
    @Test
    public void test_emptyTranscriptList() throws IOException, InterruptedException {
        Thread.sleep(SHORT_RECORD_TIME);
        boolean toBeFalse = s2t.isSpeechAvailable();
        assertFalse("Empty Transcript List Test", toBeFalse);
    }

    @Test
    public void test_nullTranscript() throws IOException, InterruptedException {
        Thread.sleep(SHORT_RECORD_TIME);
        Transcript t = s2t.speech();
        assertNull("Null Transcript Text when retrieving from an empty Transcript list test", t);
    }

    @Test
    public void test_captureSpeechTranscriptList() throws InterruptedException, IOException {
        Thread.sleep(SPEAK_SLEEP_TIME);
        //say 'How Are you'
        boolean toBeTrue = s2t.isSpeechAvailable();
        assertTrue("Transcript List with contents test", toBeTrue);
    }

    @Test
    public void test_captureSpeech() throws InterruptedException, IOException {
        Thread.sleep(SPEAK_SLEEP_TIME);
        //say 'How Are you'
        String expectedString = "how are you";
        String toBeTested = s2t.speech().getSpeech();
        assertEquals("\"How Are You\" speech recognition test", toBeTested.trim().toLowerCase(), expectedString);
    }

}
