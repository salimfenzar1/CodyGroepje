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

import com.example.AnswerConverter;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

public class WvieGetReadyActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private boolean clarificationAsked = false;
    private ImageButton next;
    private ImageButton hearButton;
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

        new Handler().postDelayed(this::speakText, 5000);

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
                speechRecognitionManager.startListening();
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
                speechRecognitionManager.startListening();
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
            switch (AnswerConverter.determineAnswer(result)) {
                default:
                    speechRecognitionManager.stopListening();
                    new Handler().postDelayed(this::speakText, 5000);
                    break;
                case YES:
                    speechRecognitionManager.stopListening();
                    speechRecognitionManager.destroy();
                    goNextActivity();
                    break;
                case NO:
                    speakTextAskClarification();
                    break;

            }
        }  else {
            switch (AnswerConverter.determineAnswer(result)) {
                case YES:
                    clarificationAsked = false;
                    speechRecognitionManager.stopListening();
                    new Handler().postDelayed(this::speakText, 5000);
                    break;
                case NO:
                    clarificationAsked = false;
                    speakTextClarification();
                    break;
                default:
                    speakTextAskClarification();
            }
        }
    }
    private void goNextActivity() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), WvieStatementRedActivity.class);
        startActivity(intent);
    }
}