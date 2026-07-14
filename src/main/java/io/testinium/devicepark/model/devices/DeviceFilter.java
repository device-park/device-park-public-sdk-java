package io.testinium.devicepark.model.devices;

/**
 * Cihaz listeleme isteklerinde kullanılabilecek filter alanları.
 *
 * @since 1.0.0
 */
public enum DeviceFilter {
    POOL_ID("devicePools.id", String.class),
    SERIAL_NUMBER("serial", String.class),
    MARKETING_NAME("marketName", String.class),
    MENUFACTURER("manufacturer", String.class),
    MODEL_NAME("model", String.class),
    PLATFORM("platform", String.class),
    OS_VERSION("osVersion", String.class),
    TAGS("tags.name", String.class),
    STATE("deviceStates.state", String.class);

    private final String dbField;
    private final Class<?> aClass;

    DeviceFilter(String dbField, Class<?> aClass) {
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

