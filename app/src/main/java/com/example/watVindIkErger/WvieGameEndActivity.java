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

import com.example.MainActivity;
import com.example.SpeechHelper;
import com.example.codycactus.R;

public class WvieGameEndActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private ImageButton home;
    private ImageButton replay;
    private ImageButton hearButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_game_end);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_game_end), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // If home button is clicked
        home = findViewById(R.id.homeButton);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op home geklikt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // If replay button is clicked
        replay = findViewById(R.id.againButton);

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op replay geklikt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieGetReadyActivity.class);
                startActivity(intent);
            }
        });

        hearButton = findViewById(R.id.hearButton);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        new Handler().postDelayed(this::speakText, 2000);

    }
    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie nog een ronde spelen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
            }
        });
    }
}
