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

public class WvieStatementYellowActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton next;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement yellowStatement;
    private Statement redStatement;
    private boolean hasNavigated = false; // To prevent double navigation
    private boolean askingForClarity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_statement_yellow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_statement_yellow), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        redStatement = intent.getParcelableExtra("red_statement");

        Log.d("WvieStatementYellowActivity", "Filtered statements: " + filteredStatements);
        Log.d("WvieStatementYellowActivity", "Red statement: " + redStatement.description);

        statementImageView = findViewById(R.id.image_view_foto_statement_yellow);

        if (filteredStatements != null) {
            // Filter active statements
            ArrayList<Statement> activeStatements = new ArrayList<>();
            for (Statement statement : filteredStatements) {
                if (statement.isActive()) {
                    activeStatements.add(statement);
                }
            }

            if (!activeStatements.isEmpty()) {
                Collections.shuffle(activeStatements);
                yellowStatement = activeStatements.remove(0);  // Get a random active statement and remove it from the list
                yellowStatement.setActive(false);  // Mark the statement as inactive

                Log.d("WvieStatementYellowActivity", "Selected yellow statement: " + yellowStatement.description);

                // Load the image only after the layout has been laid out
                statementImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        statementImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = statementImageView.getWidth();
                        int height = statementImageView.getHeight();

                        if (yellowStatement.getImageUrl().startsWith("gs://") || yellowStatement.getImageUrl().startsWith("https://")) {
                            // Laad afbeelding van Firebase Storage
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(yellowStatement.getImageUrl());
                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                Log.d("WvieStatementYellowActivity", "Loading image from URL: " + uri.toString());
                                Picasso.get().load(uri).resize(width, height).centerInside().into(statementImageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("WvieStatementYellowActivity", "Image loaded successfully");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("WvieStatementYellowActivity", "Error loading image", e);
                                    }
                                });
                            }).addOnFailureListener(exception -> {
                                Log.e("WvieStatementYellowActivity", "Failed to get download URL from Firebase Storage", exception);
                            });
                        } else {
                            // Laad afbeelding van drawable
                            int resId = getResources().getIdentifier(yellowStatement.getImageUrl(), "drawable", getPackageName());
                            if (resId != 0) {
                                Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width / 2, height / 2); // Schaal naar beneden met de helft
                                statementImageView.setImageBitmap(bitmap);
                            } else {
                                Log.e("WvieStatementYellowActivity", "Drawable resource not found for: " + yellowStatement.getImageUrl());
                            }
                        }
                    }
                });
            } else {
                Log.d("WvieStatementYellowActivity", "No active statements available.");
            }
        } else {
            Log.d("WvieStatementYellowActivity", "No statements available.");
        }

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNextActivity();
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

    private void navigateToNextActivity() {
        speechRecognitionManager.stopListening();
        speechRecognitionManager.destroy();
        if (!hasNavigated) { // Ensure the activity transition happens only once
            hasNavigated = true;
            Intent intent = new Intent(getApplicationContext(), WvieMakeChoiceActivity.class);
            intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
            intent.putExtra("red_statement", redStatement);
            intent.putExtra("yellow_statement", yellowStatement);
            startActivity(intent);
        }
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        if (yellowStatement != null) {
            speechHelper.speak( yellowStatement.description, new SpeechHelper.SpeechCompleteListener() {
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
            speechHelper.speak("Geen stelling beschikbaar voor de kleur geel.", new SpeechHelper.SpeechCompleteListener() {
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

        if (askingForClarity) {
            if (result.equalsIgnoreCase("ja")) {
                askingForClarity = false;
                navigateToNextActivity();
            } else if (result.equalsIgnoreCase("nee")) {
                askingForClarity = false;
                speakText();
            } else {
                speechRecognitionManager.startListening();
            }
        } else {
            speechRecognitionManager.startListening();
        }
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
