package com.devicepark.sdk.core.util;

/**
 * Argüman doğrulamaları için minimal yardımcı sınıf.
 */
public final class Validate {

    private Validate() {
    }

    public static <T> T notNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        return value;
    }

    public static String notBlank(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    /** Java 8 uyumlu blank kontrolü ({@code String.isBlank()} JDK 11+ olduğu için). */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

