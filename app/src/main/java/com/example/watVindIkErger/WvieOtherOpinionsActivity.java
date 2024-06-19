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

public class WvieOtherOpinionsActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private boolean someoneWantsToSpeak = false;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<Statement> filteredStatements;
    private final WvieOtherOpinionsActivity context = this;

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

    private void goNextActivity() {
        Intent intent = new Intent(getApplicationContext(), WvieGameEndActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        finish();
    }

    public void speakText() {
        setButtonsClickable(false);
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Wat vinden de andere hiervan? Wanneer iedereen is uitgepraat, zeg dan: Wij willen doorgaan, om door te gaan!", new SpeechHelper.SpeechCompleteListener() {
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


    private void setButtonsClickable(boolean clickable) {
        hearButton.setEnabled(clickable);
        next.setEnabled(clickable);
    }


    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = (result.trim().toLowerCase());
        if (result.contains("willen") || result.contains("doorgaan")) {
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
}
