package com.example.watVindIkErger;

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

import com.example.AnswerConverter;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

public class WvieOtherOpinionsActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private boolean someoneWantsToSpeak = false;
    private ImageButton next;
    private ImageButton hearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_other_opinions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_other_opinions), (v, insets) -> {
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
        setButtonsClickable(false);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakTextPart2();
            }
        });
        new Handler().postDelayed(this::speakText, 2000);
    }

    private void goNextActivity() {
        Intent intent = new Intent(getApplicationContext(), WvieGameEndActivity.class);
        startActivity(intent);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Wat vinden de andere hiervan?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                speakTextPart2();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                speakTextPart2();
            }
        });
    }

    public void speakTextPart2() {
        speechRecognitionManager.stopListening();
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Wil iemand anders nog iets zeggen?", new SpeechHelper.SpeechCompleteListener() {
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
    private void setButtonsClickable(boolean clickable) {
        hearButton.setEnabled(clickable);
        next.setEnabled(clickable);
    }


    public void speakTextAskRemainingOpinions() {
        speechRecognitionManager.stopListening();
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Heeft iedereen kunnen zeggen wat ze willen?", new SpeechHelper.SpeechCompleteListener() {
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


    @Override
    public void onSpeechResult(String result) {
        if (!someoneWantsToSpeak) {
            switch (AnswerConverter.determineAnswer(result)) {
                case YES:
                    someoneWantsToSpeak = true;
                    speakTextAskRemainingOpinions();
                    break;
                case NO: goNextActivity(); break;
                default: speakTextPart2(); break;
            }
        } else {
            switch (AnswerConverter.determineAnswer(result)) {
                case YES: goNextActivity(); break;
                case NO: speakTextAskRemainingOpinions(); break;
                default: speakTextPart2(); break;
            }
        }
    }
}
