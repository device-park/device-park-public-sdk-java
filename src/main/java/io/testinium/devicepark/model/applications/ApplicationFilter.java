package io.testinium.devicepark.model.applications;

/**
 * Filter fields that can be used in application listing requests.
 *
 * @since 1.0.0
 */
public enum ApplicationFilter {
    /**
     * Exact match on {@code fileKey}.
     */
    FILE_KEY("fileKey", String.class),
    /**
     * Match on {@code filePath}.
     */
    FILE_PATH("filePath", String.class),
    /**
     * Match on {@code version}.
     */
    VERSION("version", String.class),
    /**
     * Numeric comparison on {@code revision}.
     */
    REVISION("revision", Long.class);

    private final String dbField;
    private final Class<?> aClass;

    ApplicationFilter(String dbField, Class<?> aClass) {
        this.dbField = dbField;
        this.aClass = aClass;
    }

    /**
     * @return the DB field name on the server side
     */
    public String getDbField() {
        return dbField;
    }

    /**
     * @return the expected value type for this field
     */
    public Class<?> getAClass() {
        return aClass;
    }
}
