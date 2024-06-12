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
    private final Context context;
    private final SpeechRecognitionListener listener;
    private ConfirmationResultListener confirmationResultListener;
    private boolean isListening = false;

    public SpeechRecognitionManager(Context context, SpeechRecognitionListener listener) {
        this.context = context;
        this.listener = listener;
        initializeSpeechComponents();
    }

    private void initializeSpeechComponents() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d("SpeechRecognizer", "Ready for speech");
                }

                @Override
                public void onBeginningOfSpeech() {
                    isListening = true;
                    Log.d("SpeechRecognizer", "Beginning of speech");
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
                    isListening = false;
                    Log.d("SpeechRecognizer", "End of speech");
                }

                @Override
                public void onError(int error) {
                    isListening = false;
                    Log.e("SpeechRecognizer", "Error: " + error);
                    if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                        Log.e("SpeechRecognizer", "Recognizer busy, will retry...");
                    } else {
                        startListening(); // Restart listening on error
                    }
                }

                @Override
                public void onResults(Bundle results) {
                    isListening = false;
                    Log.d("context test", context.getClass().getSimpleName());
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        if (confirmationResultListener != null) {
                            confirmationResultListener.onConfirmationResult(matches.get(0));
                        } else {
                            listener.onSpeechResult(matches.get(0));
                        }
                    }
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
        } else {
            Log.e("SpeechRecognition", "Speech recognition is not available on this device.");
        }
    }

    public void startListening() {
        if (speechRecognizer != null && !isListening) {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                Log.d("StartListening Method", "Listening...");
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("nl-NL"));
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Zeg iets...");
                speechRecognizer.startListening(intent);
                isListening = true;
            }
        }
    }

    public void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            isListening = false;
        }
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public boolean isListening() {
        return isListening;
    }

    public void setConfirmationResultListener(ConfirmationResultListener listener) {
        this.confirmationResultListener = listener;
    }

    public interface SpeechRecognitionListener {
        void onSpeechResult(String result);
    }

    public interface ConfirmationResultListener {
        void onConfirmationResult(String result);
    }
}
