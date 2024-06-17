package com.example.levendOrganogram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;
import com.example.watVindIkErger.WvieGetReadyActivity;

import java.util.ArrayList;

public class LoTutorialActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<String> selectedIntensities;
    private ArrayList<Statement> filteredStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_tutorial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_tutorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        // Checking if filtered statements are received
        if (filteredStatements.size() < 1) {
            Log.d("LoTutorialActivity", "Filtered statements count: " + filteredStatements.size());
        } else {
            Log.d("LoTutorialActivity", "No filtered statements received.");
        }

        next = findViewById(R.id.nextButton);
        hearButton = findViewById(R.id.hearButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                performOutro();
            }
        });

        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                repeatText();
            }
        });

        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechRecognitionManager.stopListening();
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Welkom bij het spel levend organogram! Ik licht zo kort toe wat we gaan doen. We vormen zo een kring om mij heen. Ik lees steeds een stelling voor. Als jullie het eens zijn met de stelling kom je dichterbij mij staan. Ben je het oneens met de stelling ga je verder van mij af staan. Zodra iedereen staat bespreken we waarom jullie daarvoor hebben gekozen... Is alles duidelijk?", new SpeechHelper.SpeechCompleteListener() {
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
            }
        });
    }

    public void repeatText() {
        speechRecognitionManager.stopListening();
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("We vormen een kring om mij heen. Ik lees steeds een stelling voor. Als jullie het eens zijn met de stelling kom je dichterbij mij staan. Ben je het oneens met de stelling ga je verder van mij af staan. Zodra iedereen staat bespreken we waarom jullie daarvoor hebben gekozen... Is alles nu wel duidelijk?", new SpeechHelper.SpeechCompleteListener() {
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
            }
        });
    }

    public void performOutro() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Veel plezier met het spelen van Levend Organogram!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                goNextActivity();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                performOutro();
            }
        });
}

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        if (result.equalsIgnoreCase("ja")) {
            performOutro();
        } else if (result.equalsIgnoreCase("nee") || result.equalsIgnoreCase("misschien")) {
            repeatText();
        } else {
            // Handle the UNKNOWN case if necessary
            speechRecognitionManager.startListening();
        }
    }


    private void goNextActivity() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), LoGetReadyActivity.class);
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