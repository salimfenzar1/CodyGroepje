package com.example.DAO;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.Model.Statement;

import java.util.List;

public class StatementViewModel extends AndroidViewModel {
    private StatementRepository repository;
    private LiveData<List<Statement>> allStatements;

    public StatementViewModel(Application application) {
        super(application);
        repository = new StatementRepository(application);
        allStatements = repository.getAllStatements();
    }

    public LiveData<List<Statement>> getAllStatements() {
        return allStatements;
    }

    public void insert(Statement statement) {
        repository.insert(statement);
    }

    public void insertAll(List<Statement> statements) {
        repository.insertAll(statements);
    }

    public void deleteAllStatements() {
        repository.deleteAllStatements();
    }

    public void updateStatementStatus(int id, boolean isActive) {
        repository.updateStatementStatus(id, isActive);
    }

    public void updateAllStatementsStatus(boolean isActive) {
        repository.updateAllStatementsStatus(isActive);
    }

    public LiveData<Statement> getStatementByDescription(String description) {
        return repository.getStatementByDescription(description);
    }
}
