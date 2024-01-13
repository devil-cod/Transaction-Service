package transaction.com.demo.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TsStatusCode {
    SUCCESS("ES_200"),
    BAD_REQUEST("ES_400"),
    INTERNAL_SERVER_ERROR("ES_500"),
    UNAUTHORIZED("ES_500");

    private final String msg;

    TsStatusCode(String name) {
        this.msg = name;
    }

    public boolean equalsToString(String otherName) {
        return msg.equals(otherName);
    }

    @JsonValue
    public String getValue() {
        return this.msg;
    }

    @JsonProperty
    public String getProperty() {
        return this.msg;
    }
}
