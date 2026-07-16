package io.testinium.devicepark.model.common;

/**
 * Filter comparison operators.
 *
 * <p>Maps one-to-one to the server-side {@code SearchOperation} enum.
 * Typical usage is in {@code addFilter(...)} calls on listing endpoints.</p>
 *
 * <h2>Example</h2>
 * <pre>
 * ListDevicesRequest.builder()
 *     .addFilter(DeviceFilter.PLATFORM, "Android", SearchOperation.EQUAL)
 *     .build();
 * </pre>
 *
 * @since 1.0.0
 */
public enum SearchOperation {
    EQUAL,
    NOT_EQUAL,
    GREATER_THAN,
    LESS_THAN
}

