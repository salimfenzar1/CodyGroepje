package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.Random;

public class WvieMakeChoiceActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private Statement redStatement;
    private Statement yellowStatement;
    private ArrayList<Statement> filteredStatements;
    private boolean hasNavigated = false; // To prevent double navigation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_make_choice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_make_choice), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        redStatement = intent.getParcelableExtra("red_statement");
        yellowStatement = intent.getParcelableExtra("yellow_statement");
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        new Handler().postDelayed(this::speakText, 2000);
        new Handler().postDelayed(this::navigateToNextActivity, 10000);
    }

    private void navigateToNextActivity() {
        if (!hasNavigated) { // Ensure the activity transition happens only once
            hasNavigated = true;
            Random random = new Random();
            Intent nextIntent;
            if (random.nextBoolean()) {
                nextIntent = new Intent(this, WvieChoiceRedActivity.class);
                nextIntent.putExtra("red_statement", redStatement);
                nextIntent.putExtra("yellow_statement", yellowStatement);
            } else {
                nextIntent = new Intent(this, WvieChoiceYellowActivity.class);
                nextIntent.putExtra("yellow_statement", yellowStatement);
                nextIntent.putExtra("red_statement", redStatement);
            }
            nextIntent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
            startActivity(nextIntent);
            finish();
        }
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Kies nu aan welke kant van mij je gaat staan", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechHelper != null) {
            speechHelper.close();
        }
    }
}
