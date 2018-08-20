package io.greyseal.messi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.greyseal.messi.model.Mock;
import io.vertx.core.json.JsonObject;

@JsonDeserialize(builder = MockDTO.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MockDTO {
    @JsonIgnore
    private String mockURL;
    @JsonIgnore
    private Integer code;
    @JsonIgnore
    private JsonObject response;

    private MockDTO(final Builder builder) {
        this.mockURL = builder.mockURL;
        this.response = builder.response;
        this.code = builder.code;
    }

    public static Mock toMock(final MockDTO mockDTO) {
        final Mock mock = new Mock();
        mock.setStatusCode(mockDTO.getCode());
        mock.setResponse(mockDTO.getResponse());
        mock.setUrl(mockDTO.getMockURL());
        return mock;
    }

    public String getMockURL() {
        return mockURL;
    }

    public Integer getCode() {
        return code;
    }

    public JsonObject getResponse() {
        return response;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "set")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private String mockURL;
        private Integer code;
        private JsonObject response;

        public Builder() {
        }

        public Builder setMockURL(final String mockURL) {
            this.mockURL = mockURL;
            return this;
        }

        public Builder setCode(final Integer code) {
            this.code = code;
            return this;
        }

        public Builder setPassword(final JsonObject response) {
            this.response = response;
            return this;
        }

        public MockDTO build() {
            return new MockDTO(this);
        }
    }
}