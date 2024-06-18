package com.example.services;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

import java.util.Locale;

public class SpeechHelper {
    private static final String API_KEY = "50ebcb27116f4c669a3f595d905b4d27";
    private static final String REGION = "westeurope";

    private SpeechSynthesizer synthesizer;
    private TextToSpeech textToSpeech;
    private Context context;

    public SpeechHelper(Context context) {
        this.context = context;
        SpeechConfig config = SpeechConfig.fromSubscription(API_KEY, REGION);
        config.setSpeechSynthesisVoiceName("en-US-AndrewMultilingualNeural");
        this.synthesizer = new SpeechSynthesizer(config);
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(new Locale("nl", "NL")); // Set language to Dutch
            } else {
                Log.e("TTS", "Initialization of Android TTS failed.");
            }
        });
    }

    public void speak(String text, SpeechCompleteListener listener) {
        String ssml = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='en-US'>" +
                "<voice name='en-US-AndrewMultilingualNeural'>" +
                "<prosody rate='+20%'>" + text + "</prosody>" +
                "</voice></speak>";
        new SynthesisTask(listener).execute(ssml, text);
    }

    private class SynthesisTask extends AsyncTask<String, Void, SpeechSynthesisResult> {
        private SpeechCompleteListener listener;
        private String fallbackText;

        public SynthesisTask(SpeechCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected SpeechSynthesisResult doInBackground(String... texts) {
            fallbackText = texts[1]; // Save the fallback text
            try {
                return synthesizer.SpeakSsml(texts[0]);
            } catch (Exception e) {
                Log.e("TTS", "Error in speech synthesis: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(SpeechSynthesisResult result) {
            if (result != null && result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                Log.i("TTS", "Speech synthesis succeeded.");
                listener.onSpeechComplete();
            } else {
                Log.e("TTS", "Speech synthesis failed or null result. Reason: " + (result != null ? result.getReason() : "null result"));
                useAndroidTTS(fallbackText, listener);
            }
            if (result != null) {
                result.close();
            }
        }
    }

    private void useAndroidTTS(String text, SpeechCompleteListener listener) {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Not needed
            }

            @Override
            public void onDone(String utteranceId) {
                listener.onSpeechComplete();
            }

            @Override
            public void onError(String utteranceId) {
                listener.onSpeechFailed();
            }
        });

        int result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID");
        if (result == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in Android TTS synthesis.");
            listener.onSpeechFailed();
        }
    }

    public interface SpeechCompleteListener {
        void onSpeechComplete();
        void onSpeechFailed();
    }

    public void close() {
        if (synthesizer != null) {
            synthesizer.close();
        }
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
