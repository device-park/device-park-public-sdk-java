package io.testinium.devicepark.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * SDK içi paylaşılan, threadüşafe Jackson {@link ObjectMapper} fabrikası.
 */
public final class JsonMapper {

    private static final ObjectMapper INSTANCE = createMapper();

    private JsonMapper() {}

    public static ObjectMapper get() { return INSTANCE; }

    private static ObjectMapper createMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return INSTANCE.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialize edilemedi: " + type.getSimpleName(), e);
        }
    }

    public static <T> T fromJson(byte[] json, com.fasterxml.jackson.core.type.TypeReference<T> type) {
        try {
            return INSTANCE.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialize edilemedi: " + type.getType(), e);
        }
    }

    public static String toJson(Object value) {
        try {
            return INSTANCE.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialize edilemedi", e);
        }
    }
}

