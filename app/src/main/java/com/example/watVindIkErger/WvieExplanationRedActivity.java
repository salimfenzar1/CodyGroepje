package com.example.watVindIkErger;

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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ImageUtils;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;

public class WvieExplanationRedActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private boolean selectedYes;
    private ArrayList<Statement> filteredStatements;
    private Statement redStatement;
    private ImageView image_view_red;
    private boolean listeningForContinuation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_explanation_red);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_explanation_red), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            selectedYes = intent.getBooleanExtra("selectedYes", false);
            filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
            redStatement = intent.getParcelableExtra("red_statement");
        }

        image_view_red = findViewById(R.id.image_view_foto_explanation_red);
        if (redStatement != null) {
            Log.d("Image", "Image: " + redStatement.description + "");

            image_view_red.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    image_view_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int width = image_view_red.getWidth();
                    int height = image_view_red.getHeight();
                    int resId = getResources().getIdentifier(redStatement.imageUrl, "drawable", getPackageName());
                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                    image_view_red.setImageBitmap(bitmap);
                }
            });
        }

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextActivity();
            }
        });

        hearButton = findViewById(R.id.hearButton);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakText();
            }
        });

        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);

        // Initialize SpeechRecognitionManager
        speechRecognitionManager = new SpeechRecognitionManager(this, this);
        startListening();
    }

    private void startListening() {
        speechRecognitionManager.startListening();
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        String textToSpeak = selectedYes ? "Waarom vindt je deze stelling erger?" : "Waarom vindt je de rode stelling erger? Nadat iedereen is uitgepraat, kun je 'Wij willen doorgaan' zeggen om  door te gaan";
        speechHelper.speak(textToSpeak, new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                listeningForContinuation = true;
                startListening();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                listeningForContinuation = true;
                startListening();
            }
        });
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        if (listeningForContinuation && result.equalsIgnoreCase("Wij willen doorgaan")) {
            navigateToNextActivity();
        } else {
            startListening();
        }
    }

    private void navigateToNextActivity() {
        Toast.makeText(getApplicationContext(), "Navigating to the next activity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), WvieOtherOpinionsActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
        finish(); // Close current activity to free up resources
    }

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechHelper != null) {
            speechHelper.close();
        }
        if (speechRecognitionManager != null) {
            speechRecognitionManager.destroy();
        }
    }
}
