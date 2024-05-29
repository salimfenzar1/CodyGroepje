package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.SpeechHelper;
import com.example.codycactus.R;

import java.util.Random;

public class WvieMakeChoiceActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_make_choice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_make_choice), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(this::speakText, 2000);
        new Handler().postDelayed(() -> {
            Random random = new Random();
            // starting activity based on what boolean is generated
            Intent intent;
            if (random.nextBoolean()) {
                intent = new Intent(this, WvieChoiceRedActivity.class);
            } else {
                intent = new Intent(this, WvieChoiceYellowActivity.class);
            }
            startActivity(intent);
            finish();
        }, 10000);

    }
    public void speakText(){
        speechHelper = new SpeechHelper(this);
        speechHelper.speak(" Kies nu aan welke kant van mij je gaat staan", new SpeechHelper.SpeechCompleteListener() {
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
