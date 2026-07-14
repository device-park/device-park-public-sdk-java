package io.testinium.devicepark.model.common;

/**
 * Filter karşılaştırma operatörleri.
 *
 * <p>Server tarafındaki {@code SearchOperation} enum'ı ile birebir eşleşir.
 * Tipik kullanım, listeleme uçlarındaki {@code addFilter(...)} çağrılarındadır.</p>
 *
 * <h2>Öörnek</h2>
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

