package com.example.triante.translatingheadsetapp.UITests;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.triante.translatingheadsetapp.MainActivity;
import com.example.triante.translatingheadsetapp.R;
import com.example.triante.translatingheadsetapp.TranslaTaSettings;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Created by Jorge Aguiniga on 4/23/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AmplitudeSettingsActivityUITests {

    private final int MAX_THRESHOLD = 100000000;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private void navigateToAmplitudeSettings() {
        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.bAmplitude_settings)).perform(click());
    }

    private void setSaveDefaults() {
        TranslaTaSettings.setAmplitudeThreshold(MAX_THRESHOLD/10);
        TranslaTaSettings.setMaxAmplitude(MAX_THRESHOLD/5);
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity());
    }

    public static ViewAction setProgress(final int progress) {
        return new GeneralClickAction(Tap.SINGLE, new CoordinatesProvider() {
            @Override
            public float[] calculateCoordinates(View view) {
                SeekBar bar = (SeekBar) view;
                final int[] screenPos = new int[2];
                bar.getLocationOnScreen(screenPos);

                int trueWidth = bar.getWidth() - bar.getPaddingLeft() - bar.getPaddingRight();
                float relativePos = (0.3f + progress)/ (float) bar.getMax();
                if (relativePos > 1.0f) {
                    relativePos = 1.0f;
                }
                final float screenX = trueWidth*relativePos + screenPos[0] + bar.getPaddingLeft();
                final float screenY = bar.getHeight()/2f + screenPos[1];
                float[] coor = {screenX, screenY};
                return coor;
            }
        }, Press.FINGER);
    }

    public static Matcher<View> isProgress(final int progress) {
        return new BaseMatcher<View>() {
            private int actualProgress;

            @Override
            public boolean matches(Object item) {
                SeekBar bar = (SeekBar) item;
                actualProgress  = bar.getProgress();
                if (progress - progress*.05 < actualProgress && actualProgress< progress + progress*.05) return true;
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Progress not within 5% distance of expected Progress: " + progress + ", but was: " + actualProgress);
            }
        };
    }

    public static Matcher<View> isProgressBarMax(final int value) {
        return new BaseMatcher<View>() {
            private int actualProgress;

            @Override
            public boolean matches(Object item) {
                ProgressBar bar = (ProgressBar) item;
                actualProgress  = bar.getMax();
                if (value - value*.05 < actualProgress && actualProgress< value + value*.05) return true;
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Max not within 5% distance of expected Max: " + value + ", but was: " + actualProgress);
            }
        };
    }

    public static Matcher<View> isThresholdProgress(final int progress) {
        return new BaseMatcher<View>() {
            private int actualProgress;

            @Override
            public boolean matches(Object item) {
                ProgressBar bar = (ProgressBar) item;
                actualProgress  = bar.getSecondaryProgress();
                if (progress - progress*.05 < actualProgress && actualProgress< progress + progress*.05) return true;
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Secondary progress not within 5% distance of expected Progress: " + progress + ", but was: " + actualProgress);
            }
        };
    }

    public static Matcher<View> withIntegerAsText(final int value) {
        return new BaseMatcher<View>() {
            private int storedValAsInt;
            @Override
            public boolean matches(Object item) {
                TextView textView = (TextView) item;
                String storedValAsString = textView.getText().toString();
                storedValAsInt = Integer.parseInt(storedValAsString);
                if (value - value*.05 < storedValAsInt && storedValAsInt< value + value*.05) return true;
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Value not within 5% distance of expected Value: " + value + ", but was: " + storedValAsInt);
            }
        };
    }

    @Test
    public void test_correctLayout() {
        TranslaTaSettings.setAmplitudeThreshold(MAX_THRESHOLD/10);
        TranslaTaSettings.setMaxAmplitude(MAX_THRESHOLD/5);
        TranslaTaSettings.saveAmplitudeSettings(mActivityRule.getActivity());
        navigateToAmplitudeSettings();
        onView(withId(R.id.amplitudeSettingsTitleText)).check(matches(isDisplayed()));
        onView(withId(R.id.amplitudeSettingsActivityDescriptionText)).check(matches(isDisplayed()));
        onView(withId(R.id.amplitudeSettingsActivitySplitter)).check(matches(isDisplayed()));
        onView(withId(R.id.bRecordAmplitude)).check(matches(isDisplayed()));
        onView(withId(R.id.bRecordAmplitudeSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.bConfigAmplitude)).check(matches(isDisplayed()));
        onView(withId(R.id.bConfigAmplitudeSubText)).check(matches(isDisplayed()));
        onView(withId(R.id.imageMic)).check(matches(isDisplayed()));
        onView(withId(R.id.thresholdTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.text_current)).check(matches(isDisplayed()));
        onView(withId(R.id.maxTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.text_max)).check(matches(isDisplayed()));
        onView(withId(R.id.seekBar_threshold)).check(matches(isDisplayed()));
        onView(withId(R.id.seekBar_max)).check(matches(isDisplayed()));
        onView(withId(R.id.amplitude_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.bDoneAmplitude)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bDoneAmplitudeSubText)).check(matches(not(isDisplayed())));
        int max = MAX_THRESHOLD/2;
        int threshold = max/2;
        onView(withId(R.id.seekBar_max)).perform(setProgress(max));
        onView(withId(R.id.seekBar_threshold)).perform(setProgress(threshold));
        onView(withId(R.id.text_current)).check(matches(not(withText("" + threshold))));
        onView(withId(R.id.text_max)).check(matches(not(withText("" + max))));
    }

    @Test
    public void test_RecordAmplitude() {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_title)).check(matches(isDisplayed()));
        onView(withText(R.string.amplitude_dialog_message)).check(matches(isDisplayed()));
        onView(withText(R.string.amplitude_dialog_confirm)).check(matches(isDisplayed()));
        onView(withText(R.string.dialog_cancel)).check(matches(isDisplayed()));
    }

    @Test
    public void test_ConfigDialogCancel() {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.dialog_cancel)).perform(click());
        onView(withText(R.string.amplitude_dialog_title)).check(doesNotExist());
        onView(withText(R.string.amplitude_dialog_message)).check(doesNotExist());
        onView(withText(R.string.amplitude_dialog_confirm)).check(doesNotExist());
        onView(withText(R.string.dialog_cancel)).check(doesNotExist());
    }

    @Test
    public void test_ConfigDialogRecord() {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_confirm)).perform(click());
        onView(withText(R.string.amplitude_dialog_title)).check(doesNotExist());
        onView(withText(R.string.recording_dialog_title)).check(matches(isDisplayed()));
        onView(withText(R.string.recording_dialog_message)).check(matches(isDisplayed()));
    }

    @Test
    public void test_ConfigDialogRecordNonCancelable() {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_confirm)).perform(click());
        onView(withText(R.string.dialog_cancel)).check(doesNotExist());
        pressBack();
        onView(withText(R.string.recording_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void test_CompleteRecordDialog() throws InterruptedException {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_confirm)).perform(click());
        Thread.sleep(15000);
        onView(withText(R.string.finish_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void test_CompleteRecordDialogNonCancelable() throws InterruptedException {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_confirm)).perform(click());
        Thread.sleep(12000);
        onView(withText(R.string.dialog_cancel)).check(doesNotExist());
        pressBack();
        onView(withText(R.string.finish_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void test_CompleteRecordDialogOK() throws InterruptedException {
        navigateToAmplitudeSettings();
        onView(withId(R.id.bRecordAmplitude)).perform(click());
        onView(withText(R.string.amplitude_dialog_confirm)).perform(click());
        Thread.sleep(15000);
        onView(withText(R.string.dialog_confirm)).perform(click());
        onView(withText(R.string.finish_dialog_title)).check(doesNotExist());
    }

    @Test
    public void test_ConfigAmplitudeButtonAction() {
        setSaveDefaults();
        int max = MAX_THRESHOLD/2;
        int threshold = max/2;
        navigateToAmplitudeSettings();
        onView(withId(R.id.bConfigAmplitude)).perform(click());
        onView(withId(R.id.bRecordAmplitude)).check(matches(not(isClickable())));
        onView(withId(R.id.bConfigAmplitude)).check(matches(not(isClickable())));
        onView(withId(R.id.bDoneAmplitude)).check(matches(isDisplayed()));
        onView(withId(R.id.bDoneAmplitude)).check(matches(isClickable()));
        onView(withId(R.id.seekBar_max)).perform(setProgress(max));
        onView(withId(R.id.seekBar_threshold)).perform(setProgress(threshold));
        onView(withId(R.id.seekBar_max)).check(matches(isProgress(max)));
        onView(withId(R.id.seekBar_threshold)).check(matches(isProgress(threshold)));
    }

    @Test
    public void test_DoneButtonAction() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        onView(withId(R.id.bConfigAmplitude)).perform(click());
        int max = MAX_THRESHOLD/3;
        int threshold = max/3;
        onView(withId(R.id.seekBar_max)).perform(setProgress(max));
        onView(withId(R.id.seekBar_threshold)).perform(setProgress(threshold));
        int currentSavedThreshold = TranslaTaSettings.getThresholdAmplitude();
        int currentSavedMax = TranslaTaSettings.getMaxAmplitude();
        assertEquals("Threshold should not be overwritten yet", MAX_THRESHOLD/10, currentSavedThreshold, 0);
        assertEquals("Max should not be overwritten yet", MAX_THRESHOLD/5, currentSavedMax, 0);
        onView(withId(R.id.bDoneAmplitude)).perform(click());
        int deltaT = (int) (threshold - threshold*.5);
        int deltaM = (int) (max - max*.5);
        assertEquals("Threshold saved new value", threshold, TranslaTaSettings.getThresholdAmplitude(), deltaT);
        assertEquals("Max saved new value", max, TranslaTaSettings.getMaxAmplitude(), deltaM);
        onView(withId(R.id.bRecordAmplitude)).check(matches(isDisplayed()));
        onView(withId(R.id.bConfigAmplitude)).check(matches(isDisplayed()));
        onView(withId(R.id.bDoneAmplitude)).check(matches(not(isDisplayed())));
        onView(withId(R.id.bDoneAmplitudeSubText)).check(matches(not(isDisplayed())));
    }

    @Test
    public void test_SliderAndLabelInitializations() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        int threshold = MAX_THRESHOLD/10;
        int newMax = 2 * ((int) (threshold / 1E7) + 1);
        newMax = (int) (newMax * 1E7);
        if (newMax > MAX_THRESHOLD) {
            newMax = MAX_THRESHOLD;
        }
        onView(withId(R.id.seekBar_max)).check(matches(isProgress(newMax)));
        onView(withId(R.id.seekBar_threshold)).check(matches(isProgress(threshold)));
        onView(withId(R.id.text_max)).check(matches(withIntegerAsText(newMax)));
        onView(withId(R.id.text_current)).check(matches(withIntegerAsText(threshold)));
    }

    @Test
    public void test_SliderAndLabelUpdates() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        onView(withId(R.id.bConfigAmplitude)).perform(click());
        int max1 = MAX_THRESHOLD/2;
        int thr1 = max1/3;
        onView(withId(R.id.seekBar_max)).perform(setProgress((int) max1));
        onView(withId(R.id.seekBar_threshold)).perform(setProgress((int) thr1));
        onView(withId(R.id.seekBar_max)).check(matches(isProgress((int) max1)));
        onView(withId(R.id.seekBar_threshold)).check(matches(isProgress((int) thr1)));
        onView(withId(R.id.text_max)).check(matches(withIntegerAsText((int) max1)));
        onView(withId(R.id.text_current)).check(matches(withIntegerAsText((int) thr1)));

        int max2 = MAX_THRESHOLD - (MAX_THRESHOLD/5);
        int thr2 = max2 - (max2/3);
        onView(withId(R.id.seekBar_max)).perform(setProgress((int) max2));
        onView(withId(R.id.seekBar_threshold)).perform(setProgress((int) thr2));
        onView(withId(R.id.seekBar_max)).check(matches(isProgress((int) max2)));
        onView(withId(R.id.seekBar_threshold)).check(matches(isProgress((int) thr2)));
        onView(withId(R.id.text_max)).check(matches(withIntegerAsText((int) max2)));
        onView(withId(R.id.text_current)).check(matches(withIntegerAsText((int) thr2)));

        onView(withId(R.id.seekBar_max)).check(matches(not(isProgress((int) max1))));
        onView(withId(R.id.seekBar_threshold)).check(matches(not(isProgress((int) thr1))));
        onView(withId(R.id.text_max)).check(matches(not(withIntegerAsText((int) max1))));
        onView(withId(R.id.text_current)).check(matches(not(withIntegerAsText((int) thr1))));
    }

    @Test
    public void test_ProgressBarInitialization() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        int threshold = MAX_THRESHOLD / 10;
        int newMax = 2 * ((int) (threshold / 1E7) + 1);
        newMax = (int) (newMax * 1E7);
        if (newMax > MAX_THRESHOLD) {
            newMax = MAX_THRESHOLD;
        }
        onView(withId(R.id.amplitude_bar)).check(matches(isThresholdProgress(threshold)));
        onView(withId(R.id.amplitude_bar)).check(matches(isProgressBarMax(newMax)));
    }

    @Test
    public void test_ProgressBarThresholdUpdate() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        onView(withId(R.id.bConfigAmplitude)).perform(click());
        int max1 = MAX_THRESHOLD/5;
        int thr1 = max1/3;
        onView(withId(R.id.seekBar_threshold)).perform(setProgress(thr1));
        onView(withId(R.id.amplitude_bar)).check(matches(isThresholdProgress(thr1)));
    }

    @Test
    public void test_ProgressBarMaxUpdate() {
        setSaveDefaults();
        navigateToAmplitudeSettings();
        onView(withId(R.id.bConfigAmplitude)).perform(click());
        int max = MAX_THRESHOLD - MAX_THRESHOLD/7;
        onView(withId(R.id.seekBar_max)).perform(setProgress(max));
        onView(withId(R.id.amplitude_bar)).check(matches(isProgressBarMax(max)));
    }

}
