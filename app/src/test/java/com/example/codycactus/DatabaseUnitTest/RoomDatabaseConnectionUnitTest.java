package com.example.codycactus.DatabaseUnitTest;

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;

import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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
}