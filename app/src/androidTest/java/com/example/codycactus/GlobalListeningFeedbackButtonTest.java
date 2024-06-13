package com.example.codycactus;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.GlobalListeningFeedbackButton;
import com.example.GlobalSpeechState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParser;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GlobalListeningFeedbackButtonTest {

    private TestGlobalSpeechState testGlobalSpeechState;

    private TestGlobalSpeechState testAppContext;

    private Context testContext;

    @Before
    public void setUp() {
        // Use testContext obtained from InstrumentationRegistry
        testContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Create a mock AttributeSet
        AttributeSet attrs = createAttributeSet();

        // Create an instance of TestGlobalSpeechState
        testGlobalSpeechState = new TestGlobalSpeechState();

        // Set the global listening button
        GlobalListeningFeedbackButton button = new GlobalListeningFeedbackButton(testContext, attrs);
        testGlobalSpeechState.setGlobalListeningButton(button);

        // Assign testAppContext with the initialized testGlobalSpeechState
        testAppContext = testGlobalSpeechState;
    }




    @Test
    public void testInitialization() {
        AttributeSet attrs = createAttributeSet();
        GlobalListeningFeedbackButton button = new GlobalListeningFeedbackButton(testContext, attrs);

        // Verify that the button registers itself with GlobalSpeechState
        assertEquals(button.getId(), testAppContext.getGlobalListeningButton().getId());

        // Verify that the button is initially hidden
        assertEquals(View.GONE, button.getVisibility());
    }




    private AttributeSet createAttributeSet() {
        // Create an AttributeSet using a dummy XML resource
        XmlPullParser parser = testContext.getResources().getXml(R.xml.dummy); // Replace with your actual XML resource
        AttributeSet attrs = Xml.asAttributeSet(parser);
        return attrs;
    }

    // A simple class to mimic the behavior of GlobalSpeechState
    public static class TestGlobalSpeechState extends GlobalSpeechState {
        private GlobalListeningFeedbackButton globalListeningButton;

        public void setGlobalListeningButton(GlobalListeningFeedbackButton button) {
            this.globalListeningButton = button;
        }

        public GlobalListeningFeedbackButton getGlobalListeningButton() {
            return globalListeningButton;
        }
    }
}
