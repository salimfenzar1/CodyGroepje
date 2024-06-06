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
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

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
    private String[] intensities = {"laagdrempelig", "matig", "intens"};
    private int currentIntensityIndex = 0;

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
                toggleIntensitySelection("laagdrempelig");
                updateImageView("laagdrempelig");
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection("matig");
                updateImageView("matig");
            }
        });

        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleIntensitySelection("intens");
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
                askToProceed();
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
        speechHelper.speak("In welke mate van intensiteit willen jullie de stellingen? Je kan kiezen tussen laagdrempelig, matig, intens of een combinatie hiervan!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                askForConfirmation();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                askForConfirmation();
            }
        });
    }

    private void askForConfirmation() {
        if (currentIntensityIndex < intensities.length) {
            String currentIntensity = intensities[currentIntensityIndex];
            speechHelper.speak("Wil je " + currentIntensity + " selecteren?", new SpeechHelper.SpeechCompleteListener() {
                @Override
                public void onSpeechComplete() {
                    Log.d("WvieIntensityActivity", "Asking for confirmation: " + currentIntensity);
                    speechRecognitionManager.setConfirmationResultListener(new SpeechRecognitionManager.ConfirmationResultListener() {
                        @Override
                        public void onConfirmationResult(String result) {
                            Log.d("WvieIntensityActivity", "Received confirmation result: " + result);
                            if (result.equalsIgnoreCase("ja")) {
                                Log.d("WvieIntensityActivity", "User confirmed selection: " + currentIntensity);
                                toggleIntensitySelection(currentIntensity);
                                updateImageView(currentIntensity);
                            } else {
                                Log.d("WvieIntensityActivity", "User declined selection: " + currentIntensity);
                            }
                            currentIntensityIndex++;
                            if (currentIntensityIndex < intensities.length) {
                                askForConfirmation();
                            } else {
                                askToProceed();
                            }
                            speechRecognitionManager.setConfirmationResultListener(null);
                        }
                    });
                    speechRecognitionManager.startListening();
                }

                @Override
                public void onSpeechFailed() {
                    speechRecognitionManager.startListening();
                }
            });
        } else {
            askToProceed();
        }
    }

    private void askToProceed() {
        speechHelper.speak("Wil je nu doorgaan?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("WvieIntensityActivity", "Asking to proceed");
                speechRecognitionManager.setConfirmationResultListener(new SpeechRecognitionManager.ConfirmationResultListener() {
                    @Override
                    public void onConfirmationResult(String result) {
                        Log.d("WvieIntensityActivity", "Received proceed result: " + result);
                        if (result.equalsIgnoreCase("ja")) {
                            startNextActivity();
                        } else {
                            currentIntensityIndex = 0;
                            askForConfirmation();
                        }
                        speechRecognitionManager.setConfirmationResultListener(null);
                    }
                });
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
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
        Log.d("WvieIntensityActivity", "onSpeechResult: " + result);
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
