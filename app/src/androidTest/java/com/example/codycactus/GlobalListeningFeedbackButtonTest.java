// GlobalListeningFeedbackButtonTest.java
package com.example.codycactus;

import android.content.Context;
import android.view.View;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.GlobalListeningFeedbackButton;

public class GlobalListeningFeedbackButtonTest {

    private Context context;
    private GlobalListeningFeedbackButton globalListeningFeedbackButton;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        globalListeningFeedbackButton = new GlobalListeningFeedbackButton(context, null);
    }

    @Test
    public void testInitialVisibility() {
        assertEquals(View.GONE, globalListeningFeedbackButton.getVisibility());
    }
}
