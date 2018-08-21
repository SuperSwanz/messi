package io.greyseal.messi.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.Map;

@DataObject(generateConverter = true)
public class Mock extends Base {

    public static final String DB_TABLE = "mock_api";
    private static final long serialVersionUID = 7012872680076147456L;

    private String url;
    private Integer statusCode;
    private JsonObject response;
    private Map<String, String> headers;

    // Mandatory for data objects
    public Mock(final JsonObject jsonObject) {
        MockConverter.fromJson(jsonObject, this);
        fromBaseJson(jsonObject, this);
    }

    // for api handlers
    public Mock() {
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        MockConverter.toJson(this, json);
        toBaseJson(this, json);
        return json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public JsonObject getResponse() {
        return response;
    }

    public void setResponse(JsonObject response) {
        this.response = response;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
