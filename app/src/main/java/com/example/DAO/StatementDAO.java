package com.example.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.Model.Statement;

import java.util.List;

@Dao
public interface StatementDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Statement statement);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Statement> statements);

    @Query("SELECT * FROM statement")
    LiveData<List<Statement>> getAllStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel = 2")
    LiveData<List<Statement>> getMatigStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel = 1")
    LiveData<List<Statement>> getLaagdrempeligStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel = 3")
    LiveData<List<Statement>> getIntensStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (1, 2)")
    LiveData<List<Statement>> getMatigAndLaagdrempeligStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (2, 3)")
    LiveData<List<Statement>> getMatigAndIntensStatements();

    @Query("SELECT * FROM statement WHERE intensityLevel IN (1, 3)")
    LiveData<List<Statement>> getLaagdrempeligAndIntensStatements();

    @Query("SELECT * FROM statement WHERE isActive = 1")
    LiveData<List<Statement>> getActiveStatements();

    @Query("SELECT * FROM statement WHERE description = :description LIMIT 1")
    LiveData<Statement> getStatementByDescription(String description);

    @Query("SELECT * FROM statement WHERE description = :description LIMIT 1")
    Statement getStatementByDescriptionSync(String description);

    @Query("DELETE FROM statement")
    void deleteAllStatements();

    @Query("UPDATE statement SET isActive = :isActive WHERE statementId = :id")
    void updateStatementStatus(int id, boolean isActive);

    @Query("UPDATE statement SET isActive = :isActive")
    void updateAllStatementsStatus(boolean isActive);
}
