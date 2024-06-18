package com.example.deTijdTikt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DttStartPassingActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
    private ArrayList<Statement> filteredStatements;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dtt_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
        new Handler().postDelayed(this::speakText, 1000);
    }
    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Begin nu met overgooien!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                startTimer();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                speakText();
            }
        });
    }
    public void startTimer() {
        // Create a random duration between 5 and 10 seconds (5000 to 10000 milliseconds)
        Random random = new Random();
        int randomDuration = 7000 + random.nextInt(7000);

        mediaPlayer = MediaPlayer.create(this, R.raw.jeopardy);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            goNextActivity();
            Log.d("MediaPlayer", "Muziek is afgelopen");
        });

        handler.postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                goNextActivity();
            }
            Log.d("Timer", "Timer voltooid");
        }, randomDuration);
    }
    private void goNextActivity(){
        Intent intent = new Intent(getApplicationContext(), DttCaseActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}