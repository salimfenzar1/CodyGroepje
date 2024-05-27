package com.example.codycactus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
    private boolean isSpeaking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton tijdTikt = findViewById(R.id.tijdTikt);
        speechHelper = new SpeechHelper(this);
        tijdTikt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpeaking) {
                    isSpeaking = true;  // Set the flag to true when starting speech
                    Log.d("test", "de tekst naar spraak werkt");
                    speechHelper.speak("Je hebt gedrukt op het spel: De tijd tikt! Succes met spelen!", new SpeechHelper.SpeechCompleteListener() {
                        @Override
                        public void onSpeechComplete() {
                            Log.d("Speech", "Speech synthesis voltooid");
                            isSpeaking = false;  
                        }

                        @Override
                        public void onSpeechFailed() {
                            Log.e("Speech", "Speech synthesis mislukt");
                            isSpeaking = false;
                        }
                    });
                } else {
                    Log.d("test", "Spraak is al bezig, klik genegeerd");
                }
            }
        });
    }
}