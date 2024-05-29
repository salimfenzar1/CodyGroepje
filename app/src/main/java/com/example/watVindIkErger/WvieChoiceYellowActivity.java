package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codycactus.R;


public class WvieChoiceYellowActivity extends AppCompatActivity {

    private ImageButton yesButton;
    private ImageButton noButton;
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
        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt ja gekozen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieChoiceYellowActivity.class);
                intent.putExtra("selectedYes", true);
                startActivity(intent);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt nee gekozen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieChoiceRedActivity.class);
                intent.putExtra("selectedYes", false);
                startActivity(intent);
            }
        });
    }
}
