package com.devicepark.sdk.model.sessions.screenRecord;

/**
 * Screen-record listeleme isteklerinde kullanılabilecek filter alanları.
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

