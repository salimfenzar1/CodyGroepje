package com.example.watVindIkErger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.SpeechHelper;
import com.example.codycactus.R;

public class WvieSubjectsActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
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
        new Handler().postDelayed(this::speakText, 2000);

    }
    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Willen jullie stellingen over het onderwerp: seksualiteit op de werkvloer , overlijden , of allebei!?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                Toast.makeText(getApplicationContext(), "Je hebt gekozen voor wat vind ik erger", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieIntensityActivity.class);
                startActivity(intent);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
            }
        });
    }
}
