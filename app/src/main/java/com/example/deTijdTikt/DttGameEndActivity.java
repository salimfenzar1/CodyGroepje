package com.example.deTijdTikt;

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

import com.example.MainActivity;
import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.services.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;

public class DttGameEndActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton home;
    private ImageButton replay;
    private ImageButton hearButton;
    private final DttGameEndActivity context = this;
    private ArrayList<Statement> filteredStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dtt_game_end);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Retrieve the filtered statements from the intent
        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        // If home button is clicked
        home = findViewById(R.id.homeButton);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        // If replay button is clicked
        replay = findViewById(R.id.againButton);

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replayGame();
            }
        });

        hearButton = findViewById(R.id.hearButton);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakText();
            }
        });

        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);
    }

    private void goHome() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void replayGame() {
        Intent intent = new Intent(getApplicationContext(), DttGetReadyActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
        finish();
    }

    public void speakText() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie nog een ronde spelen?", new SpeechHelper.SpeechCompleteListener() {
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

    public void speakTextPlayAgain() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("We gaan nog een keer spelen. Iedereen ga weer klaarstaan.", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                replayGame();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                replayGame();
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        home.setEnabled(clickable);
        replay.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }


    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = (result.trim().toLowerCase());
        if (result.contains("ja")) {
            speakTextPlayAgain();
        } else if (result.contains("nee")) {
            goHome();
        } else {
            speechRecognitionManager.startListening();
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

