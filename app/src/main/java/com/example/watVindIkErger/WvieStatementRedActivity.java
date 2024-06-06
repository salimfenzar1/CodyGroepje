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

public class WvieStatementRedActivity extends AppCompatActivity {
    private ImageButton next;
    private SpeechHelper speechHelper;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement redStatement;

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

        statementImageView = findViewById(R.id.image_view_foto_statement_red);

        if (filteredStatements != null && !filteredStatements.isEmpty()) {
            Collections.shuffle(filteredStatements);
            redStatement = filteredStatements.remove(0);  // Get a random statement and remove it from the list
            redStatement.isActive = false;  // Mark the statement as inactive
            Log.d("WvieStatementRedActivity", "Selected red statement: " + redStatement.description);

            ViewTreeObserver vto = statementImageView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
            Log.d("WvieStatementRedActivity", "No statements available.");
        }

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNextActivity();
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
    }

    private void goNextActivity() {
        Intent intent = new Intent(getApplicationContext(), WvieStatementYellowActivity.class);
        startActivity(intent);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        WvieStatementRedActivity currentActivity = this;
        speechHelper.speak("De stelling voor de kleur rood... Een zorgmedewerker betrapt twee collega's op intiem contact", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                new Handler().postDelayed(currentActivity::goNextActivity, 5000);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                new Handler().postDelayed(currentActivity::goNextActivity, 5000);
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }
}
