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

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;

public class DttExplanationRedActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<Statement> filteredStatements;
    private boolean isFirst = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dtt_explanation_red);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isFirst = getIntent().getBooleanExtra("isFirst", false);
        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        Intent intent = getIntent();
        if (intent != null) {
            filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        }

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
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Kan de persoon met de rode bal de casus toelichten? Als je klaar bent met praten zeg dan: wij willen doorgaan!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                new Handler().postDelayed(() -> speakText(), 1000);
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        hearButton.setEnabled(clickable);
        next.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = result.trim().toLowerCase();
        if (result.contains("wij willen doorgaan")) {
            goNextActivity();
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

    private void goNextActivity() {
        if (isFirst) {
            Intent intent = new Intent(this, DttExplanationYellowActivity.class);
            intent.putParcelableArrayListExtra("filtered_statements", intent.getParcelableArrayListExtra("filtered_statements"));
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, DttPassToNextPersonActivity.class);
            intent.putParcelableArrayListExtra("filtered_statements", intent.getParcelableArrayListExtra("filtered_statements"));
            startActivity(intent);
        }
    }
}