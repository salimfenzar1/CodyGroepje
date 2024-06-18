package com.example.levendOrganogram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.watVindIkErger.WvieIntensityActivity;
import com.example.watVindIkErger.WvieTutorialActivity;

import java.util.ArrayList;
import java.util.List;

public class LoIntensityActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private ImageView low;
    private ImageView medium;
    private ImageView high;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private ImageButton next;
    private List<Integer> selectedIntensities;
    private boolean isInitialLowImage = true;
    private boolean isInitialMediumImage = true;
    private boolean isInitialHighImage = true;
    private int[] intensities = {1, 2, 3};
    private int currentIntensityIndex = 0;
    private ArrayList<Statement> filteredStatements;
    private final LoIntensityActivity context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_intensity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_intensity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedIntensities = new ArrayList<>();
        filteredStatements = getIntent().getParcelableArrayListExtra("statements");
        Log.d("LoIntensityActivity", "Filtered statements: " + filteredStatements.size());

        low = findViewById(R.id.image_view_low_intensity);
        medium = findViewById(R.id.image_view_medium_intensity);
        high = findViewById(R.id.image_view_high_intensity);

        low.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                toggleIntensitySelection(1);
                updateImageView("laagdrempelig");
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection(2);
                updateImageView("matig");
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection(3);
                updateImageView("intens");
            }
        });

        hearButton = findViewById(R.id.hearButton);
        setButtonsClickable(false);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakText();
            }
        });

        next = findViewById(R.id.toNextPage);
        next.setEnabled(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedIntensities.isEmpty()) {
                    filterStatementsByIntensity();
                    startNextActivity();
                } else {
                    Toast.makeText(LoIntensityActivity.this, "Selecteer minimaal één intensiteit", Toast.LENGTH_SHORT).show();
                }
            }
        });


        new Handler().postDelayed(this::speakText, 2000);
    }

    private void toggleIntensitySelection(int intensity) {
        if (selectedIntensities.contains(intensity)) {
            selectedIntensities.remove((Integer) intensity);
            Log.d("LoIntensityActivity", "Removed intensity level: " + intensity + ". Current selection: " + selectedIntensities);
        } else {
            selectedIntensities.add(intensity);
            Log.d("LoIntensityActivity", "Added intensity level: " + intensity + ". Current selection: " + selectedIntensities);
        }
        updateToNextPageButtonState();
    }

    private void updateToNextPageButtonState() {
        next.setEnabled(!selectedIntensities.isEmpty());
    }

    private void filterStatementsByIntensity() {
        ArrayList<Statement> filteredList = new ArrayList<>();
        for (Statement statement : filteredStatements) {
            if (selectedIntensities.contains(statement.intensityLevel)) {
                filteredList.add(statement);
            }
        }
        filteredStatements = filteredList;
    }

    private void startNextActivity() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(this, LoTutorialActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
        finish();
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        speechHelper.speak("In welke mate van intensiteit willen jullie de stellingen? Je kan kiezen tussen laagdrempelig, matig, intens of een combinatie hiervan! Als je een combinatie van de intensiteiten wilt kiezen, moet je dit handmatig op het scherm aanklikken.", new SpeechHelper.SpeechCompleteListener() {
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

    private void updateImageView(String intensity) {
        switch (intensity) {
            case "laagdrempelig":
                if (isInitialLowImage) {
                    low.setImageResource(R.drawable.intensity_low_selected);
                } else {
                    low.setImageResource(R.drawable.intensity_low);
                }
                isInitialLowImage = !isInitialLowImage;
                break;
            case "matig":
                if (isInitialMediumImage) {
                    medium.setImageResource(R.drawable.intensity_medium_selected);
                } else {
                    medium.setImageResource(R.drawable.intensity_medium);
                }
                isInitialMediumImage = !isInitialMediumImage;
                break;
            case "intens":
                if (isInitialHighImage) {
                    high.setImageResource(R.drawable.intensity_high_selected);
                } else {
                    high.setImageResource(R.drawable.intensity_high);
                }
                isInitialHighImage = !isInitialHighImage;
                break;
        }
    }

    @Override
    public void onSpeechResult(String result) {
        Log.d("LoIntensityActivity", "onSpeechResult: " + result);
        result = (result.trim().toLowerCase());
        if (result.contains("laag") || result.contains("drempelig")){
            toggleIntensitySelection(1);
            updateImageView("laagdrempelig");
            filterStatementsByIntensity();
            startNextActivity();
        } else if (result.contains("matig")){
            toggleIntensitySelection(2);
            updateImageView("matig");
            filterStatementsByIntensity();
            startNextActivity();
        } else if (result.contains("intens")){
            toggleIntensitySelection(3);
            updateImageView("intens");
            filterStatementsByIntensity();
            startNextActivity();
        } else {
            speechRecognitionManager.startListening();
        }
    }

    private void setButtonsClickable(boolean clickable) {
        low.setEnabled(clickable);
        medium.setEnabled(clickable);
        high.setEnabled(clickable);
        hearButton.setEnabled(clickable);
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