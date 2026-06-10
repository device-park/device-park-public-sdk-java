package com.devicepark.sdk.core.exception;

/**
 * Device Park SDK kullanımı sırasında oluşan tüm hataların kök sınıfı.
 * AWS SDK'daki {@code SdkException} muadilidir.
 */
public class SdkException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SdkException(String message) {
        super(message);
    }

    public SdkException(String message, Throwable cause) {
        super(message, cause);
    }
}

