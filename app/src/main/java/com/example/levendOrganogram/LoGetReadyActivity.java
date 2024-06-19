package com.example.levendOrganogram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.services.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoGetReadyActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private boolean clarificationAsked = false;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<Statement> filteredStatements;
    private final LoGetReadyActivity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_get_ready);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_get_ready), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        // Ensure filteredStatements is not null
        if (filteredStatements == null) {
            filteredStatements = new ArrayList<>();
        }
        // Filter the statements to only include active ones
        List<Statement> activeStatements = filteredStatements.stream()
                .filter(Statement::isActive)
                .collect(Collectors.toList());

        filteredStatements = new ArrayList<>(activeStatements);

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextActivity();
            }
        });

        hearButton = findViewById(R.id.hearButton);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });
        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Staat iedereen klaar?", new SpeechHelper.SpeechCompleteListener() {
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

    public void speakTextAskClarification() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Is het duidelijk wat jullie moeten doen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                clarificationAsked = true;
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                clarificationAsked = true;
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();            }
        });
    }

    public void speakTextClarification() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        LoGetReadyActivity currentActivity = this;
        speechHelper.speak("Ga in een kring om mij heen staan", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                new Handler().postDelayed(currentActivity::speakText, 5000);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                new Handler().postDelayed(currentActivity::speakText, 5000);
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        result = (result.trim().toLowerCase());
        if (!clarificationAsked) {
            if (result.contains("ja")) {
                speechRecognitionManager.stopListening();
                speechRecognitionManager.destroy();
                goNextActivity();
            } else if (result.contains("nee")) {
                speakTextAskClarification();
            } else {
                speechRecognitionManager.stopListening();
                new Handler().postDelayed(this::speakText, 5000);
            }
        } else {
            if (result.contains("ja")) {
                clarificationAsked = false;
                speechRecognitionManager.stopListening();
                new Handler().postDelayed(this::speakText, 5000);
            } else if (result.contains("nee")) {
                clarificationAsked = false;
                speakTextClarification();
            } else {
                speakTextAskClarification();
            }
        }
    }


    private void goNextActivity() {
        Log.d("LoGetReadyActivityStatements", "Received filtered statements: " + filteredStatements);
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        Intent intent = new Intent(getApplicationContext(), LoStatementActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
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