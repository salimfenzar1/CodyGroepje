package com.example.DAO;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.Model.Statement;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StatementRepository {
    private StatementDAO statementDAO;
    private LiveData<List<Statement>> allStatements;
    private LiveData<List<Statement>> matigStatements;
    private LiveData<List<Statement>> laagdrempeligStatements;
    private LiveData<List<Statement>> intensStatements;
    private LiveData<List<Statement>> matigAndLaagdrempeligStatements;
    private LiveData<List<Statement>> matigAndIntensStatements;
    private LiveData<List<Statement>> laagdrempeligAndIntensStatements;

    public StatementRepository(Application application) {
        StatementRoom database = StatementRoom.getInstance(application);
        statementDAO = database.statementDAO();
        allStatements = statementDAO.getAllStatements();
        matigStatements = statementDAO.getMatigStatements();
        laagdrempeligStatements = statementDAO.getLaagdrempeligStatements();
        intensStatements = statementDAO.getIntensStatements();
        matigAndLaagdrempeligStatements = statementDAO.getMatigAndLaagdrempeligStatements();
        matigAndIntensStatements = statementDAO.getMatigAndIntensStatements();
        laagdrempeligAndIntensStatements = statementDAO.getLaagdrempeligAndIntensStatements();
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

    public Statement getStatementByDescription(String description) {
        try {
            return new GetStatementByDescriptionAsyncTask(statementDAO).execute(description).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(Statement statement) {
        new InsertStatementAsyncTask(statementDAO).execute(statement);
    }

    public void updateStatementStatus(int id, boolean isActive) {
        new UpdateStatementStatusAsyncTask(statementDAO, isActive).execute(id);
    }

    public void updateAllStatementsStatus(boolean isActive) {
        new UpdateAllStatementsStatusAsyncTask(statementDAO, isActive).execute();
    }

    private static class GetStatementByDescriptionAsyncTask extends AsyncTask<String, Void, Statement> {
        private StatementDAO statementDAO;

        private GetStatementByDescriptionAsyncTask(StatementDAO statementDAO) {
            this.statementDAO = statementDAO;
        }

        @Override
        protected Statement doInBackground(String... descriptions) {
            return statementDAO.getStatementByDescription(descriptions[0]);
        }
    }

    private static class InsertStatementAsyncTask extends AsyncTask<Statement, Void, Void> {
        private StatementDAO statementDAO;

        private InsertStatementAsyncTask(StatementDAO statementDAO) {
            this.statementDAO = statementDAO;
        }

        @Override
        protected Void doInBackground(Statement... statements) {
            statementDAO.insert(statements[0]);
            return null;
        }
    }

    private static class UpdateStatementStatusAsyncTask extends AsyncTask<Integer, Void, Void> {
        private StatementDAO statementDAO;
        private boolean isActive;

        private UpdateStatementStatusAsyncTask(StatementDAO statementDAO, boolean isActive) {
            this.statementDAO = statementDAO;
            this.isActive = isActive;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            statementDAO.updateStatementStatus(ids[0], isActive);
            return null;
        }
    }

    private static class UpdateAllStatementsStatusAsyncTask extends AsyncTask<Void, Void, Void> {
        private StatementDAO statementDAO;
        private boolean isActive;

        private UpdateAllStatementsStatusAsyncTask(StatementDAO statementDAO, boolean isActive) {
            this.statementDAO = statementDAO;
            this.isActive = isActive;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            statementDAO.updateAllStatementsStatus(isActive);
            return null;
        }
    }
}
