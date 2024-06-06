package com.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.codycactus.R;
import com.example.watVindIkErger.WvieSubjectsActivity;

public class MainActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private ImageButton tijdTikt;
    private ImageButton levend;
    private ImageButton watVind;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;

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
        watVind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Je hebt wat vind ik erger gekozen", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieSubjectsActivity.class);
                startActivity(intent);
            }
        });

        setButtonsClickable(false);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionToRecordAccepted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (permissionToRecordAccepted) {
            speechRecognitionManager = new SpeechRecognitionManager(this, this);
            speakIntro();
        } else {
            Toast.makeText(this, "Permission to use microphone denied", Toast.LENGTH_SHORT).show();
            finish(); // Close the app if permission is denied
        }
    }

    public void speakIntro() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Hoi, ik ben Cody! jullie kunnen samen met mij een spel spelen. Deze spellen zullen het mogelijk maken om moeilijke onderwerpen bespreekbaar te maken. Jullie kunnen kiezen tussen: De Tijd Tikt ,  levend organogram, en wat vind ik erger! Welk spel willen jullie spelen?", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);  // Zet de knoppen klikbaar
                Log.d("MainActivity", "Buttons should be clickable now.");
                speechRecognitionManager.startListening();
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);  // Zet de knoppen klikbaar zelfs als de spraaksynthese mislukt
                Log.d("MainActivity", "Buttons should be clickable now even after speech failure.");
                speechRecognitionManager.startListening();
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
        Log.d("MainActivity", "setButtonsClickable: " + clickable);
    }

    @Override
    public void onSpeechResult(String result) {
        Log.i("SpeechRecognizer", "Recognized speech: " + result);
        if ("wat vind ik erger".equalsIgnoreCase(result.trim())) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
            Intent intent = new Intent(getApplicationContext(), WvieSubjectsActivity.class);
            startActivity(intent);
        }
        speechRecognitionManager.startListening(); // Restart listening after receiving results
    }

    @Override
    protected void onDestroy() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
