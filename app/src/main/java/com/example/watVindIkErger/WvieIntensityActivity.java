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

import com.example.SpeechHelper;
import com.example.SpeechRecognitionManager;
import com.example.codycactus.R;

public class WvieIntensityActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {
    private ImageView medium;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private ImageButton hearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_intensity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_intensity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speechRecognitionManager = new SpeechRecognitionManager(this, this);

        medium = findViewById(R.id.image_view_medium_intensity);

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieTutorialActivity.class);
                startActivity(intent);
            }
        });

        hearButton = findViewById(R.id.hearButton);
        setButtonsClickable(false);
        hearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(false);
                speakText();
            }
        });

        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("In welke mate van intensiteit willen jullie de stellingen ? Je kan kiezen tussen laagdrempelig, matig, intens of een combinatie hiervan!", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
//                speechRecognitionManager.startListening();
            }
            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
//                speechRecognitionManager.startListening();
            }
        });
    }
    private void setButtonsClickable(boolean clickable) {
        medium.setEnabled(clickable);
        hearButton.setEnabled(clickable);

    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("WvieIntensityActivity", "Recognized speech: " + result);
        String trimmedResult = result.trim();
        switch(trimmedResult.toLowerCase()) {
            case "laagdrempellig":
                // Result if user says "Laagdrempellig"
                break;
            case "middelmatig":
                // Result if user says "Middelmatig"
                break;
            case "intens":
                // Result if user says "Intens"
                break;
            default:
                // unexpected input
        }
    }
}
