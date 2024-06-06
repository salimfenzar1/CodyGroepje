package com.example.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.Model.Statement;

import java.util.List;

@Dao
public interface StatementDAO {

    @Insert
    void insert(Statement statement);

    @Query("SELECT * FROM statement")
    LiveData<List<Statement>> getAllStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel = 2")
    LiveData<List<Statement>> getMatigStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel = 1")
    LiveData<List<Statement>> getLaagdrempeligStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel =3")
    LiveData<List<Statement>> getIntensStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (1, 2)")
    LiveData<List<Statement>> getMatigAndLaagdrempeligStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (2, 3)")
    LiveData<List<Statement>> getMatigAndIntensStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (1, 3)")
    LiveData<List<Statement>> getLaagdrempeligAndIntensStatements();



}
