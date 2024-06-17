package com.example.DAO;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.Model.Statement;

import java.util.List;

public class StatementViewModel extends AndroidViewModel {
    private StatementRepository repository;
    private LiveData<List<Statement>> allStatements;
    private LiveData<List<Statement>> matigStatements;
    private LiveData<List<Statement>> laagdrempeligStatements;
    private LiveData<List<Statement>> intensStatements;
    private LiveData<List<Statement>> matigAndLaagdrempeligStatements;
    private LiveData<List<Statement>> matigAndIntensStatements;
    private LiveData<List<Statement>> laagdrempeligAndIntensStatements;

    public StatementViewModel(Application application) {
        super(application);
        repository = new StatementRepository(application);
        allStatements = repository.getAllStatements();
        matigStatements = repository.getMatigStatements();
        laagdrempeligStatements = repository.getLaagdrempeligStatements();
        intensStatements = repository.getIntensStatements();
        matigAndLaagdrempeligStatements = repository.getMatigAndLaagdrempeligStatements();
        matigAndIntensStatements = repository.getMatigAndIntensStatements();
        laagdrempeligAndIntensStatements = repository.getLaagdrempeligAndIntensStatements();
    }

    public LiveData<List<Statement>> getAllStatements() {
        return allStatements;
    }

    public LiveData<List<Statement>> getMatigStatements() {
        return matigStatements;
    }

    public LiveData<List<Statement>> getLaagdrempeligStatements() {
        return laagdrempeligStatements;
    }

    public LiveData<List<Statement>> getIntensStatements() {
        return intensStatements;
    }

    public LiveData<List<Statement>> getMatigAndLaagdrempeligStatements() {
        return matigAndLaagdrempeligStatements;
    }

    public LiveData<List<Statement>> getMatigAndIntensStatements() {
        return matigAndIntensStatements;
    }

    public LiveData<List<Statement>> getLaagdrempeligAndIntensStatements() {
        return laagdrempeligAndIntensStatements;
    }

    public void insert(Statement statement) {
        repository.insert(statement);
    }

    public void updateStatementStatus(int id, boolean isActive) {
        repository.updateStatementStatus(id, isActive);
    }

    public Statement getStatementByDescription(String description) {
        return repository.getStatementByDescription(description);
    }

    public void updateAllStatementsStatus(boolean isActive) {
        repository.updateAllStatementsStatus(isActive);
    }
}
