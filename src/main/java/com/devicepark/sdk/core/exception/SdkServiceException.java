package com.devicepark.sdk.core.exception;

/**
 * Sunucu (service) tarafından dönen hataları temsil eder. HTTP status,
 * error code ve request id gibi bilgileri taşır.
 */
public class SdkServiceException extends SdkException {

    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String errorCode;
    private final String requestId;

    public SdkServiceException(String message, int statusCode, String errorCode, String requestId) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    public SdkServiceException(String message, int statusCode, String errorCode, String requestId, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    public int statusCode() {
        return statusCode;
    }

    public String errorCode() {
        return errorCode;
    }

    public String requestId() {
        return requestId;
    }
}

