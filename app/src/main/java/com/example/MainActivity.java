package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.DAO.DatabaseInitializer;
import com.example.DAO.StatementViewModel;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.codycactus.R;
import com.example.watVindIkErger.WvieSubjectsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton tijdTikt;
    private ImageButton levend;
    private ImageButton watVind;
    private SpeechHelper speechHelper;
    private StatementViewModel statementViewModel;
    private List<Statement> allStatements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tijdTikt = findViewById(R.id.tijdTikt);
        levend = findViewById(R.id.levendOrganogram);
        watVind = findViewById(R.id.watVindIk);

        setButtonsClickable(false);

        // Initialize the database if not already populated
        DatabaseInitializer.populateDatabase(this);

        // Initialize ViewModel
        statementViewModel = new ViewModelProvider(this).get(StatementViewModel.class);
        statementViewModel.getAllStatements().observe(this, statements -> {
            allStatements = new ArrayList<>(statements);
            // Ensure the resetAllStatements is called only after allStatements is initialized
            resetAllStatements();
        });

        speakIntro();

        tijdTikt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt gekozen voor de tijd tikt", Toast.LENGTH_SHORT).show();
            }
        });
        levend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt gekozen voor levend organogram", Toast.LENGTH_SHORT).show();
            }
        });
        watVind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt gekozen voor wat vind ik erger", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieSubjectsActivity.class);
                intent.putParcelableArrayListExtra("statements", new ArrayList<>(allStatements));
                startActivity(intent);
            }
        });
    }

    private void resetAllStatements() {
        statementViewModel.updateAllStatementsStatus(true);
//        Toast.makeText(MainActivity.this, "All statements have been reset to active", Toast.LENGTH_SHORT).show();
    }

    public void speakIntro() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Hoi, ik ben Cody! jullie kunnen samen met mij een spel spelen. Deze spellen zullen het mogelijk maken om moeilijke onderwerpen bespreekbaar te maken. Jullie kunnen kiezen tussen: De Tijd Tikt , levend organogram, en wat vind ik erger! Welk spel willen jullie spelen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                // Maak de knoppen klikbaar nadat de spraak voltooid is
                setButtonsClickable(true);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                // Maak de knoppen ook klikbaar als de spraak mislukt
                setButtonsClickable(true);
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        tijdTikt.setClickable(clickable);
        levend.setClickable(clickable);
        watVind.setClickable(clickable);
        tijdTikt.setEnabled(clickable);
        levend.setEnabled(clickable);
        watVind.setEnabled(clickable);
    }
}
