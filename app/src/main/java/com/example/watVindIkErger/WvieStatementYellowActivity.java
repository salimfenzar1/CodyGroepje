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
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.Collections;

public class WvieStatementYellowActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private ImageButton next;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement yellowStatement;
    private Statement redStatement;

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
        Log.d("WvieStatementYellowActivity", "Red statement: " + redStatement);

        statementImageView = findViewById(R.id.image_view_foto_statement_yellow);

        if (filteredStatements != null && !filteredStatements.isEmpty()) {
            Collections.shuffle(filteredStatements);
            yellowStatement = filteredStatements.remove(0);  // Get a random statement and remove it from the list
            yellowStatement.isActive = false;  // Mark the statement as inactive
            int resId = getResources().getIdentifier(yellowStatement.imageUrl, "drawable", getPackageName());
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, statementImageView.getWidth(), statementImageView.getHeight());
            statementImageView.setImageBitmap(bitmap);

            Log.d("WvieStatementYellowActivity", "Selected yellow statement: " + yellowStatement.description);

            ViewTreeObserver vto = statementImageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    statementImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = statementImageView.getWidth();
                    int height = statementImageView.getHeight();
                    int resId = getResources().getIdentifier(yellowStatement.imageUrl, "drawable", getPackageName());
                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, width, height);
                    statementImageView.setImageBitmap(bitmap);
                }
            });
        } else {
            Log.d("WvieStatementYellowActivity", "No statements available.");
        }

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieMakeChoiceActivity.class);
                intent.putExtra("red_statement", redStatement);
                intent.putExtra("yellow_statement", yellowStatement);
                intent.putExtra("filtered_statements", filteredStatements);
                startActivity(intent);
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
        speechHelper = new SpeechHelper(this);
        if (yellowStatement != null) {
            speechHelper.speak("De stelling voor de kleur geel... " + yellowStatement.description, new SpeechHelper.SpeechCompleteListener() {
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

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }
}
