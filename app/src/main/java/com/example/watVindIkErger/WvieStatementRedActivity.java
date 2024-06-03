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

import com.example.SpeechHelper;
import com.example.codycactus.R;

public class WvieStatementRedActivity extends AppCompatActivity {
    private ImageButton next;
    private SpeechHelper speechHelper;
    private ImageButton hearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_statement_red);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_statement_red), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        next = findViewById(R.id.nextButton);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieStatementYellowActivity.class);
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
        speechHelper.speak("De stelling voor de kleur rood... Een zorgmedewerker betrapt twee collega's op intiem contact", new SpeechHelper.SpeechCompleteListener() {
            @Override
            public void onSpeechComplete() {
                Log.d("Speech", "Speech synthesis voltooid");
                setButtonsClickable(true);
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);
            }
        });
    }
    private void setButtonsClickable(boolean clickable) {
        next.setEnabled(clickable);
        hearButton.setEnabled(clickable);
    }
}
