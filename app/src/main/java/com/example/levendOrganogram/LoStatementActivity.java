package com.example.levendOrganogram;

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

import com.example.utils.ImageUtils;
import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.services.SpeechRecognitionManager;
import com.example.codycactus.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class LoStatementActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener{
    private ImageButton next;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement statement;
    private boolean askingForClarity = false;
    private final LoStatementActivity context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_statement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_statement), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        Log.d("LoStatementActivity", "Received filtered statements: " + filteredStatements);

        statementImageView = findViewById(R.id.image_view_foto_case);

        if (filteredStatements != null && !filteredStatements.isEmpty()) {
            Collections.shuffle(filteredStatements);
            statement = filteredStatements.remove(0);  // Get a random statement and remove it from the list
            statement.isActive = false;  // Mark the statement as inactive
            Log.d("LoStatementActivity", "Selected red statement: " + statement.description);

            ViewTreeObserver vto = statementImageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    statementImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = statementImageView.getWidth();
                    int height = statementImageView.getHeight();

                    if (statement.getImageUrl().startsWith("gs://") || statement.getImageUrl().startsWith("https://")) {
                        // Load image from Firebase Storage
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(statement.getImageUrl());
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).resize(width, height).centerInside().into(statementImageView);
                        }).addOnFailureListener(exception -> {
                            Log.e("LoStatementActivity", "Failed to load image from Firebase Storage", exception);
                        });
                    } else {
                        // Load image from drawable
                        int resId = getResources().getIdentifier(statement.imageUrl, "drawable", getPackageName());
                        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                        statementImageView.setImageBitmap(bitmap);
                    }
                }
            });
        } else {
            Log.d("LoStatementActivity", "No statements available.");
        }


        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoChooseDistanceActivity.class);
                intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
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

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }

        speechHelper = new SpeechHelper(this);

        if (statement != null) {
            speechHelper.speak(statement.description, new SpeechHelper.SpeechCompleteListener() {
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
            speechHelper.speak("Geen stelling beschikbaar.", new SpeechHelper.SpeechCompleteListener() {
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
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }

        askingForClarity = true;
        speechHelper.speak("Is de stelling duidelijk?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                speechRecognitionManager = new SpeechRecognitionManager(context, context);
                speechRecognitionManager.startListening();            }
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
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        Intent intent = new Intent(getApplicationContext(), LoChooseDistanceActivity.class);
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
