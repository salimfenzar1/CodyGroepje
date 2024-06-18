// ListeningButton.java
package com.example.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


@SuppressLint("AppCompatCustomView")
public class GlobalListeningFeedbackButton extends Button {
    public GlobalListeningFeedbackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        GlobalSpeechState app = (GlobalSpeechState) getContext().getApplicationContext();
        app.setGlobalListeningButton(this);
        setVisibility(View.GONE); // Initially hidden
    }
}
