package com.example.triante.translatingheadsetapp.AndroidJUnit;

        import android.support.test.InstrumentationRegistry;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.util.Log;
        import android.widget.TextView;

        import com.example.triante.translatingheadsetapp.DemoActivity;
        import com.example.triante.translatingheadsetapp.IBMSpeechToText;
        import com.example.triante.translatingheadsetapp.IBMTextToSpeech;
        import com.example.triante.translatingheadsetapp.LanguageSettings;
        import com.example.triante.translatingheadsetapp.R;
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
 *      uses in: test_transcribeSpeechFromUser() and test_transcribeSpeechFromParty()
 * This test case contains the test for profanity filter enabled (on)
 */

@RunWith(AndroidJUnit4.class)
public class IBMSpeechToTextPartyTests {

    DemoActivity act;
    private IBMSpeechToText s2t;

    @Rule
    public ActivityTestRule<DemoActivity> mActivityRule = new ActivityTestRule<>(
            DemoActivity.class);

    @Before
    public void initiateIBMComponent() {
        act = mActivityRule.getActivity();
        TranslaTaSettings.setProfanityFilter(true);
        TranslaTaSettings.setAmplitudeThreshold(100000000);
        LanguageSettings.setLanguage(true, LanguageSettings.Language.JAPANESE_EMI);
        LanguageSettings.setLanguage(false, LanguageSettings.Language.ENGLISH_US_ALLISON);
        s2t = new IBMSpeechToText(act);
        s2t.record();
    }

    @After
    public void turnOffSpeechToText() throws IOException {
        s2t.end();
    }
    //IBM Speech Recognition Party Tests

    @Test
    public void test_profanityFilterEnabled() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
        }
        //say 'Fuck you'
        String expectedString = "you are a ****";
        String toBeTested = s2t.speech().getSpeech();
        assertEquals("\"Fuck you\" speech recognition test with profanity filter enabled", toBeTested.trim().toLowerCase(), expectedString);
    }

    @Test
    public void test_captureSpeechIdentifyParty() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
        }
        //say 'How Are you' with english set as party
        int userExpected = 1;
        int userToBeTested = s2t.speech().getUser();
        assertEquals("Party chosen when amplitude is less than threshold", userToBeTested, userExpected, 0);
    }

    @Test
    public void test_transcribeSpeechFromParty() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
        }
        //say 'How Are you'
        String expectedString = "how are you";
        int expectedUser = 1;
        Transcript t = s2t.speech();
        String stringToBeTested = t.getSpeech();
        int intToBeTested = t.getUser();
        assertEquals("transcript is in user language when amplitude is greater than threshold", stringToBeTested.trim().toLowerCase(), expectedString);
        assertEquals("User should be user", intToBeTested, expectedUser, 0);
    }

    @Test
    public void test_onlyPartySpeechCaptured() throws InterruptedException, IOException {
        while (!s2t.isSpeechAvailable()) {
        }
        //say 'How Are you'
        Transcript t = s2t.speech();
        boolean shouldBeEmpty = s2t.isSpeechAvailable();
        int expectedUser = 1;
        int userToBeTested = t.getUser();
        assertEquals("User ID should be party (1)", userToBeTested, expectedUser, 0);
        assertFalse("Empty list to verify no other transcript created", shouldBeEmpty);
    }


}
