package com.example.DAO;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.Model.Statement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    private static final String PREFS_NAME = "DatabaseInitializer";
    private static final String PREF_POPULATED = "populated";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void populateDatabase(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isPopulated = preferences.getBoolean(PREF_POPULATED, false);

        if (!isPopulated) {
            executor.execute(() -> {
                StatementRoom db = StatementRoom.getInstance(context);
                StatementDAO statementDAO = db.statementDAO();

                // Insert initial statements
                Statement statement1 = new Statement();
                statement1.description = "This is the first statement";
                statement1.category = "Seksualiteit";
                statement1.imageUrl = "https://example.com/image1.jpg";
                statement1.intensityLevel = 1;
                statementDAO.insert(statement1);

                Statement statement2 = new Statement();
                statement2.description = "This is the second statement";
                statement2.category = "Category B";
                statement2.imageUrl = "https://example.com/image2.jpg";
                statement2.intensityLevel = 3;
                statementDAO.insert(statement2);

                Statement statement3 = new Statement();
                statement2.description = "This is the third statement";
                statement2.category = "Category B";
                statement2.imageUrl = "https://example.com/image2.jpg";
                statement2.intensityLevel = 3;
                statementDAO.insert(statement2);

                // Insert more statements as needed

                // Mark the database as populated
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_POPULATED, true);
                editor.apply();
            });
        }
    }
}
