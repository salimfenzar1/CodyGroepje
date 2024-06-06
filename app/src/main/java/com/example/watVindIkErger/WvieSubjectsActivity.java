package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;

public class WvieSubjectsActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private ImageView themeDecease;
    private ImageView themeSexuality;
    private List<Statement> allStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        allStatements = getIntent().getParcelableArrayListExtra("statements");
        if (allStatements == null) {
            allStatements = new ArrayList<>(); // Initialize to an empty list if null
        }

        setButtonsClickable(false);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });

        themeDecease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt op overlijden gedrukt", Toast.LENGTH_SHORT).show();
                filterAndNavigate("Overlijden");
            }
        });

        themeSexuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt op seksualiteit op de werkvloer gedrukt", Toast.LENGTH_SHORT).show();
                filterAndNavigate("Seksualiteit op de werkvloer");
            }
        });

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie stellingen over het onderwerp: seksualiteit op de werkvloer, overlijden, of allebei?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                // Wait for a short period before starting recognition
                new Handler().postDelayed(() -> {
                    if (!speechRecognitionManager.isListening()) {
                        speechRecognitionManager.startListening();
                    }
                }, 1000); // Delay to ensure speech synthesis completes
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                // Wait for a short period before starting recognition
                new Handler().postDelayed(() -> {
                    if (!speechRecognitionManager.isListening()) {
                        speechRecognitionManager.startListening();
                    }
                }, 1000); // Delay to ensure speech synthesis completes
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        themeDecease.setEnabled(clickable);
        themeSexuality.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.d("WvieSubjectsActivity", "onSpeechResult: " + result);
        if (result.equalsIgnoreCase("seksualiteit op de werkvloer")) {
            filterAndNavigate("Seksualiteit op de werkvloer");
        } else if (result.equalsIgnoreCase("overlijden")) {
            filterAndNavigate("Overlijden");
        } else if (result.equalsIgnoreCase("allebei")) {
            filterAndNavigate("Seksualiteit op de werkvloer", "Overlijden");
        } else {
            Toast.makeText(this, "Ongeldige invoer, probeer opnieuw.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                if (!speechRecognitionManager.isListening()) {
                    speechRecognitionManager.startListening();
                }
            }, 1000); // Restart listening after an invalid input
        }
    }

    private void filterAndNavigate(String... categories) {
        List<Statement> filteredStatements = new ArrayList<>();
        for (Statement statement : allStatements) {
            for (String category : categories) {
                if (statement.category.equals(category)) {
                    filteredStatements.add(statement);
                }
            }
        }
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), WvieIntensityActivity.class);
        intent.putParcelableArrayListExtra("statements", new ArrayList<>(filteredStatements));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognitionManager != null) {
            speechRecognitionManager.destroy();
        }
        if (speechHelper != null) {
            speechHelper.close();
        }
    }
}
