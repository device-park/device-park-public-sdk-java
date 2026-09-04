package io.testinium.devicepark.model.devices;

/**
 * Filter fields that can be used in device listing requests.
 *
 * @since 1.0.0
 */
public enum DeviceFilter {
    POOL_ID("devicePools.id", String.class),
    SERIAL_NUMBER("serial", String.class),
    MARKETING_NAME("marketName", String.class),
    MANUFACTURER("manufacturer", String.class),
    MODEL_NAME("model", String.class),
    PLATFORM("platform", String.class),
    PLATFORM_VERSION("platformVersion", String.class),
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

