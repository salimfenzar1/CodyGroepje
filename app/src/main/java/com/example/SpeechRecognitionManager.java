package com.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognitionManager {
    private SpeechRecognizer speechRecognizer;
    private Context context;
    private SpeechRecognitionListener listener;

    public SpeechRecognitionManager(Context context, SpeechRecognitionListener listener) {
        this.context = context;
        this.listener = listener;
        initializeSpeechComponents();
    }

    private void initializeSpeechComponents() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                // Do something when ready for speech
            }

            @Override
            public void onBeginningOfSpeech() {
                // Do something at the beginning of speech
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // Do something when RMS changed
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // Do something when buffer received
            }

            @Override
            public void onEndOfSpeech() {
                // Do something at the end of speech
            }

            @Override
            public void onError(int error) {
                Log.e("SpeechRecognizer", "Error: " + error);
                startListening(); // Restart listening on error
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    listener.onSpeechResult(matches.get(0));
                }
                startListening(); // Restart listening after receiving results
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Do something with partial results
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // Handle events
            }
        });
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Zeg iets...");
        speechRecognizer.startListening(intent);
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public interface SpeechRecognitionListener {
        void onSpeechResult(String result);
    }
}
