package com.devicepark.sdk.core.http;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable HTTP response. Body byte[] olarak tutulur; convenience method ile string okunabilir.
 */
public final class SdkHttpResponse {

    private final int statusCode;
    private final Map<String, String> headers;
    private final byte[] body;

    public SdkHttpResponse(int statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(headers != null ? headers : Collections.<String, String>emptyMap()));
        this.body = body != null ? body : new byte[0];
    }

    public int statusCode() { return statusCode; }
    public Map<String, String> headers() { return headers; }
    public byte[] body() { return body; }

    public String bodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }
}

