package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieStatementRedActivity.class);
                intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
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
