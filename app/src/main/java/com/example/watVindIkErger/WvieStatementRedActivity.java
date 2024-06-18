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
import java.util.Collections;

public class WvieStatementRedActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private ImageButton next;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement redStatement;
    private boolean askingForClarity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_statement_red);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_statement_red), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        Log.d("WvieStatementRedActivity", "Received filtered statements: " + filteredStatements);

        statementImageView = findViewById(R.id.image_view_foto_statement_red);

        if (filteredStatements != null) {
            // Check if there are less than 2 active statements
            long activeCount = filteredStatements.stream().filter(Statement::isActive).count();
            if (activeCount < 2) {
                // Reset all statements to active
                for (Statement statement : filteredStatements) {
                    statement.setActive(true);
                }
            }

            // Filter active statements
            ArrayList<Statement> activeStatements = new ArrayList<>();
            for (Statement statement : filteredStatements) {
                if (statement.isActive()) {
                    activeStatements.add(statement);
                }
            }

            if (!activeStatements.isEmpty()) {
                Collections.shuffle(activeStatements);
                redStatement = activeStatements.remove(0);  // Get a random active statement and remove it from the list
                redStatement.setActive(false);  // Mark the statement as inactive
                Log.d("WvieStatementRedActivity", "Selected red statement: " + redStatement.description);

                // Load the image only after the layout has been laid out
                statementImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        statementImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = statementImageView.getWidth();
                        int height = statementImageView.getHeight();
                        int resId = getResources().getIdentifier(redStatement.imageUrl, "drawable", getPackageName());
                        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                        statementImageView.setImageBitmap(bitmap);
                    }
                });
            } else {
                Log.d("WvieStatementRedActivity", "No active statements available.");
            }
        } else {
            Log.d("WvieStatementRedActivity", "No statements available.");
        }

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognitionManager.stopListening();
                speechRecognitionManager.destroy();
                Intent intent = new Intent(getApplicationContext(), WvieStatementYellowActivity.class);
                intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
                intent.putExtra("red_statement", redStatement);
                startActivity(intent);
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

        speechRecognitionManager = new SpeechRecognitionManager(this, this);
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);

        if (redStatement != null) {
            speechHelper.speak("De stelling voor de kleur rood... " + redStatement.description, new SpeechHelper.SpeechCompleteListener() {
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
                }
            });
        } else {
            speechHelper.speak("Geen stelling beschikbaar voor de kleur rood.", new SpeechHelper.SpeechCompleteListener() {
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
        askingForClarity = true;
        speechHelper.speak("Is de stelling duidelijk?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                speechRecognitionManager.startListening();
            }
        });
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
                speechRecognitionManager.startListening();
            }
        } else {
            speechRecognitionManager.startListening();
        }
    }

    private void goToNextPage() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        Intent intent = new Intent(getApplicationContext(), WvieStatementYellowActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        intent.putExtra("red_statement", redStatement);
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
