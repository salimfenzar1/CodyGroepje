package com.example.deTijdTikt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.Random;

public class DttPassToNextPersonActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;

    private ArrayList<Statement> filteredStatements;


    private final String[] optionsPassToNextPerson = {
            "waarvan jij denkt dat hij/zij er totaal anders over denkt",
            "die vandaag al het langst aan het werk is",
            "die je beter wilt leren kennen",
            "van het management",
            "die jonger is dan jij",
            "die net nieuw is in dit zorg team",
            "die kleiner is dan jij",
            "die volgens jou de grappigste collega is",
            "die vegetarisch of vegan is",
            "die ouder is dan jij",
            "met een andere haarkleur",
            "die het langst werkzaam is op deze locatie",
            "die een instrument bespeelt",
            "die het verst van deze werkplaats af woont",
            "die je al minimaal 3 jaar kent",
            "naar keuze"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dtt_pass_to_next_person);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        speakText();
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);

        Random random = new Random();
        int randomIndex = random.nextInt(optionsPassToNextPerson.length);
        String randomOption = optionsPassToNextPerson[randomIndex];
        DttPassToNextPersonActivity currentActivity = this;
        speechHelper.speak("De personen die nu de ballen in hun hand hebben, gooi deze over naar " + randomOption, new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                new Handler().postDelayed(currentActivity::goNextActivity, 2000);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
            }
        });
    }

    private void goNextActivity() {
        Random random = new Random();
        Intent intent;
        if (random.nextBoolean()){
            intent = new Intent(getApplicationContext(), DttExplanationRedActivity.class);
        }else{
            intent = new Intent(getApplicationContext(), DttExplanationYellowActivity.class);
        }
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        intent.putExtra("hasPassedToNextPerson", true);
        intent.putExtra("isFirst", true);
        startActivity(intent);
    }
}
