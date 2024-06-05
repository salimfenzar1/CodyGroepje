package com.example.codycactus.DatabaseUnitTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;

import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;
import com.example.Model.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class RoomDatabaseConnectionUnitTest {

    private StatementRoom database;
    private StatementDAO statementDAO;

    @Before
    public void setUp() {
        // Get the application context
        Context context = RuntimeEnvironment.getApplication();

        // Create an in-memory version of the database
        database = Room.inMemoryDatabaseBuilder(context, StatementRoom.class)
                .allowMainThreadQueries()
                .build();

        // Get the DAO
        statementDAO = database.statementDAO();
    }

    @After
    public void tearDown() {
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
    public void testReadPerformance() {
        long startTime = System.currentTimeMillis();

        // Perform read operation
        List<Statement> statements = statementDAO.getAllStatements().getValue();

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Assert that the read operation completes within an acceptable time
        assertTrue("Read operation took too long: " + elapsedTime + " milliseconds", elapsedTime < 1000);
    }
}