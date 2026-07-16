package io.testinium.devicepark.model.sessions;

/**
 * Filter fields that can be used in session listing requests.
 *
 * @since 1.0.0
 */
public enum SessionFilter {
    ALLOCATION("allocationId", String.class),
    SESSION("sessionId", String.class),
    SERIAL("deviceSerial", String.class),
    STATE("state", String.class);

    private final String dbField;
    private final Class<?> aClass;

    SessionFilter(String dbField, Class<?> aClass) {
        this.dbField = dbField;
        this.aClass = aClass;
    }

    public String getDbField() {
        return dbField;
    }

    public Class<?> getAClass() {
        return aClass;
    }
}

