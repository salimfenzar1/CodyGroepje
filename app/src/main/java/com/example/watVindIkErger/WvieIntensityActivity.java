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
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;
import android.Manifest;
import java.util.ArrayList;
import java.util.List;

public class WvieIntensityActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private ImageView low;
    private ImageView medium;
    private ImageView high;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
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
                    low.setImageResource(R.drawable.intensity_low_selected);
                    isInitialLowImage = false;
                } else {
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
                    medium.setImageResource(R.drawable.intensity_medium_selected);
                    isInitialMediumImage = false;
                } else {
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
                    high.setImageResource(R.drawable.intensity_high_selected);
                    isInitialHighImage = false;
                } else {
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

        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        new Handler().postDelayed(this::speakText, 2000);
    }

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

    private void updateToNextPageButtonState() {
        next.setEnabled(!selectedIntensities.isEmpty());
    }

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
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = result.trim().toLowerCase();
        switch (result) {
            case "laagdrempellig":
                toggleIntensitySelection("laagdrempellig");
                low.setImageResource(isInitialLowImage ? R.drawable.intensity_low_selected : R.drawable.intensity_low);
                isInitialLowImage = !isInitialLowImage;
                break;
            case "matig":
                toggleIntensitySelection("matig");
                medium.setImageResource(isInitialMediumImage ? R.drawable.intensity_medium_selected : R.drawable.intensity_medium);
                isInitialMediumImage = !isInitialMediumImage;
                break;
            case "intens":
                toggleIntensitySelection("intens");
                high.setImageResource(isInitialHighImage ? R.drawable.intensity_high_selected : R.drawable.intensity_high);
                isInitialHighImage = !isInitialHighImage;
                break;
            case "laagdrempellig en matig":
                toggleIntensitySelection("laagdrempellig");
                low.setImageResource(isInitialLowImage ? R.drawable.intensity_low_selected : R.drawable.intensity_low);
                isInitialLowImage = !isInitialLowImage;
                toggleIntensitySelection("matig");
                medium.setImageResource(isInitialMediumImage ? R.drawable.intensity_medium_selected : R.drawable.intensity_medium);
                isInitialMediumImage = !isInitialMediumImage;
                break;
            // Add more combinations if needed
            default:
                Toast.makeText(this, "Onbekende intensiteit: " + result, Toast.LENGTH_SHORT).show();
                break;
        }
        updateToNextPageButtonState();
        speechRecognitionManager.startListening(); // Restart listening after processing result
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
    }
}
