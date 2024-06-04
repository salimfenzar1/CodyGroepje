package com.example;

import static com.example.codycactus.R.*;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.DAO.DatabaseInitializer;
import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;
import com.example.Model.Statement;
import com.example.codycactus.R;

import java.util.List;
import java.util.Random;

public class TestActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.testactivity);
        textView = findViewById(R.id.textviewview);

        DatabaseInitializer.populateDatabase(this);

        // Get an instance of the StatementDAO
        StatementRoom db = StatementRoom.getInstance(this);
        StatementDAO statementDAO = db.statementDAO();

        // Observe the LiveData from the getAllStatements method

        statementDAO.getAllStatements().observe(this, new Observer<List<Statement>>() {
            @Override
            public void onChanged(List<Statement> statements) {
                if (statements != null && !statements.isEmpty()) {
                    // Choose a random statement
                    Random random = new Random();
                    int randomIndex = random.nextInt(statements.size());
                    Statement randomStatement = statements.get(randomIndex);

                    // Update the TextView with the description of the random Statement object
                    textView.setText(randomStatement.description);
                }
            }
        });
    }
}
