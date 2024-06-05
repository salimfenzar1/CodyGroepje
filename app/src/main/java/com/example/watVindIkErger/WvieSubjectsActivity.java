package com.example.watVindIkErger;

import android.content.Intent;
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

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;

public class WvieSubjectsActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private ImageButton hearButton;
    private ImageView themeDecease;
    private ImageView themeSexuality;
    private List<Statement> allStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_subjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_subjects), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hearButton = findViewById(R.id.hearButton);
        themeDecease = findViewById(R.id.image_view_family);
        themeSexuality = findViewById(R.id.image_seksualiteit);

        allStatements = getIntent().getParcelableArrayListExtra("statements");

        setButtonsClickable(false);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakText();
            }
        });

        new Handler().postDelayed(this::speakText, 2000);

        themeDecease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                filterAndNavigate("Overlijden");
            }
        });

        themeSexuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                filterAndNavigate("Seksualiteit op de werkvloer");
            }
        });
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie stellingen over het onderwerp: seksualiteit op de werkvloer , overlijden , of allebei!?", new SpeechHelper.SpeechCompleteListener() {
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
        themeDecease.setEnabled(clickable);
        themeSexuality.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }

    private void filterAndNavigate(String category) {
        List<Statement> filteredStatements = new ArrayList<>();
        for (Statement statement : allStatements) {
            if (statement.category.equals(category)) {
                filteredStatements.add(statement);
            }
        }
        Intent intent = new Intent(getApplicationContext(), WvieIntensityActivity.class);
        intent.putParcelableArrayListExtra("statements", new ArrayList<>(filteredStatements));
        startActivity(intent);
    }
}
