package io.greyseal.messi.handler;


import com.greyseal.vertx.boot.Constant.MediaType;
import com.greyseal.vertx.boot.annotation.RequestMapping;
import com.greyseal.vertx.boot.handler.BaseHandler;
import com.greyseal.vertx.boot.helper.ConfigHelper;
import io.greyseal.messi.helper.MessiMockHelper;
import io.greyseal.messi.model.Mock;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.MultiMap;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

@RequestMapping(path = "/mock")
public class MessiMockHandler extends BaseHandler {
    private final static MessiMockHelper messiMockHelper = MessiMockHelper.INSTANCE;
    private final static String CONTEXT_PATH = String.join("/", ConfigHelper.getContextPath(), "mock/server");

    public MessiMockHandler(final Vertx vertx) {
        super(vertx);
    }

    @Override
    @RequestMapping(path = "/server/*", method = HttpMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handle(final RoutingContext event) {
        getMockResponse(event);
    }

    @RequestMapping(path = "/server/*", method = HttpMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void doPost(final RoutingContext event) {
        getMockResponse(event);
    }

    @RequestMapping(path = "/server/*", method = HttpMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void doPut(final RoutingContext event) {
        getMockResponse(event);
    }

    @RequestMapping(path = "/server/*", method = HttpMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void doDelete(final RoutingContext event) {
        getMockResponse(event);
    }

    @RequestMapping(path = "/create", method = HttpMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(final RoutingContext event) {
        final JsonObject requestBody = event.getBodyAsJson();
        final Mock mock = new Mock(requestBody);
        Objects.requireNonNull(mock.getStatusCode(), "Http Status Code should have value");
        Objects.requireNonNull(mock.getUrl(), "API url should have value");
        messiMockHelper.doCreateMock(mock)
                .doOnSuccess(result -> {
                    event.setBody(Buffer.buffer(result.toString()));
                    event.next();
                }).doOnError(error -> {
            event.fail(error);
        }).subscribe();
    }

    private void getMockResponse(final RoutingContext event) {
        final HttpServerRequest serverRequest = event.request();
        final String requestedStatusCode = serverRequest.getParam("statusCode");
        final Integer statusCode = !StringUtils.isBlank(requestedStatusCode) ? Integer.parseInt(requestedStatusCode) : HttpResponseStatus.OK.code();
        final String requestURL = serverRequest.path();
        final String requestedURL = requestURL.replace(CONTEXT_PATH, "");
        final JsonObject query = new JsonObject().put("statusCode", statusCode);
        query.put("url", requestedURL);
        final JsonObject fields = new JsonObject();
        fields.put("response", true);
        fields.put("headers", true);
        fields.put("_id", false);
        messiMockHelper.doFindMock(query, fields)
                .doOnSuccess(result -> {
                    event.setBody(Buffer.buffer(result.getJsonObject("response").toString()));
                    event.response().setStatusCode(statusCode);
                    event.response().headers().addAll(getHeaders(result.getJsonObject("headers", new JsonObject())));
                    event.next();
                }).doOnError(error -> {
            event.fail(error);
        }).subscribe();
    }

    private MultiMap getHeaders(final JsonObject headers) {
        final MultiMap multiMap = MultiMap.caseInsensitiveMultiMap();
        if (null != headers && headers.size() > 0) {
            headers.forEach(k -> {
                multiMap.add(k.getKey(), (String) k.getValue());
            });
        }
        return multiMap;
    }
}