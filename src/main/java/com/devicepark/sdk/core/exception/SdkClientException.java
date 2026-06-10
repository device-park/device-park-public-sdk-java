package com.devicepark.sdk.core.exception;

/**
 * İstemci tarafında (yapılandırma, geçersiz parametre, credential bulunamadı vb.)
 * oluşan hataları temsil eder.
 */
public class SdkClientException extends SdkException {

    private static final long serialVersionUID = 1L;

    public SdkClientException(String message) {
        super(message);
    }

    public SdkClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

