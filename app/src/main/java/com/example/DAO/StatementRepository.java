package com.example.DAO;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.DAO.StatementDAO;
import com.example.DAO.StatementRoom;
import com.example.Model.Statement;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatementRepository {
    private StatementDAO statementDAO;
    private LiveData<List<Statement>> allStatements;
    private ExecutorService executorService;

    public StatementRepository(Application application) {
        StatementRoom database = StatementRoom.getInstance(application);
        statementDAO = database.statementDAO();
        allStatements = statementDAO.getAllStatements();
        executorService = Executors.newFixedThreadPool(2); // You can adjust the number of threads as needed
    }

    public LiveData<List<Statement>> getAllStatements() {
        return allStatements;
    }

    public LiveData<List<Statement>> getMatigStatements() {
        return statementDAO.getMatigStatements();
    }

    public LiveData<List<Statement>> getLaagdrempeligStatements() {
        return statementDAO.getLaagdrempeligStatements();
    }

    public LiveData<List<Statement>> getIntensStatements() {
        return statementDAO.getIntensStatements();
    }

    public LiveData<List<Statement>> getMatigAndLaagdrempeligStatements() {
        return statementDAO.getMatigAndLaagdrempeligStatements();
    }

    public LiveData<List<Statement>> getMatigAndIntensStatements() {
        return statementDAO.getMatigAndIntensStatements();
    }

    public LiveData<List<Statement>> getLaagdrempeligAndIntensStatements() {
        return statementDAO.getLaagdrempeligAndIntensStatements();
    }

    public void insert(final Statement statement) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                statementDAO.insert(statement);
            }
        });
    }
}
