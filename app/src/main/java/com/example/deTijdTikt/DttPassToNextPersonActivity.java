package com.example.deTijdTikt;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codycactus.R;

public class DttPassToNextPersonActivity extends AppCompatActivity {
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
    }
}