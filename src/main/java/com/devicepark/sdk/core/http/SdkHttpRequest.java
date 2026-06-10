package com.devicepark.sdk.core.http;

import com.devicepark.sdk.core.util.Validate;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Immutable HTTP request soyutlaması. Builder ile kurulur.
 */
public final class SdkHttpRequest {

    private final HttpMethod method;
    private final URI uri;
    private final Map<String, String> headers;
    private final byte[] body;
    private final String contentType;

    private SdkHttpRequest(Builder b) {
        this.method = Validate.notNull(b.method, "method");
        this.uri = Validate.notNull(b.uri, "uri");
        this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(b.headers));
        this.body = b.body;
        this.contentType = b.contentType;
    }

    public HttpMethod method() { return method; }
    public URI uri() { return uri; }
    public Map<String, String> headers() { return headers; }
    public byte[] body() { return body; }
    public String contentType() { return contentType; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private HttpMethod method = HttpMethod.GET;
        private URI uri;
        private final Map<String, String> headers = new LinkedHashMap<>();
        private byte[] body;
        private String contentType;

        public Builder method(HttpMethod method) { this.method = method; return this; }
        public Builder uri(URI uri) { this.uri = uri; return this; }
        public Builder header(String name, String value) { this.headers.put(name, value); return this; }
        public Builder body(byte[] body, String contentType) {
            this.body = body;
            this.contentType = contentType;
            return this;
        }

        public SdkHttpRequest build() { return new SdkHttpRequest(this); }
    }
}

