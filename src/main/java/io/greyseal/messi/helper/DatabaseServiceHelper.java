package io.greyseal.messi.helper;


import io.greyseal.messi.service.DatabaseService;

public enum DatabaseServiceHelper {
    INSTANCE;

    private DatabaseService dbService;

    public DatabaseService getDbService() {
        return dbService;
    }

    public void setDbService(final DatabaseService dbService) {
        this.dbService = dbService;
    }
}