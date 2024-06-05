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

public class WvieTutorialActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
import java.util.ArrayList;

public class WvieTutorialActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<String> selectedIntensities;

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
        selectedIntensities = intent.getStringArrayListExtra("SELECTED_INTENSITIES");

        // Checking if intensity level is received
        if (selectedIntensities != null) {
            Log.d("WvieTutorialActivity", "Selected intensities: " + selectedIntensities.toString());
        } else {
            Log.d("WvieTutorialActivity", "No selected intensities received.");
        }

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performOutro();
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
        speechHelper.speak("Welkom bij het spel: Wat vind ik erger! Ik licht kort toe wat we gaan doen. Ik lees dadelijk twee stellingen voor, deze zijn gekoppeld aan een kleur. Mijn linker kant is geel en mijn  rechter kant is rood. Vervolgens kiezen jullie welke van de twee stellingen je erger vindt en ga je aan deze kant van mij staan. Daarna zullen we discussiëren over waarom je deze stelling erger vindt... Is alles duidelijk?", new SpeechHelper.SpeechCompleteListener() {
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

    public void performOutro() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

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
                goNextActivity();
            }
        });
    }
    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);

    }

    @Override
    public void onSpeechResult(String result) {
        switch (AnswerConverter.determineAnswer(result)) {
            case YES: performOutro(); break;
            default: speakText(); break; // NO and MAYBE
            case UNKNOWN: break; // TODO: Implement UNKNOWN
        }
    }

    private void goNextActivity() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), WvieGetReadyActivity.class);
        startActivity(intent);
    }
}
