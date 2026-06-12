package com.devicepark.sdk.model;

import org.apache.hc.core5.http.HttpStatus;

public class SdkResponse {
    private final HttpStatus status;
    private final String message;

    public SdkResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
