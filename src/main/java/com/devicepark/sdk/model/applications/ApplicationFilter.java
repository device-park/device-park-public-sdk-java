package com.devicepark.sdk.model.applications;

/**
 * Application listeleme isteklerinde kullanılabilecek filter alanları.
 *
 * @since 1.0.0
 */
public enum ApplicationFilter {
    /**
     * {@code fileKey} üzerinden tam eşleşme.
     */
    FILE_KEY("fileKey", String.class),
    /**
     * {@code filePath} üzerinden eşleşme.
     */
    FILE_PATH("filePath", String.class),
    /**
     * {@code version} üzerinden eşleşme.
     */
    VERSION("version", String.class),
    /**
     * {@code revision} üzerinden sayısal karşılaştırma.
     */
    REVISION("revision", Long.class);

    private final String dbField;
    private final Class<?> aClass;

    ApplicationFilter(String dbField, Class<?> aClass) {
        this.dbField = dbField;
        this.aClass = aClass;
    }

    /**
     * @return server tarafındaki DB alan adı
     */
    public String getDbField() {
        return dbField;
    }

    /**
     * @return bu alan için beklenen değer tipi
     */
    public Class<?> getAClass() {
        return aClass;
    }
}
