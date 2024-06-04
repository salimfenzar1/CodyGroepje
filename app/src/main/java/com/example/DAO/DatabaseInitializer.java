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

                // Seksualiteit op de werkvloer
                // Insert initial statements
                Statement statement1 = new Statement();
                statement1.description = "Een cliënt kijkt regelmatig naar pornografische beelden.";
                statement1.category = "Seksualiteit op de werkvloer";
                statement1.imageUrl = ""; // Geen afbeelding gegeven
                statement1.intensityLevel = 1;
                statementDAO.insert(statement1);

                Statement statement2 = new Statement();
                statement2.description = "Tijdens de zorgverlening aan een van de cliënten wordt je ongepast aangeraakt.";
                statement2.category = "Seksualiteit op de werkvloer";
                statement2.imageUrl = ""; // Geen afbeelding gegeven
                statement2.intensityLevel = 3;
                statementDAO.insert(statement2);

                Statement statement3 = new Statement();
                statement3.description = "Je betrapt een bezoeker op het ongepaste gedrag van zelfbevrediging in de kamer van een van de bewoners.";
                statement3.category = "Seksualiteit op de werkvloer";
                statement3.imageUrl = ""; // Geen afbeelding gegeven
                statement3.intensityLevel = 4;
                statementDAO.insert(statement3);

                Statement statement4 = new Statement();
                statement4.description = "Je betrapt twee cliënten op het uitvoeren van seksuele handelingen.";
                statement4.category = "Seksualiteit op de werkvloer";
                statement4.imageUrl = ""; // Geen afbeelding gegeven
                statement4.intensityLevel = 4;
                statementDAO.insert(statement4);

                Statement statement5 = new Statement();
                statement5.description = "Een cliënt die aan ernstige dementie lijdt raakt zoek en wordt uiteindelijk gevonden op de kamer van een andere cliënt. Na onderzoek werd vastgesteld dat mevrouw shendelder is geweest en seksueel misbuikt.";
                statement5.category = "Seksualiteit op de werkvloer";
                statement5.imageUrl = ""; // Geen afbeelding gegeven
                statement5.intensityLevel = 5;
                statementDAO.insert(statement5);

                Statement statement6 = new Statement();
                statement6.description = "Een zorgmedewerker betrapt twee collega's op intiem contact.";
                statement6.category = "Seksualiteit op de werkvloer";
                statement6.imageUrl = ""; // Geen afbeelding gegeven
                statement6.intensityLevel = 2;
                statementDAO.insert(statement6);

                Statement statement7 = new Statement();
                statement7.description = "Tijdens een reflectiegesprek met de leidinggevende worden zeer persoonlijke vragen gesteld. Aan het einde van het gesprek stelt hij/zij de afspraak voor die gericht lijkt te zijn op seksueel contact.";
                statement7.category = "Seksualiteit op de werkvloer";
                statement7.imageUrl = ""; // Geen afbeelding gegeven
                statement7.intensityLevel = 4;
                statementDAO.insert(statement7);

                Statement statement8 = new Statement();
                statement8.description = "Een cliënt maakt ongepaste opmerkingen over het uiterlijk van een zorgmedewerker.";
                statement8.category = "Seksualiteit op de werkvloer";
                statement8.imageUrl = ""; // Geen afbeelding gegeven
                statement8.intensityLevel = 2;
                statementDAO.insert(statement8);

                Statement statement9 = new Statement();
                statement9.description = "Een cliënt maakt regelmatig ongepaste opmerkingen richting minderjange/stagiaires/medewerkers.";
                statement9.category = "Seksualiteit op de werkvloer";
                statement9.imageUrl = ""; // Geen afbeelding gegeven
                statement9.intensityLevel = 3;
                statementDAO.insert(statement9);

                Statement statement10 = new Statement();
                statement10.description = "De afgelopen weken heb je herhaaldelijk geconstateerd dat een cliënt seksueel getinte foto's maalt, van dichzelf en jouw collega's.";
                statement10.category = "Seksualiteit op de werkvloer";
                statement10.imageUrl = ""; // Geen afbeelding gegeven
                statement10.intensityLevel = 4;
                statementDAO.insert(statement10);

                // Insert more statements as needed

                // Mark the database as populated
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_POPULATED, true);
                editor.apply();
            });
        }
    }
}
