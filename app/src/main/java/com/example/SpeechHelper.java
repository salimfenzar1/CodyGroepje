package com.example;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

public class SpeechHelper {
    private static final String API_KEY = "92fff4f9dab940e9a5e9a9fc9a97e1b0";
    private static final String REGION = "germanywestcentral";

    private SpeechSynthesizer synthesizer;
    private Context context;
    public SpeechHelper(Context context) {
        this.context = context;
        SpeechConfig config = SpeechConfig.fromSubscription(API_KEY, REGION);
        config.setSpeechSynthesisVoiceName("en-US-AndrewMultilingualNeural");
        this.synthesizer = new SpeechSynthesizer(config);
    }

    public void speak(String text, SpeechCompleteListener listener) {
        String ssml = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='en-US'>" +
                "<voice name='en-US-AndrewMultilingualNeural'>" +
                "<prosody rate='+10%'>" + text + "</prosody>" +
                "</voice></speak>";
        new SynthesisTask(listener).execute(ssml);
    }

    private class SynthesisTask extends AsyncTask<String, Void, SpeechSynthesisResult> {
        private SpeechCompleteListener listener;

        public SynthesisTask(SpeechCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected SpeechSynthesisResult doInBackground(String... texts) {
            try {
                return synthesizer.SpeakSsml(texts[0]); // Gebruik SpeakSsml in plaats van SpeakText
            } catch (Exception e) {
                Log.e("TTS", "Error in speech synthesis: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(SpeechSynthesisResult result) {
            if (result != null) {
                if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    Log.i("TTS", "Speech synthesis succeeded.");
                    listener.onSpeechComplete();
                } else {
                    Log.e("TTS", "Speech synthesis failed. Reason: " + result.getReason());
                    listener.onSpeechFailed();
                }
                result.close();
            } else {
                listener.onSpeechFailed();
            }
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
    }
}
