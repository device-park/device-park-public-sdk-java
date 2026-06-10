package com.devicepark.sdk.core.http;

import com.devicepark.sdk.auth.credentials.DeviceParkCredentialsProvider;
import com.devicepark.sdk.core.endpoint.Environment;
import com.devicepark.sdk.core.log.LogConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ClientOptions {
    private final Environment environment;

    private final Map<String, String> headers;

    private final Map<String, Supplier<String>> headerSuppliers;

    private final ApacheHttp5Client httpClient;

    private final int timeout;

    private final int maxRetries;

    private final Optional<LogConfig> logging;

    private final String bearerToken;

    private final DeviceParkCredentialsProvider credentialsProvider;

    private ClientOptions(Environment environment,
                          Map<String, String> headers,
                          Map<String, Supplier<String>> headerSuppliers,
                          ApacheHttp5Client httpClient,
                          int timeout,
                          int maxRetries,
                          Optional<LogConfig> logging,
                          String bearerToken,
                          DeviceParkCredentialsProvider credentialsProvider) {
        this.environment = environment;
        this.headers = new HashMap<String, String>(headers);
        this.headers.put("X-Fern-Language", "JAVA");
        this.headers.put("X-Fern-SDK-Name", "com.device-park.fern:api-sdk");
        this.headers.put("X-Fern-SDK-Version", "0.0.3");
        this.headerSuppliers = new HashMap<String, Supplier<String>>(headerSuppliers);
        this.httpClient = httpClient;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.logging = logging;
        this.bearerToken = bearerToken;
        this.credentialsProvider = credentialsProvider;
    }

    public Environment environment() {
        return this.environment;
    }

    public Map<String, String> headers(RequestOptions requestOptions) {
        Map<String, String> values = new HashMap<String, String>(this.headers);
        headerSuppliers.forEach((key, supplier) -> values.put(key, supplier.get()));
        if (requestOptions != null) {
            values.putAll(requestOptions.getHeaders());
        }
        return values;
    }

    public int timeout(RequestOptions requestOptions) {
        if (requestOptions == null) {
            return this.timeout;
        }
        return requestOptions.getTimeout().orElse(this.timeout);
    }

    public ApacheHttp5Client httpClient() {
        return this.httpClient;
    }

    // ApacheHttp5Client immutable olduğu için request-level timeout override burada uygulanmıyor.
    public ApacheHttp5Client httpClientWithTimeout(RequestOptions requestOptions) {
        return this.httpClient;
    }

    public int maxRetries() {
        return this.maxRetries;
    }

    public Optional<LogConfig> logging() {
        return this.logging;
    }

    public Optional<String> bearerToken() {
        return Optional.ofNullable(this.bearerToken);
    }

    public Optional<DeviceParkCredentialsProvider> credentialsProvider() {
        return Optional.ofNullable(this.credentialsProvider);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Environment environment;

        private final Map<String, String> headers = new HashMap<String, String>();

        private final Map<String, Supplier<String>> headerSuppliers = new HashMap<String, Supplier<String>>();

        private int maxRetries = 2;

        private Optional<Integer> timeout = Optional.empty();

        private ApacheHttp5Client httpClient = null;

        private Optional<LogConfig> logging = Optional.empty();

        private String bearerToken;

        private DeviceParkCredentialsProvider credentialsProvider;

        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder addHeader(String key, Supplier<String> value) {
            this.headerSuppliers.put(key, value);
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = Optional.of(timeout);
            return this;
        }

        public Builder timeout(Optional<Integer> timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder httpClient(ApacheHttp5Client httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder logging(LogConfig logging) {
            this.logging = Optional.of(logging);
            return this;
        }

        public Builder bearerToken(String bearerToken) {
            this.bearerToken = bearerToken;
            return this;
        }

        public Builder credentialsProvider(DeviceParkCredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
            return this;
        }

        public ClientOptions build() {
            ApacheHttp5Client resolvedHttpClient = this.httpClient != null
                    ? this.httpClient
                    : ApacheHttp5Client.builder()
                    .responseTimeout(Duration.ofSeconds(this.timeout.orElse(60)))
                    .build();

            int resolvedTimeout = this.timeout.orElse(60);
            return new ClientOptions(
                    environment,
                    headers,
                    headerSuppliers,
                    resolvedHttpClient,
                    resolvedTimeout,
                    this.maxRetries,
                    this.logging,
                    this.bearerToken,
                    this.credentialsProvider
            );
        }

        public static Builder from(ClientOptions clientOptions) {
            Builder builder = new Builder();
            builder.environment = clientOptions.environment();
            builder.timeout = Optional.of(clientOptions.timeout(null));
            builder.httpClient = clientOptions.httpClient();
            builder.headers.putAll(clientOptions.headers);
            builder.headerSuppliers.putAll(clientOptions.headerSuppliers);
            builder.maxRetries = clientOptions.maxRetries();
            builder.logging = clientOptions.logging();
            builder.bearerToken = clientOptions.bearerToken().orElse(null);
            builder.credentialsProvider = clientOptions.credentialsProvider().orElse(null);
            return builder;
        }
    }
}
