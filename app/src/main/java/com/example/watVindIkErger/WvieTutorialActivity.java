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

public class WvieTutorialActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<String> selectedIntensities;
    private ArrayList<Statement> filteredStatements;
    private final WvieTutorialActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_tutorial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_tutorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");


        next = findViewById(R.id.nextButton);
        hearButton = findViewById(R.id.hearButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOutro();
            }
        });

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
        speechHelper.speak("Welkom bij het spel: Wat vind ik erger! Ik licht kort toe wat we gaan doen. Ik lees dadelijk twee stellingen voor, deze zijn gekoppeld aan een kleur. Mijn linker kant is geel en mijn rechter kant is rood. Vervolgens kiezen jullie welke van de twee stellingen je erger vindt en ga je aan deze kant van mij staan. Daarna zullen we discussiëren over waarom je deze stelling erger vindt... Is alles duidelijk?", new SpeechHelper.SpeechCompleteListener() {
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

    public void performOutro() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Veel plezier met het spelen van wat vind ik erger!", new SpeechHelper.SpeechCompleteListener() {
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
        result = (result.trim().toLowerCase());
        if (result.contains("ja")) {
            performOutro();
        } else if (result.contains("nee") || result.contains("misschien")) {
            speakText();
        } else {
            speechRecognitionManager.startListening();
        }
    }


    private void goNextActivity() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        Intent intent = new Intent(getApplicationContext(), WvieGetReadyActivity.class);
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
