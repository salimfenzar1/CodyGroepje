package com.example.DAO;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.Model.Statement;
import com.example.codycactus.R;

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
                statement1.description = "Een cliënt kijkt regelmatig naar pornografische beelden";
                statement1.category = "Seksualiteit op de werkvloer";
                statement1.imageUrl = String.valueOf(R.drawable.wvie_pornografisch_2);
                statement1.intensityLevel = 1;
                statement1.isActive = true;
                statementDAO.insert(statement1);

                Statement statement2 = new Statement();
                statement2.description = "Tijdens de zorgverlening aan een van de cliënten wordt je ongepast aangeraakt.";
                statement2.category = "Seksualiteit op de werkvloer";
                statement2.imageUrl = String.valueOf(R.drawable.ongepaste_aanraking_client);
                statement2.intensityLevel = 2;
                statement2.isActive = true;
                statementDAO.insert(statement2);
//
                Statement statement3 = new Statement();
                statement3.description = "Je betrapt een bezoeker op het ongepaste gedrag van zelfbevrediging in de kamer van een van de bewoners.";
                statement3.category = "Seksualiteit op de werkvloer";
                statement3.imageUrl = String.valueOf(R.drawable.zelfbevredigend_client);
                statement3.intensityLevel = 3;
                statement3.isActive = true;
                statementDAO.insert(statement3);
//
                Statement statement4 = new Statement();
                statement4.description = "Je betrapt twee cliënten op het uitvoeren van seksuele handelingen.";
                statement4.category = "Seksualiteit op de werkvloer";
                statement4.imageUrl = String.valueOf(R.drawable.wvie_betrapt_client_op_seksuele_handelingen);
                statement4.intensityLevel = 2;
                statement4.isActive = true;
                statementDAO.insert(statement4);
//
                Statement statement5 = new Statement();
                statement5.description = "Een cliënt die aan ernstige dementie lijdt raakt zoek en wordt uiteindelijk gevonden op de kamer van een andere cliënt. Na onderzoek werd vastgesteld dat mevrouw slachtoffer is geweest en seksueel misbruikt.";
                statement5.category = "Seksualiteit op de werkvloer";
                statement5.imageUrl = String.valueOf(R.drawable.seksueel_misbruik_client_op_client);
                statement5.intensityLevel = 3;
                statement5.isActive = true;
                statementDAO.insert(statement5);

                Statement statement6 = new Statement();
                statement6.description = "Een zorgmedewerker betrapt twee collega's op intiem contact.";
                statement6.category = "Seksualiteit op de werkvloer";
                statement6.imageUrl = String.valueOf(R.drawable.wvie_betrapt_collegas_intiem_contact);
                statement6.intensityLevel = 2;
                statement6.isActive = true;
                statementDAO.insert(statement6);

                Statement statement7 = new Statement();
                statement7.description = "Tijdens een reflectiegesprek met de leidinggevende worden zeer persoonlijke vragen gesteld. Aan het einde van het gesprek stelt hij/zij de afspraak voor die gericht lijkt te zijn op seksueel contact.";
                statement7.category = "Seksualiteit op de werkvloer";
                statement7.imageUrl = String.valueOf(R.drawable.reflecitegesprek_leidinggevende);
                statement7.intensityLevel = 3;
                statement7.isActive = true;
                statementDAO.insert(statement7);
//
                Statement statement8 = new Statement();
                statement8.description = "Een cliënt maakt ongepaste opmerkingen over het uiterlijk van een zorgmedewerker.";
                statement8.category = "Seksualiteit op de werkvloer";
                statement8.imageUrl = String.valueOf(R.drawable.wvie_ongepaste_opmerking_over_uiterlijk);
                statement8.intensityLevel = 2;
                statement8.isActive = true;
                statementDAO.insert(statement8);

                Statement statement9 = new Statement();
                statement9.description = "Een cliënt maakt regelmatig ongepaste opmerkingen richting minderjarige/stagiaires/medewerkers.";
                statement9.category = "Seksualiteit op de werkvloer";
                statement9.imageUrl = String.valueOf(R.drawable.wvie_ongepaste_opmerking_2);
                statement9.intensityLevel = 2;
                statement9.isActive = true;
                statementDAO.insert(statement9);
//
                Statement statement10 = new Statement();
                statement10.description = "De afgelopen weken heb je herhaaldelijk geconstateerd dat een cliënt seksueel getinte foto's maakt, van zichzelf en jouw collega's.";
                statement10.category = "Seksualiteit op de werkvloer";
                statement10.imageUrl = String.valueOf(R.drawable.client_maakt_fotos);
                statement10.intensityLevel = 3;
                statement10.isActive = true;
                statementDAO.insert(statement10);
//
                // Overlijden
                Statement statement11 = new Statement();
                statement11.description = "Wanneer een cliënt mogelijk binnenkort komt te overlijden en hier vragen over heeft, vind ik het lastig om hier mee om te gaan.";
                statement11.category = "Overlijden";
                statement11.imageUrl = String.valueOf(R.drawable.client_komt_te_overlijden);
                statement11.intensityLevel = 3;
                statement11.isActive = true;
                statementDAO.insert(statement11);

                Statement statement12 = new Statement();
                statement12.description = "Na het overlijden van een cliënt die zelf regelmatig zorg weigerde voel ik me erg schuldig en machteloos.";
                statement12.category = "Overlijden";
                statement12.imageUrl = String.valueOf(R.drawable.client_weigerde_zorg);
                statement12.intensityLevel = 1;
                statement12.isActive = true;
                statementDAO.insert(statement12);
//
                Statement statement13 = new Statement();
                statement13.description = "Ik vind het lastig wanneer familie van een, reeds overleden, cliënt langs komt. Als ik hun verdriet zie weet ik niet wat ik tegen hen moet zeggen.";
                statement13.category = "Overlijden";
                statement13.imageUrl = String.valueOf(R.drawable.familie_overleden);
                statement13.intensityLevel = 3;
                statement13.isActive = true;
                statementDAO.insert(statement13);
//
                Statement statement14 = new Statement();
                statement14.description = "Na het overlijden van een cliënt heb ik wel eens te maken gehad met een stressreactie, zoals herbeleving, vermijdingsgedrag of verhoogde prikkelbaarheid.";
                statement14.category = "Overlijden";
                statement14.imageUrl = String.valueOf(R.drawable.geschrokken_overlijden_client);
                statement14.intensityLevel = 1;
                statement14.isActive = true;
                statementDAO.insert(statement14);
//
                Statement statement15 = new Statement();
                statement15.description = "Na het overlijden van een cliënt, waar ik een hechte band mee had voelt ik me wel eens eenzaam.";
                statement15.category = "Overlijden";
                statement15.imageUrl = String.valueOf(R.drawable.eenzaamheid_na_overlijden_client);
                statement15.intensityLevel = 2;
                statement15.isActive = true;
                statementDAO.insert(statement15);
//
                Statement statement16 = new Statement();
                statement16.description = "Het delen van ervaringen is volgens onderzoek essentieel voor een gezonde verwerking van gebeurtenissen. Ik kan mijn ervaringen goed delen met de mensen om me heen.";
                statement16.category = "Overlijden";
                statement16.imageUrl = String.valueOf(R.drawable.delen_van_ervaring);
                statement16.intensityLevel = 1;
                statement16.isActive = true;
                statementDAO.insert(statement16);
//
                Statement statement17 = new Statement();
                statement17.description = "Wanneer een cliënt een dierbare verliest vind ik het lastig om hen hiermee te begeleiden.";
                statement17.category = "Overlijden";
                statement17.imageUrl = String.valueOf(R.drawable.client_dierbare_begeleiden);
                statement17.intensityLevel = 3;
                statement17.isActive = true;
                statementDAO.insert(statement17);
//
                Statement statement18 = new Statement();
                statement18.description = "Wanneer ik met collega's praat over het overlijden van een cliënt merk ik dat zij de dood als een onvermijdelijk onderdeel van het leven zien en hier minder moeite mee hebben. Ik voel me daardoor soms niet begrepen of gehoord in mijn eigen gevoelens.";
                statement18.category = "Overlijden";
                statement18.imageUrl = String.valueOf(R.drawable.overlijden_client_niet_gehoord);
                statement18.intensityLevel = 2;
                statement18.isActive = true;
                statementDAO.insert(statement18);
//
                Statement statement19 = new Statement();
                statement19.description = "Ik ben wel eens geschrokken omdat er niet met mij was gecommuniceerd over het overlijden van een cliënt.";
                statement19.category = "Overlijden";
                statement19.imageUrl = String.valueOf(R.drawable.overlijden_client_niet_gehoord);
                statement19.intensityLevel = 2;
                statement19.isActive = true;
                statementDAO.insert(statement19);
//
//                // casussen werk-prive balans
                Statement statement20 = new Statement();
                statement20.description = "Emir heeft naast zijn baan in de zorg een groot gezin, daarmee is hij ook nog een mantelzorger voor zijn buurvrouw. Hij komt hierdoor nauwelijks aan zelfzorg toe en ervaart veel stress. Welke tips zou jij Emir geven en hoe zou jij je collega aanspreken wanneer je ziet dat hij/zij veel stress ervaart?";
                statement20.category = "Werk-prive balans";
                statement20.imageUrl = String.valueOf(R.drawable.emir);
                statement20.intensityLevel = 1;
                statement20.isActive = true;
                statementDAO.insert(statement20);

                Statement statement21 = new Statement();
                statement21.description = "Jacqueline is lerende in de zorg. Ze wordt vaak gevraagd bij te springen i.v.m. de personeelstekorten. Dit durft ze moeilijk te weigeren omdat ze weet dat ze haar nodig hebben. Ze vindt het lastig om haar school, sport en sociale leven hiernaar te onderhouden. Ze heeft veel stress en gaat hiervoor naar een psycholoog. Ervaar jij een hoge werkdruk? Zo ja, hoe zouden collega's hier elkaar in kunnen ondersteunen?";
                statement21.category = "Werk-prive balans";
                statement21.imageUrl = String.valueOf(R.drawable.jaqueline_personeelkorten);
                statement21.intensityLevel = 1;
                statement21.isActive = true;
                statementDAO.insert(statement21);

                Statement statement22 = new Statement();
                statement22.description = "Vera werkt sinds kort in de zorg. Zij heeft een goede band opgebouwd met één van de cliënten. Deze cliënt is onlangs komen te overlijden. Vera heeft hier enorm veel moeite mee en voelt zich op werk erg somber. De rest van haar collega's lijken hier minder moeite mee te hebben. Hoe zou jij omgaan met deze rouw/somberheid als je in de schoenen van Vera zou staan? Welke tips zou je haar geven?";
                statement22.category = "Werk-prive balans";
                statement22.imageUrl = String.valueOf(R.drawable.vera);
                statement22.intensityLevel = 3;
                statement1.isActive = true;
                statementDAO.insert(statement22);
//
                Statement statement23 = new Statement();
                statement23.description = "Ria werkt al 40 jaar in de zorg. Ze merkt dat ze tegenwoordig last krijgt van fysieke klachten na een werkweek, zoals nek- en rugpijn. Ze maakt hiervoor gebruik van fysiotherapie, echter blijft ze fysiek uitgeput. Ervaar jij wel eens fysieke klachten en zo ja, hoe ga je hiermee om?";
                statement23.category = "Werk-prive balans";
                statement23.imageUrl = String.valueOf(R.drawable.fysieke_klachten);
                statement23.intensityLevel = 1;
                statement23.isActive = true;
                statementDAO.insert(statement23);
//
                Statement statement24 = new Statement();
                statement24.description = "Fenna heeft regelmatig te maken met slaap problemen. Zij komt in de avond na haar avonddienst niet meteen in slaap. Wanneer zij de volgende ochtend weer moet werken stapelt haar vermoeidheid steeds verder op. Welke tips heb jij?";
                statement24.category = "Werk-prive balans";
                statement24.imageUrl = String.valueOf(R.drawable.slaap_problemen);
                statement24.intensityLevel = 2;
                statement24.isActive = true;
                statementDAO.insert(statement24);

                Statement statement25 = new Statement();
                statement25.description = "Isa heeft last van extreme menstrue klachten, ze is hier meerdere keren voor naar de huisarts geweest maar ze kunnen haar niet goed helpen. Op haar eerste twee menstruatie dagen kan ze wegens krampen en rugpijn haar werk niet goed uitvoeren. Collega's hebben meerdere keren opmerkingen gemaakt dat ze zich niet moet aanstellen. Hoe zou jou zorgorganisatie hier mee om moeten gaan?";
                statement25.category = "Werk-prive balans";
                statement25.imageUrl = String.valueOf(R.drawable.isa);
                statement25.intensityLevel = 3;
                statement25.isActive = true;
                statementDAO.insert(statement25);
//
                Statement statement26 = new Statement();
                statement26.description = "Fatma heeft na haar besmetting met Corona nog steeds veel klachten die niet weg lijken te gaan. Ze blijft vermoeid en heeft moeite om haar werk vol te houden. Ook blijft ze bang om opnieuw besmet te worden. Hoe zou jij als collega haar hierin ondersteunen?";
                statement26.category = "Werk-prive balans";
                statement26.imageUrl = String.valueOf(R.drawable.fatma_corona);
                statement26.intensityLevel = 3;
                statement26.isActive = true;
                statementDAO.insert(statement26);
//
                Statement statement27 = new Statement();
                statement27.description = "Tom krijgt op werk vaak last van hoofdpijn, die thuis leiden tot migraine aanvallen. Hij durft dit niet aan te geven omdat hij al veel dagen ziek thuis is gebleven. Wat zou jij doen in de situatie van Tom?";
                statement27.category = "Werk-prive balans";
                statement27.imageUrl = String.valueOf(R.drawable.tom_migraine);
                statement27.intensityLevel = 2;
                statement27.isActive = true;
                statementDAO.insert(statement27);

                // Mark the database as populated
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(PREF_POPULATED, true);
                editor.apply();
            });
        }
    }
}
