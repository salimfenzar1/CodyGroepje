package com.example.levendOrganogram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;
import com.example.watVindIkErger.WvieOtherOpinionsActivity;

public class LoExplanationActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private boolean userAgrees = false;
    private boolean hasSpokenFirstPart = false;
    private boolean hasSpokenSecondPart = false;
    private boolean hasSpokenThirdPart = false;

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_explanation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_explanation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userAgrees = getIntent().getBooleanExtra("userAgrees", false);

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextActivity();
            }
        });

        hearButton = findViewById(R.id.hearButton);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                if (hasSpokenThirdPart) {
                    speakText();
                    return;
                }
                if (hasSpokenSecondPart) {
                    speakTextSecondPart();
                    return;
                }
                if (hasSpokenFirstPart) {
                    speakText();
                }
            }
        });



    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        boolean value = hasSpokenSecondPart ? !userAgrees : userAgrees;
        String textToSpeak = value
                ? "Waarom ben jij het eens met de stelling? Zeg: 'wij willen verder', als jullie verder willen"
                : "Waarom ben jij het oneens met de stelling? Zeg: 'wij willen verder', als jullie verder willen";
        speechHelper.speak(textToSpeak, new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                hasSpokenFirstPart = true;
                hasSpokenThirdPart = hasSpokenSecondPart;
                speechRecognitionManager.startListening();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                hasSpokenFirstPart = true;
                hasSpokenThirdPart = hasSpokenSecondPart;
                speechRecognitionManager.startListening();
            }
        });
    }


    public void speakTextSecondPart() {
        speechHelper = new SpeechHelper(this);
        String textToSpeak = userAgrees
                ? "Is er iemand het oneens met de stelling?"
                : "Is er iemand het eens met de stelling?";
        speechHelper.speak(textToSpeak, new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                hasSpokenSecondPart = true;
                speechRecognitionManager.startListening();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                hasSpokenSecondPart = true;
                speechRecognitionManager.startListening();
            }
        });
    }


    @Override
    public void onSpeechResult(String result) {
        result = result.toLowerCase().trim();
        if (!hasSpokenSecondPart && result.contains("wij willen verder")) {
            speakTextSecondPart();
        } else {
            speakText();
        }
    }

    private void navigateToNextActivity() {
        Toast.makeText(getApplicationContext(), "Navigating to the next activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), WvieOtherOpinionsActivity.class);
        startActivity(intent);
        finish(); // Close current activity to free up resources
    }

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechHelper != null) {
            speechHelper.close();
        }
        if (speechRecognitionManager != null) {
            speechRecognitionManager.destroy();
        }
    }
}