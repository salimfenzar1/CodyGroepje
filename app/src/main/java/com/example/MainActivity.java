package com.example;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.DAO.DatabaseInitializer;
import com.example.DAO.StatementDAO;
import com.example.DAO.StatementViewModel;
import com.example.Model.Statement;
import com.example.codycactus.R;
import com.example.levendOrganogram.LoSubjectsActivity;
import com.example.services.SpeechHelper;
import com.example.services.SpeechRecognitionManager;
import com.example.deTijdTikt.DttIntensityActivity;
import com.example.watVindIkErger.WvieSubjectsActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SpeechRecognitionManager.SpeechRecognitionListener {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private ImageButton tijdTikt;
    private ImageButton levend;
    private ImageButton watVind;
    private SpeechHelper speechHelper;
    private SpeechRecognitionManager speechRecognitionManager;
    private StatementViewModel statementViewModel;
    private StatementDAO statementDAO;
    private List<Statement> allStatements = new ArrayList<>(); // Initialize the list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearCache();
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

        setButtonsClickable(false);

        // Initialize ViewModel
        statementViewModel = new ViewModelProvider(this).get(StatementViewModel.class);

        statementViewModel.getAllStatements().observe(this, new Observer<List<Statement>>() {
            @Override
            public void onChanged(List<Statement> statements) {
                if (statements != null) {
                    allStatements = new ArrayList<>(statements);
                    Log.d("MainActivity", "Number of statements in local database: " + allStatements.size());

                    // Remove observer after getting the initial data
                    statementViewModel.getAllStatements().removeObserver(this);
                } else {
                    Log.d("MainActivity", "Statements list is null");
                }
            }
        });

        DatabaseInitializer.populateDatabase(MainActivity.this);
        new PopulateDatabaseTask().execute();
        Log.d("Db", "Statements teogevoegd: " + allStatements.size());

        tijdTikt.setOnClickListener(v -> navigateToActivity(DttIntensityActivity.class));
        levend.setOnClickListener(v -> navigateToActivity(LoSubjectsActivity.class));
        watVind.setOnClickListener(v -> navigateToActivity(WvieSubjectsActivity.class));

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        speakIntro();
    }

    private void navigateToActivity(Class<?> activityClass) {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.stopListening();
            speechRecognitionManager.destroy();
        }
        Intent intent = new Intent(MainActivity.this, activityClass);
        intent.putParcelableArrayListExtra("statements", new ArrayList<>(allStatements));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            speechRecognitionManager = new SpeechRecognitionManager(this, this);
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
                if (speechRecognitionManager != null) {
                    speechRecognitionManager.startListening();
                }
            }

            @Override
            public void onSpeechFailed() {
                Log.e("Speech", "Speech synthesis mislukt");
                setButtonsClickable(true);  // Zet de knoppen klikbaar zelfs als de spraaksynthese mislukt
                speakIntro();
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
        result = result.trim().toLowerCase();
        if (result.contains("vind") && result.contains("erger")) {
            navigateToActivity(WvieSubjectsActivity.class);
        } else if (result.contains("tijd") && result.contains("tikt")) {
            navigateToActivity(DttIntensityActivity.class);
        } else if (result.contains("levend") || result.contains("gram")) {
            navigateToActivity(LoSubjectsActivity.class);
        } else {
            speechHelper = new SpeechHelper(this);
            speechHelper.speak("Sorry dat verstond ik niet, zou je dat kunnen herhalen?", new SpeechHelper.SpeechCompleteListener() {
                @Override
                public void onSpeechComplete() {
                    Log.d("Speech", "Speech synthesis voltooid");
                    setButtonsClickable(true);  // Zet de knoppen klikbaar
                    if (speechRecognitionManager != null) {
                        speechRecognitionManager.startListening();
                    }
                    Log.d("speakreplay",    "begint met luisteren");
                }

                @Override
                public void onSpeechFailed() {
                    Log.e("Speech", "Speech synthesis mislukt");
                    setButtonsClickable(true);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognitionManager != null) {
            speechRecognitionManager.destroy();
        }
        super.onDestroy();
    }

    private void clearCache() {
        try {
            File cacheDir = getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                Log.d("MainActivity", "Cache successfully removed");
                deleteDir(cacheDir);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error clearing cache: " + e.getMessage());
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private class PopulateDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Populate with initial local data

            // Fetch and add Firebase statements
            fetchStatementsFromFirebase();


            return null;
        }
    }

    private void fetchStatementsFromFirebase() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("statements").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<Statement> firebaseStatements = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Statement statement = document.toObject(Statement.class);
                        if (statement != null) {
                            statementViewModel.getStatementByDescription(statement.description).observe(this, existingStatement -> {
                                if (existingStatement == null) {
                                    firebaseStatements.add(statement);
                                    Log.d("Db", "Fetched Firebase statement: " + statement.description);
                                    statementViewModel.insert(statement);
                                }
                            });
                        }
                    }

                    // Log the number of statements fetched from Firebase
                    Log.d("MainActivity", "Number of Firebase statements fetched: " + firebaseStatements.size() + " " + allStatements.size());
                }
            } else {
                Log.e("MainActivity", "Error getting documents: ", task.getException());
            }
        });
    }
}
