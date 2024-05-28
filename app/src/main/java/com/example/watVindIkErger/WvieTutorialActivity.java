package com.example.watVindIkErger;

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

public class WvieTutorialActivity extends AppCompatActivity {

    private SpeechHelper speechHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_tutorial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_tutorial), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        new Handler().postDelayed(this::speakText, 2000);
    }

    public void speakText() {
        speechHelper = new SpeechHelper(this);
        speechHelper.speak("Welkom bij het spel: Wat vind ik erger! Ik licht kort toe wat we gaan doen. Ik lees dadelijk twee stellingen voor, deze zijn gekoppeld aan een kleur. Mijn linker kant is geel en mijn  rechter kant is rood. Vervolgens kiezen jullie welke van de twee stellingen je erger vindt en ga je aan deze kant van mij staan. Daarna zullen we discussiëren over waarom je deze stelling erger vindt...", new SpeechHelper.SpeechCompleteListener() {
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
