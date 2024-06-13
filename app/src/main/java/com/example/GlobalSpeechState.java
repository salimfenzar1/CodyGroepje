package com.example;

import android.app.Application;
import android.view.View;
import android.widget.Button;

public class GlobalSpeechState extends Application{
    private GlobalListeningFeedbackButton globalListeningButton;

    public void setGlobalListeningButton(GlobalListeningFeedbackButton button) {
        this.globalListeningButton = button;
    }

    public GlobalListeningFeedbackButton getGlobalListeningButton() {
        return globalListeningButton;
    }

    public void showListeningButton(boolean show) {
        if (globalListeningButton != null) {
            globalListeningButton.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void hideListeningButton() {
        if (globalListeningButton != null) {
            globalListeningButton.setVisibility(View.GONE);
        }
    }

   // public abstract void setGlobalListeningButton(GlobalListeningFeedbackButton button);

//    public abstract void setGlobalListeningButton(GlobalListeningFeedbackButton button);
}
