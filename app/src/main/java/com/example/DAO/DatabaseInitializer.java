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
                statement2.intensityLevel = 2;
                statementDAO.insert(statement2);

                Statement statement3 = new Statement();
                statement3.description = "Je betrapt een bezoeker op het ongepaste gedrag van zelfbevrediging in de kamer van een van de bewoners.";
                statement3.category = "Seksualiteit op de werkvloer";
                statement3.imageUrl = ""; // Geen afbeelding gegeven
                statement3.intensityLevel = 3;
                statementDAO.insert(statement3);

                Statement statement4 = new Statement();
                statement4.description = "Je betrapt twee cliënten op het uitvoeren van seksuele handelingen.";
                statement4.category = "Seksualiteit op de werkvloer";
                statement4.imageUrl = ""; // Geen afbeelding gegeven
                statement4.intensityLevel = 2;
                statementDAO.insert(statement4);

                Statement statement5 = new Statement();
                statement5.description = "Een cliënt die aan ernstige dementie lijdt raakt zoek en wordt uiteindelijk gevonden op de kamer van een andere cliënt. Na onderzoek werd vastgesteld dat mevrouw shendelder is geweest en seksueel misbuikt.";
                statement5.category = "Seksualiteit op de werkvloer";
                statement5.imageUrl = ""; // Geen afbeelding gegeven
                statement5.intensityLevel = 3;
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
                statement7.intensityLevel = 3;
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
                statement9.intensityLevel = 2;
                statementDAO.insert(statement9);

                Statement statement10 = new Statement();
                statement10.description = "De afgelopen weken heb je herhaaldelijk geconstateerd dat een cliënt seksueel getinte foto's maalt, van dichzelf en jouw collega's.";
                statement10.category = "Seksualiteit op de werkvloer";
                statement10.imageUrl = ""; // Geen afbeelding gegeven
                statement10.intensityLevel = 3;
                statementDAO.insert(statement10);

                // Overlijden
                Statement statement11 = new Statement();
                statement1.description = "Wanneer een cliënt mogelijk bijnnkomst krijnt te overlijden en hier vragen over heeft, vind ik het lastig om hier mee om te gaan.";
                statement1.category = "Overlijden";
                statement1.imageUrl = ""; // Geen afbeelding gegeven
                statement1.intensityLevel = 3;
                statementDAO.insert(statement11);

                Statement statement12 = new Statement();
                statement2.description = "Na het overlijden van een cliënt die zelf regelmatig zorg weigerde voel ik me erg schuldig en machteloos.";
                statement2.category = "Overlijden";
                statement2.imageUrl = ""; // Geen afbeelding gegeven
                statement2.intensityLevel = 1;
                statementDAO.insert(statement12);

                Statement statement13 = new Statement();
                statement3.description = "Ik vind het lastig wanneer familie van een, reeds overleden, cliënt langs komt. Als ik hun verhaal zie weet ik niet wat ik tegen hen moet zeggen.";
                statement3.category = "Overlijden";
                statement3.imageUrl = ""; // Geen afbeelding gegeven
                statement3.intensityLevel = 3;
                statementDAO.insert(statement13);

                Statement statement14 = new Statement();
                statement4.description = "Na het overlijden van een cliënt heb ik wel eens te maken gehad met een stress-reactie, zoals: tinteling, verre/pretlichtjes of vermoeidheid in het lichaam.";
                statement4.category = "Overlijden";
                statement4.imageUrl = ""; // Geen afbeelding gegeven
                statement4.intensityLevel = 1;
                statementDAO.insert(statement14);

                Statement statement15 = new Statement();
                statement5.description = "Na het overlijden van een cliënt, waar ik een hechte band mee had voelt ik me wel eens eenzaam.";
                statement5.category = "Overlijden";
                statement5.imageUrl = ""; // Geen afbeelding gegeven
                statement5.intensityLevel = 2;
                statementDAO.insert(statement15);

                Statement statement16 = new Statement();
                statement6.description = "Het delen van ervaringen is volgens onderzoek essentieel voor een gezonde verwerking van gebeurtenissen. Ik kan mijn ervaringen goed delen met de mensen om me heen.";
                statement6.category = "Overlijden";
                statement6.imageUrl = ""; // Geen afbeelding gegeven
                statement6.intensityLevel = 1;
                statementDAO.insert(statement16);

                Statement statement17 = new Statement();
                statement7.description = "Wanneer een cliënt een dierbare verliest vind ik het lastig om hen hiermee te begroederen.";
                statement7.category = "Overlijden";
                statement7.imageUrl = ""; // Geen afbeelding gegeven
                statement7.intensityLevel = 3;
                statementDAO.insert(statement17);

                Statement statement18 = new Statement();
                statement8.description = "Wanneer ik met collega's praat over het overlijden van een cliënt merk ik dat zij de dood als een onvermijdelijk onderdeel van het leven zien en hier minder moeite mee hebben. Ik voel me daardoor soms wat begrensd of beloodt in mijn eigen gevoelens.";
                statement8.category = "Overlijden";
                statement8.imageUrl = ""; // Geen afbeelding gegeven
                statement8.intensityLevel = 2;
                statementDAO.insert(statement18);

                Statement statement19 = new Statement();
                statement9.description = "Ik ben wel eens geschrokken omdat er niet met mij was gecommuniceerd over het overlijden van een cliënt.";
                statement9.category = "Overlijden";
                statement9.imageUrl = ""; // Geen afbeelding gegeven
                statement9.intensityLevel = 2;
                statementDAO.insert(statement19);

                // casussen werk-prive balans
                Statement statement20 = new Statement();
                statement1.description = "Fatima heeft relatief aan baan in de zorg een grote lezin, daarmee Is hij ook nog een mantelzorger voor zijn broer ovelijk. Het kost hem veel geven om haar werk als competente werkster je ziet dat hij/zij aftezit. Welke tip zou hij/zij geven om haar baan te kunnen blijven?";
                statement1.category = "Werk-prive balans";
                statement1.imageUrl = ""; // Geen afbeelding gegeven
                statement1.intensityLevel = 1;
                statementDAO.insert(statement20);

                Statement statement21 = new Statement();
                statement2.description = "Jacqueline is lerende in de zorg. Ze wordt vaak geprut bij te springen t.v.m. de personeles tekort. De Kracht op mensen in te moeten springen t.v.m. een personeelstekort. Ze vindt het lastig om haar school, sport en sociale leven hiernaar te onderhounden. Ze heeft veel uren en moet honden naar aan gevoelens maar aan gezondheid? Zo ja, hoe zouden collega's hier elkaar is kunnen ondersteunen?";
                statement2.category = "Werk-prive balans";
                statement2.imageUrl = ""; // Geen afbeelding gegeven
                statement2.intensityLevel = 1;
                statementDAO.insert(statement21);

                Statement statement22 = new Statement();
                statement3.description = "Vera werkt sinds kort in de zorg. Zij heeft een goedbetaalde opbouwmet tén van de cliënten. Door dit erg veranstalt komen te rouwen te raken heeft zij gemortiseerde wensen te werken. Op werk org andere: ben (zij) van haar collega's, zeker die met kinderen, worden steeds vaker gewaarschuwd om op te passen. Jeheeft dit gevoel dat ze vaak kan leiden tot uitputting. Welke tips heb jij?";
                statement3.category = "Werk-prive balans";
                statement3.imageUrl = ""; // Geen afbeelding gegeven
                statement3.intensityLevel = 3;
                statementDAO.insert(statement22);

                Statement statement23 = new Statement();
                statement4.description = "Ik werk al 40 jaar e de zorg. Ze moet dat ze tegenwoordig last krijgt van fysieke klachten na een werkweek, zoals nek- en rugjen. Ze maakt hiervoor gebruik van fysiotherapie, echter blijft ze toch langere. Ervaart jij wel eens fysieke klachten en zo ja, hoe ga je hiermee om?";
                statement4.category = "Werk-prive balans";
                statement4.imageUrl = ""; // Geen afbeelding gegeven
                statement4.intensityLevel = 1;
                statementDAO.insert(statement23);

                Statement statement24 = new Statement();
                statement5.description = "Fatma heeft regelmatig te maken met slaap problemen: zij komt in de ochtend na haar aanvredient niet meteen in slaap. Wanneer zij de volgende ochtend weer moet werken slaaptf zij vermoeindheid steeds vender op. Welke tips heb je?";
                statement5.category = "Werk-prive balans";
                statement5.imageUrl = ""; // Geen afbeelding gegeven
                statement5.intensityLevel = 2;
                statementDAO.insert(statement24);

                Statement statement25 = new Statement();
                statement6.description = "Ik heb het last van extreme menstrue klachten, ze is hier meerdere keren voor naar de huisars gegaan meer ze krijgt geen oplossing. Ik kan wel op dagen met deze klaclachten mijn werk niet goed uitvoeren. Collega's hebben moeities zagen mevrouw alsmantlid dat ze zich niet goed aanspelen. Hoe kan zorgorganisatie har mee om moeten gaan?";
                statement6.category = "Werk-prive balans";
                statement6.imageUrl = ""; // Geen afbeelding gegeven
                statement6.intensityLevel = 3;
                statementDAO.insert(statement25);

                Statement statement26 = new Statement();
                statement7.description = "Fatma heeft na haar besmetting met Corona nog sleeds veel klachten die niet weg lijken te gaan zonder tot voor keert mogelijk om haar werk vol te houden. Ook is het op te ouwet te worden. Hoe zou jij di collega's haar hier in ondersteunen?";
                statement7.category = "Werk-prive balans";
                statement7.imageUrl = ""; // Geen afbeelding gegeven
                statement7.intensityLevel = 3;
                statementDAO.insert(statement26);

                Statement statement27 = new Statement();
                statement8.description = "Ton voigt op werk vaak last van hoofdpijn, die truis echt en de migreine aanvallen Hij dufft aft paken maar door de werkdruk zeker niet langer lopen dan zéker. Welke tips heb jij voor Tom?";
                statement8.category = "Werk-prive balans";
                statement8.imageUrl = ""; // Geen afbeelding gegeven
                statement8.intensityLevel = 2;
                statementDAO.insert(statement27);

                // Mark the database as populated
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_POPULATED, true);
                editor.apply();
            });
        }
    }
}
