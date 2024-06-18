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

public class WvieExplanationYellowActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ImageView image_view_yellow;
    private boolean selectedYes;
    private ArrayList<Statement> filteredStatements;
    private Statement yellowStatement;
    private final WvieExplanationYellowActivity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_explanation_yellow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_explanation_yellow), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            selectedYes = intent.getBooleanExtra("selectedYes", false);
            filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
            yellowStatement = intent.getParcelableExtra("yellow_statement");
        }

        image_view_yellow = findViewById(R.id.image_view_foto_explanation_yellow);
        if (yellowStatement != null) {
            Log.d("Image", "Image: " + yellowStatement.description + "");

            image_view_yellow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    image_view_yellow.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int width = image_view_yellow.getWidth();
                    int height = image_view_yellow.getHeight();
                    int resId = getResources().getIdentifier(yellowStatement.imageUrl, "drawable", getPackageName());
                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                    image_view_yellow.setImageBitmap(bitmap);
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
    }

    private void startListening() {
        speechRecognitionManager.startListening();
    }

    public void speakText() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        speechHelper = new SpeechHelper(this);
        String textToSpeak = "Waarom vindt je de gele stelling erger? Nadat iedereen is uitgepraat, kun je 'Wij willen doorgaan' zeggen om door te gaan";
        speechHelper.speak(textToSpeak, new SpeechHelper.SpeechCompleteListener() {
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

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        result = (result.trim().toLowerCase());
        if (result.contains("willen") || result.contains("doorgaan")) {
            navigateToNextActivity();
        } else {
            startListening();
        }
    }

    private void navigateToNextActivity() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
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
