package com.example.components;

import android.app.Application;
import android.view.View;
import android.widget.Button;

public class GlobalSpeechState extends Application{
    private Button globalListeningButton;

    public void setGlobalListeningButton(Button button) {
        this.globalListeningButton = button;
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
}
