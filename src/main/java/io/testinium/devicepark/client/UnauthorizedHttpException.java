package io.testinium.devicepark.client;

import java.io.IOException;

/**
 * Thrown when an authenticated Device Park API call returns HTTP 401.
 * Used internally to invalidate the cached access token and retry once.
 */
final class UnauthorizedHttpException extends IOException {

    private final int statusCode;

    UnauthorizedHttpException(int statusCode, String responseBody) {
        super("HTTP Request Failed with status: " + statusCode + " body: " + responseBody);
        this.statusCode = statusCode;
    }

    int getStatusCode() {
        return statusCode;
    }
}
