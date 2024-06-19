package com.example.levendOrganogram;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.Statement;
import com.example.services.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;

public class LoChooseDistanceActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
    private ImageButton hearButton;
    private CountDownTimer countDownTimer;
    private ArrayList<Statement> filteredStatements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lo_choose_distance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lo_choose_distance), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hearButton = findViewById(R.id.hearButton);

        Intent intent = getIntent();
        filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {


        setButtonsClickable(false);
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Als je het eens bent kom dan dichterbij staan. Als je het oneens bent, ga dan verder van mij af staan ", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
                startCountdown(); // countdown starts after speech is complete
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
                startCountdown();
            }
        });
    }

    private void setButtonsClickable(boolean clickable) {
        hearButton.setEnabled(clickable);
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(10000, 1000) { // 10 seconds timer
            public void onTick(long millisUntilFinished) {
                Log.d("Countdown", "Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                navigateToNextActivity();
            }
        }.start();
    }

    private void navigateToNextActivity() {
        Intent intent = new Intent(this, LoChoicesActivity.class);
        intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechHelper != null) {
            speechHelper.close();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
