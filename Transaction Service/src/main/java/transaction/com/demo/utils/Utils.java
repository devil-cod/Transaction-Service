package transaction.com.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Utils {

    public static final PropertyNamingStrategies.NamingBase snakeCaseStrategy =
        new PropertyNamingStrategies.NamingBase() {
            @Override
            public String translate(String s) {
                return Utils.camelToSnakeCase(s);
            }
        };

    private Utils() {
    }

    public static String camelToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").
            replaceAll("([a-z])(\\d)", "$1_$2").toLowerCase();
    }

    public static ObjectMapper getSnakeCaseObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(snakeCaseStrategy);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static String getCleanedBody(String body) {
        if (body == null)
            return null;
        return body.replaceAll("[\r\n]+", " ").replaceAll(" +", " ");
    }

    public static LocalDateTime getTimestamp() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }
}
