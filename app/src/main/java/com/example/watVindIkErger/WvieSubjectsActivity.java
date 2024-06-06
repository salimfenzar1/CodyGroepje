package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

public class WvieSubjectsActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private ImageView themeDecease;
    private ImageView themeSexuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_subjects), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        hearButton = findViewById(R.id.hearButton);
        themeDecease = findViewById(R.id.image_view_family);
        themeSexuality = findViewById(R.id.image_seksualiteit);

        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });

        themeDecease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToIntensityActivity();
            }
        });

        themeSexuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToIntensityActivity();
            }
        });

        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie stellingen over het onderwerp: seksualiteit op de werkvloer, overlijden, of allebei?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                new Handler().postDelayed(() -> speechRecognitionManager.startListening(), 1000); // Add delay before starting listening
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                new Handler().postDelayed(() -> speechRecognitionManager.startListening(), 1000); // Add delay before starting listening
            }
        });
    }

    private void navigateToIntensityActivity() {
        Toast.makeText(getApplicationContext(), "Navigating to the next page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), WvieIntensityActivity.class);
        startActivity(intent);
    }

    private void setButtonsClickable(boolean clickable) {
        themeDecease.setEnabled(clickable);
        themeSexuality.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.d("WvieSubjectsActivity", "Recognized speech: " + result);
        result = result.trim().toLowerCase();

        if (result.contains("seksualiteit")) {
            Log.d("WvieSubjectsActivity", "Navigating to Intensity Activity for Sexuality theme");
            navigateToIntensityActivity();
        } else if (result.contains("overlijden")) {
            Log.d("WvieSubjectsActivity", "Navigating to Intensity Activity for Decease theme");
            navigateToIntensityActivity();
        } else if (result.contains("allebei")) {
            Log.d("WvieSubjectsActivity", "Navigating to Intensity Activity for both themes");
            navigateToIntensityActivity();
        } else {
            Log.d("WvieSubjectsActivity", "Unrecognized speech. Listening again...");
            new Handler().postDelayed(() -> speechRecognitionManager.startListening(), 1000); // Add delay before starting listening again
        }
    }
}
