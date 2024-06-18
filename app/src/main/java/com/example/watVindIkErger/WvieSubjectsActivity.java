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
    private final WvieSubjectsActivity context = this;
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
                filterAndNavigate("Overlijden");
            }
        });

        themeSexuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterAndNavigate("Seksualiteit op de werkvloer");
            }
        });

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie stellingen over het onderwerp: seksualiteit op de werkvloer, overlijden, of allebei?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                // Start listening immediately after speech synthesis completes
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }
        });
    }


    private void speakRetry() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        speechHelper.speak("Sorry dat verstond ik niet, zou je dat kunnen herhalen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }


            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
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
        result = (result.trim().toLowerCase());
        if (result.contains("seksualiteit") || result.contains("werkvloer")) {
            filterAndNavigate("Seksualiteit op de werkvloer");
        } else if (result.contains("overlijden")) {
            filterAndNavigate("Overlijden");
        } else if (result.contains("allebei") || result.contains("beide")) {
            filterAndNavigate("Seksualiteit op de werkvloer", "Overlijden");
        } else {
            speakRetry();
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
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        Intent intent = new Intent(getApplicationContext(), WvieIntensityActivity.class);
        intent.putParcelableArrayListExtra("statements", (ArrayList<Statement>) filteredStatements);
        startActivity(intent);
        finish();
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
