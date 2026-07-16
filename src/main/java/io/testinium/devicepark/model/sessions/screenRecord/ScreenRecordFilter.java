package io.testinium.devicepark.model.sessions.screenRecord;

/**
 * Filter fields that can be used in screen-record listing requests.
 *
 * @since 1.0.0
 */
public enum ScreenRecordFilter {
    CREATED_AT("createdAt", String.class);

    private final String dbField;
    private final Class<?> aClass;

    ScreenRecordFilter(String dbField, Class<?> aClass) {
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

