package io.testinium.devicepark.model;

import org.apache.hc.core5.http.HttpStatus;

/**
 * General-purpose SDK response envelope.
 *
 * <p>Currently serves as a placeholder for internal use; can be extended
 * in the future to carry standard error/success responses.</p>
 *
 * @since 1.0.0
 */
public class SdkResponse {
    private final HttpStatus status;
    private final String message;

    public SdkResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
