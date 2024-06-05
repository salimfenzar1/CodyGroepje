package com.example.watVindIkErger;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.ImageUtils;
import com.example.Model.Statement;
import com.example.SpeechHelper;
import com.example.codycactus.R;

import java.util.ArrayList;

public class WvieExplanationYellowActivity extends AppCompatActivity {
    private SpeechHelper speechHelper;
    private ImageButton next;
    private ImageButton hearButton;
    private ImageView image_view_yellow;
    private boolean selectedYes;
    private ArrayList<Statement> filteredStatements;
    private Statement yellowStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wvie_explanation_yellow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wvie_explanation_yellow), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null) {
            selectedYes = intent.getBooleanExtra("selectedYes", false);
            filteredStatements = intent.getParcelableArrayListExtra("filtered_statements");
        }
        Intent intent2 = getIntent();
        if (intent2 != null) {
            yellowStatement = intent2.getParcelableExtra("yellow_statement");
        }

        image_view_yellow = findViewById(R.id.image_view_foto_explanation_yellow);
        if (yellowStatement != null) {
            int resId = getResources().getIdentifier(yellowStatement.imageUrl, "drawable", getPackageName());
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), resId, image_view_yellow.getWidth(), image_view_yellow.getHeight());
            image_view_yellow.setImageBitmap(bitmap);
        }

        next = findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "je hebt op de volgende pagina gedrukt", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WvieOtherOpinionsActivity.class);
                intent.putParcelableArrayListExtra("filtered_statements", filteredStatements);
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

    public void speakText(){
        speechHelper = new SpeechHelper(this);
        String textToSpeak = selectedYes ? "Waarom vindt je deze stelling erger?" : "Waarom vindt je de gele stelling erger?";
        speechHelper.speak(textToSpeak, new SpeechHelper.SpeechCompleteListener() {
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
