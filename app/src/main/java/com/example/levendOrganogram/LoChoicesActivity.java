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
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

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

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        hearButton = findViewById(R.id.hearButton);

        choice = random.nextInt(2) + 1;
        messageIntent = new Intent(this, LoExplanationActivity.class);

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

        setButtonsClickable(false);
    }

    private void speakText(String stelling) {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak(stelling, new SpeechHelper.SpeechCompleteListener() {
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
        startActivity(messageIntent);
    }

    @Override
    public void onSpeechResult(String result) {
        switch (AnswerConverter.determineAnswer(result)) {
            case YES: // TODO: action if answer is yes
                if(choice == 1){
                    messageIntent.putExtra("userAgrees",true);
                    startActivity(messageIntent);
                } else {
                    messageIntent.putExtra("userAgrees",false);
                    startActivity(messageIntent);
                }
                break;
            case NO: // TODO: action if answer is no
                if(choice == 1){
                    messageIntent.putExtra("userAgrees",false);
                    startActivity(messageIntent);
                } else {
                    messageIntent.putExtra("userAgrees",true);
                    startActivity(messageIntent);
                }
                break;
            default: speechRecognitionManager.startListening(); break;
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
