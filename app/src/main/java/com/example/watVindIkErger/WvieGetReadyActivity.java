package com.example.watVindIkErger;

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

public class WvieGetReadyActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private boolean clarificationAsked = false;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<Statement> filteredStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_get_ready);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_get_ready), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);
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
        speechRecognitionManager.stopListening();
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Staat iedereen klaar?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                speakText();
            }
        });
    }

    public void speakTextAskClarification() {
        speechRecognitionManager.stopListening();
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Is het duidelijk wat jullie moeten doen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                clarificationAsked = true;
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                clarificationAsked = true;
                speakTextAskClarification();
            }
        });
    }

    public void speakTextClarification() {
        speechRecognitionManager.stopListening();
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        WvieGetReadyActivity currentActivity = this;
        speechHelper.speak("Ga in het midden voor mij staan en zorg dat er links en rechts plaats is", new SpeechHelper.SpeechCompleteListener() {
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
        if (!clarificationAsked) {
            if (result.equalsIgnoreCase("ja")) {
                speechRecognitionManager.stopListening();
                speechRecognitionManager.destroy();
                goNextActivity();
            } else if (result.equalsIgnoreCase("nee")) {
                new Handler().postDelayed(this::speakText, 3000);
            } else {
                speechRecognitionManager.stopListening();
                new Handler().postDelayed(this::speakText, 5000);
            }
        } else {
            if (result.equalsIgnoreCase("ja")) {
                clarificationAsked = false;
                speechRecognitionManager.stopListening();
                new Handler().postDelayed(this::speakText, 5000);
            } else if (result.equalsIgnoreCase("nee")) {
                clarificationAsked = false;
                speakTextClarification();
            } else {
                speakTextAskClarification();
            }
        }
    }


    private void goNextActivity() {
        Log.d("WvieGetReadyActivity", "Received filtered statements: " + filteredStatements);
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), WvieStatementRedActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
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
