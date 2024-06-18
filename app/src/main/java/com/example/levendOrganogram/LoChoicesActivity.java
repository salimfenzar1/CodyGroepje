package com.example.levendOrganogram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.AnswerConverter;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.Random;

public class LoChoicesActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton yesButton;
    private ImageButton noButton;
    private ImageButton hearButton;
    private Random random = new Random();
    private int choice;
    private Intent messageIntent;
    private ArrayList<Statement> filteredStatements;

    private final LoChoicesActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_choices);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_choices), (v, insets) -> {
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

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        hearButton = findViewById(R.id.hearButton);

        choice = random.nextInt(2) + 1;

        messageIntent = new Intent(this, LoExplanationActivity.class);
        messageIntent.putParcelableArrayListExtra("filtered_statements", filteredStatements);

        if (choice == 1) {
            speakText("Is er iemand dichterbij komen staan?");
        } else {
            speakText("Is er iemand verder weg gaan staan?");
        }

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(true);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(false);
            }
        });

        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakTextAgain();
            }
        });

        setButtonsClickable(false);
    }

    private void speakText(String stelling) {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        speechHelper = new SpeechHelper(this);
        speechHelper.speak(stelling, new SpeechHelper.SpeechCompleteListener() {
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

    private void speakTextAgain() {
        setButtonsClickable(false);
        if (choice == 1) {
            speakText("Is er iemand dichterbij komen staan?");
        } else {
            speakText("Is er iemand verder weg gaan staan?");
        }
    }

    private void setButtonsClickable(boolean clickable) {
        yesButton.setEnabled(clickable);
        noButton.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    private void handleButtonClick(boolean userAgrees) {
        if (choice == 1) {
            messageIntent.putExtra("userAgrees", userAgrees);
        } else {
            messageIntent.putExtra("userAgrees", !userAgrees);
        }
        messageIntent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(messageIntent);
    }

    @Override
    public void onSpeechResult(String result) {
        switch (AnswerConverter.determineAnswer(result)) {
            case YES:
                if (choice == 1) {
                    messageIntent.putExtra("userAgrees", true);
                } else {
                    messageIntent.putExtra("userAgrees", false);
                }
                startActivity(messageIntent);
                finish();
                break;
            case NO:
                if (choice == 1) {
                    messageIntent.putExtra("userAgrees", false);
                } else {
                    messageIntent.putExtra("userAgrees", true);
                }
                startActivity(messageIntent);
                finish();
                break;
            default:
                speechRecognitionManager.startListening();
                break;
        }
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
