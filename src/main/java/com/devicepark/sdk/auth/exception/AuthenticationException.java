package com.devicepark.sdk.auth.exception;

import com.devicepark.sdk.core.exception.SdkClientException;

/**
 * Authentication akışı (token alma, refresh) sırasında oluşan hatalar.
 */
public class AuthenticationException extends SdkClientException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

