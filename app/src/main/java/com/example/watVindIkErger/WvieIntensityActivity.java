package com.example.watVindIkErger;

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

import com.example.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;

public class WvieIntensityActivity extends AppCompatActivity {
    private ImageView low;
    private ImageView medium;
    private ImageView high;
    private SpeechHelper speechHelper;
    private ImageButton hearButton;
    private ImageButton next;
    private List<String> selectedIntensities;
    private boolean isInitialLowImage = true;
    private boolean isInitialMediumImage = true;
    private boolean isInitialHighImage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_intensity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_intensity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedIntensities = new ArrayList<>();

        low = findViewById(R.id.image_view_low_intensity);
        medium = findViewById(R.id.image_view_medium_intensity);
        high = findViewById(R.id.image_view_high_intensity);

        low.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                toggleIntensitySelection("laagdrempellig");
                if (isInitialLowImage) {
                    // Wijzig naar de tweede afbeelding
                    low.setImageResource(R.drawable.intensity_medium);
                    isInitialLowImage = false;
                } else {
                    // Wijzig terug naar de initiële afbeelding
                    low.setImageResource(R.drawable.intensity_low);
                    isInitialLowImage = true;
                }
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection("matig");
                if (isInitialMediumImage) {
                    // Wijzig naar de tweede afbeelding
                    medium.setImageResource(R.drawable.intensity_high);
                    isInitialMediumImage = false;
                } else {
                    // Wijzig terug naar de initiële afbeelding
                    medium.setImageResource(R.drawable.intensity_medium);
                    isInitialMediumImage = true;
                }
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection("intens");
                if (isInitialHighImage) {
                    // Wijzig naar de tweede afbeelding
                    high.setImageResource(R.drawable.intensity_low);
                    isInitialHighImage = false;
                } else {
                    // Wijzig terug naar de initiële afbeelding
                    high.setImageResource(R.drawable.intensity_high);
                    isInitialHighImage = true;
                }
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
                    startNextActivity();
                } else {
                    Toast.makeText(WvieIntensityActivity.this, "Selecteer minimaal één intensiteit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Handler().postDelayed(this::speakText, 2000);
    }

    // Intensity selection toggle
    private void toggleIntensitySelection(String intensity) {
        if (selectedIntensities.contains(intensity)) {
            selectedIntensities.remove(intensity);
            Log.d("WvieIntensityActivity", "Removed intensity level: " + intensity + ". Current selection: " + selectedIntensities);
        } else {
            selectedIntensities.add(intensity);
            Log.d("WvieIntensityActivity", "Added intensity level: " + intensity + ". Current selection: " + selectedIntensities);
        }
        updateToNextPageButtonState();
    }

    // Enable next button if at least one intensity is selected
    private void updateToNextPageButtonState() {
        next.setEnabled(!selectedIntensities.isEmpty());
    }

    // Start next activity + send intensity level data
    private void startNextActivity() {
        Intent intent = new Intent(this, WvieTutorialActivity.class);
        intent.putStringArrayListExtra("SELECTED_INTENSITIES", new ArrayList<>(selectedIntensities));
        startActivity(intent);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak(" In welke mate van intensiteit willen jullie de stellingen ? Je kan kiezen tussen laagdrempelig, matig, intens of een combinatie hiervan!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        low.setEnabled(clickable);
        medium.setEnabled(clickable);
        high.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }
}
