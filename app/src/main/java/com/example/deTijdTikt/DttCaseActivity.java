package com.example.deTijdTikt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ImageUtils;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;
import com.example.watVindIkErger.WvieChoiceRedActivity;
import com.example.watVindIkErger.WvieStatementYellowActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DttCaseActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private ImageButton next;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private final DttCaseActivity context = this;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement chosenCase;
    private boolean askingForClarity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtt_case);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        Log.d("DttCaseActivity", "Received filtered statements: " + filteredStatements);

        statementImageView = findViewById(R.id.image_view_foto_case);

        if (filteredStatements != null && !filteredStatements.isEmpty()) {
            Collections.shuffle(filteredStatements);
            chosenCase = filteredStatements.remove(0);  // Get a random statement and remove it from the list
            chosenCase.isActive = false;  // Mark the statement as inactive
            Log.d("DttCaseActivity", "Selected statement: " + chosenCase.imageUrl + chosenCase.description);

            ViewTreeObserver vto = statementImageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    statementImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = statementImageView.getWidth();
                    int height = statementImageView.getHeight();
                    loadImage(chosenCase.imageUrl, width, height);
                }
            });
        } else {
            Log.d("DttCaseActivity", "No statements available.");
        }
        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage();
                finish(); // Close current activity to free up resources
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

        new Handler().postDelayed(this::speakText, 2000);
    }

    private void loadImage(String imageUrl, int width, int height) {
        int resId = getResources().getIdentifier(imageUrl, "drawable", getPackageName());
        if (resId != 0) {
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
            statementImageView.setImageBitmap(bitmap);
        } else {
            Log.e("DttCaseActivity", "Image resource not found for: " + imageUrl);
        }
    }

    public void speakText() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        speechHelper = new SpeechHelper(this);

        if (chosenCase != null) {
            speechHelper.speak("De casus is:  " + chosenCase.description, new SpeechHelper.SpeechCompleteListener() {
                @Override
                public void onSpeechComplete() {
                    Log.d("Speech", "Speech synthesis voltooid");
                    setButtonsClickable(true);
                    askIfClear();
                }

                @Override
                public void onSpeechFailed() {
                    Log.e("Speech", "Speech synthesis mislukt");
                    setButtonsClickable(true);
                    askIfClear();
                }
            });
        } else {
            speechHelper.speak("Geen casus beschikbaar.", new SpeechHelper.SpeechCompleteListener() {
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
    }

    public void askIfClear() {

        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();

        askingForClarity = true;
        speechHelper.speak("Heeft iedereen de casus begrepen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }
        });
    }

    private void goToNextPage() {
        Random random = new Random();
        Intent nextIntent;
        if (random.nextBoolean()) {
            nextIntent = new Intent(this, DttExplanationRedActivity.class);
        } else {
            nextIntent = new Intent(this, DttExplanationYellowActivity.class);
        }
        nextIntent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        nextIntent.putExtra("chosenCase", chosenCase);
        nextIntent.putExtra("isFirst", true);
        startActivity(nextIntent);
        finish();
    }

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = (result.trim().toLowerCase());
        if (askingForClarity) {
            if (result.contains("ja")) {
                askingForClarity = false;
                goToNextPage();
            } else if (result.contains("nee")) {
                askingForClarity = false;
                speakText();
            } else {
                Toast.makeText(this, "Kun je dat alsjeblieft herhalen?", Toast.LENGTH_SHORT).show();
                restartListening();
            }
        } else {
            Toast.makeText(this, "Kun je dat alsjeblieft herhalen?", Toast.LENGTH_SHORT).show();
            restartListening();
        }
    }

    private void restartListening() {
        speechRecognitionManager.stopListening();
        new Handler().postDelayed(() -> speechRecognitionManager.startListening(), 1000);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
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
