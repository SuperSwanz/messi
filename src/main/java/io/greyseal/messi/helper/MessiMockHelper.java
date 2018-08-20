package io.greyseal.messi.helper;

import io.greyseal.messi.model.Mock;
import io.greyseal.messi.service.DatabaseService;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public enum MessiMockHelper {
    INSTANCE;
    private static final DatabaseService dbService = DatabaseServiceHelper.INSTANCE.getDbService();
    private static final io.greyseal.messi.service.reactivex.DatabaseService rxDBService = new io.greyseal.messi.service.reactivex.DatabaseService(dbService);

    public Single<JsonObject> doCreateMock(final Mock mock) {
        mock.setCreatedBy("saurabh");
        mock.setUpdatedBy("saurabh");
        mock.setCreatedDate(new Date());
        mock.setUpdatedDate(new Date());
        mock.setIsActive(true);
        final JsonObject query = mock.toJson();
        return rxDBService.rxInsertOne(Mock.DB_TABLE, query).map(result -> {
            query.put("_id", result);
            return query;
        });
    }

    public Single<JsonObject> doFindMock(final JsonObject query, final JsonObject fields) {
        return rxDBService.rxFindOne(Mock.DB_TABLE, query, fields).map(result -> {
            if (null == result) {
                result = new JsonObject().put("info", "No record found");
            }
            return result;
        });
    }
}