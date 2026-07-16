package io.testinium.devicepark.model.allocation;

/**
 * Filter fields that can be used in allocation listing requests.
 *
 * @since 1.0.0
 */
public enum AllocationFilter {
    ALLOCATION("allocationId", String.class);

    private final String dbField;
    private final Class<?> aClass;

    AllocationFilter(String dbField, Class<?> aClass) {
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

