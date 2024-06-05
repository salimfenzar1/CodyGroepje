package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WvieGetReadyActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private ImageButton next;
    private ImageButton hearButton;
    private ArrayList<Statement> filteredStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_get_ready);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_get_ready), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        // Ensure filteredStatements is not null
        if (filteredStatements == null) {
            filteredStatements = new ArrayList<>();
        }

        // Filter the statements to only include active ones
        List<Statement> activeStatements = filteredStatements.stream()
                .filter(Statement::isActive)
                .collect(Collectors.toList());

        filteredStatements = new ArrayList<>(activeStatements);

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
            Intent nextIntent = new Intent(getApplicationContext(), WvieStatementRedActivity.class);
            nextIntent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
            startActivity(nextIntent);
        });

        hearButton = findViewById(R.id.hearButton);
        setButtonsClickable(false);
        hearButton.setOnClickListener(v -> {
            setButtonsClickable(false);
            speakText();
        });

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Staat iedereen klaar?", new SpeechHelper.SpeechCompleteListener() {
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

    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }
}
