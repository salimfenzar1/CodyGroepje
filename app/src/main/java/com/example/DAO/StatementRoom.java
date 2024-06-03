package com.example.DAO;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.Model.Statement;

@Database(entities = {Statement.class}, version = 1)
public abstract class StatementRoom extends RoomDatabase{

    private static StatementRoom instance;

    public abstract StatementDAO statementDAO();

    public static synchronized StatementRoom getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            StatementRoom.class, "room_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }



}
