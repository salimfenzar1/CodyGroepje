package com.example.watVindIkErger;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.AnswerConverter;
import com.example.ImageUtils;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;

public class WvieChoiceYellowActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton yesButton;
    private ImageButton noButton;
    private ImageButton hearButton;
    private ImageView statementImageView;
    private ArrayList<Statement> filteredStatements;
    private Statement yellowStatement;
    private Statement redStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_choice_yellow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_choice_yellow), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);


        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        yellowStatement = intent.getParcelableExtra("yellow_statement");
        redStatement = intent.getParcelableExtra("red_statement");

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        statementImageView = findViewById(R.id.image_view_foto_yellow_choice);

        if (yellowStatement != null) {
            int resId = getResources().getIdentifier(yellowStatement.imageUrl, "drawable", getPackageName());
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, statementImageView.getWidth(), statementImageView.getHeight());
            statementImageView.setImageBitmap(bitmap);
        }

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goYellowActivity();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRedActivity();
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
        setButtonsClickable(false);
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Heeft er iemand voor geel gekozen?", new SpeechHelper.SpeechCompleteListener() {
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

    private void setButtonsClickable(boolean clickable) {
        yesButton.setEnabled(clickable);
        noButton.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        switch (AnswerConverter.determineAnswer(result)) {
            case YES: goYellowActivity(); break;
            case NO: goRedActivity(); break;
            default: speechRecognitionManager.startListening(); break;
        }
    }
    private void goYellowActivity() {
        Intent intent = new Intent(getApplicationContext(), WvieExplanationYellowActivity.class);
        intent.putExtra("selectedYes", true);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
    }

    private void goRedActivity() {
        Intent intent = new Intent(getApplicationContext(), WvieExplanationRedActivity.class);
        intent.putExtra("selectedYes", false);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
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
