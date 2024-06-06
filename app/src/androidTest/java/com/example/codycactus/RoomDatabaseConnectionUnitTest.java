package com.example.codycactus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;
import com.example.Model.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class RoomDatabaseConnectionUnitTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private StatementRoom database;
    private StatementDAO statementDAO;

    @Before
    public void setUp() {
        // Get the application context
        Context context = ApplicationProvider.getApplicationContext();
        // Create an in-memory version of the database
        database = Room.inMemoryDatabaseBuilder(context, StatementRoom.class)
                .allowMainThreadQueries()
                .build();

        // Get the DAO
        statementDAO = database.statementDAO();
    }
    @After
    public void tearDown() throws IOException {
        // Close the database
        database.close();
    }

    @Test
    public void testDatabaseConnection() {
        // Check that the database instance is not null
        assertNotNull(database);

        // Check that the DAO instance is not null
        assertNotNull(statementDAO);
    }
    @Test
    public void testReadAndWrite() throws Exception {
        Statement statement = new Statement();
        statement.description = "een cliënt eet een snicker bar voor een hongerige medewerker om iets uit te zaaien";
        statement.category = "Seksualiteit op de werkvloer";
        statement.imageUrl = String.valueOf(R.drawable.wvie_betrapt_client_op_seksuele_handelingen); // Geen afbeelding gegeven
        statement.intensityLevel = 1;
        statement.isActive = true;
        statementDAO.insert(statement);

        LiveData<List<Statement>> statementsLiveData = statementDAO.getAllStatements();
        List<Statement> statements = LiveDataTestUtil.getOrAwaitValue(statementsLiveData);
        assertEquals(statements.get(0).description, "een cliënt eet een snicker bar voor een hongerige medewerker om iets uit te zaaien");
        }

    @Test
    public void testReadPerformance() throws Exception{
        long startTime = System.currentTimeMillis();

        // Perform read operation
        LiveData<List<Statement>> statementsLiveData = statementDAO.getAllStatements();
        List<Statement> statements = LiveDataTestUtil.getOrAwaitValue(statementsLiveData);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Assert that the read operation completes within an acceptable time
        assertNotNull(statements);
        assertTrue("Read operation took too long: " + elapsedTime + " milliseconds", elapsedTime < 1000);
    }

    @Test
    public void testGetLaagdrempeligStatements() throws Exception {
        // Insert statements
        Statement statement1 = new Statement();
        statement1.description = "Description 1";
        statement1.category = "Laagdrempelig";
        statement1.imageUrl = "image_url_1";
        statement1.intensityLevel = 1;
        statement1.isActive = true;

        Statement statement2 = new Statement();
        statement2.description = "Description 2";
        statement2.category = "Other";
        statement2.imageUrl = "image_url_21";
        statement2.intensityLevel = 2;
        statement2.isActive = true;

        statementDAO.insert(statement1);
        statementDAO.insert(statement2);

        // Fetch laagdrempelig statements
        LiveData<List<Statement>> laagdrempeligStatementsLiveData = statementDAO.getLaagdrempeligStatements();
        List<Statement> laagdrempeligStatements = LiveDataTestUtil.getOrAwaitValue(laagdrempeligStatementsLiveData);

        // Assert results
        assertNotNull(laagdrempeligStatements);
        assertEquals(1, laagdrempeligStatements.size());
        assertEquals("Description 1", laagdrempeligStatements.get(0).description);
    }

    @Test
    public void testGetIntensStatements() throws Exception {
        // Insert statements
        Statement statement1 = new Statement();
        statement1.description = "Intense Description 11";
        statement1.category = "Category 1";
        statement1.imageUrl = "image_url_1";
        statement1.intensityLevel = 3; // Assuming 3 is considered intense
        statement1.isActive = true;

        Statement statement2 = new Statement();
        statement2.description = "Mild Description";
        statement2.category = "Category 2";
        statement2.imageUrl = "image_url_2";
        statement2.intensityLevel = 1; // Assuming 1 is not considered intense
        statement2.isActive = true;

        statementDAO.insert(statement1);
        statementDAO.insert(statement2);

        // Fetch intense statements
        LiveData<List<Statement>> intensStatementsLiveData = statementDAO.getIntensStatements();
        List<Statement> intensStatements = LiveDataTestUtil.getOrAwaitValue(intensStatementsLiveData);

        // Assert results
        assertNotNull(intensStatements);
        assertEquals(1, intensStatements.size());
        assertEquals("Intense Description 1", intensStatements.get(0).description);
    }

}