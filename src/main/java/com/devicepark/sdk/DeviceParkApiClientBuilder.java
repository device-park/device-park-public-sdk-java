package com.devicepark.sdk;

import com.devicepark.sdk.auth.credentials.DeviceParkCredentialsProvider;
import com.devicepark.sdk.auth.credentials.StaticCredentialsProvider;
import com.devicepark.sdk.core.endpoint.Environment;
import com.devicepark.sdk.core.http.ApacheHttp5Client;
import com.devicepark.sdk.core.http.ClientOptions;
import com.devicepark.sdk.core.log.LogConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class DeviceParkApiClientBuilder {
    private Optional<Integer> timeout = Optional.empty();

    private Optional<Integer> maxRetries = Optional.empty();

    private final Map<String, String> customHeaders = new HashMap<>();

    private String token = null;

    private DeviceParkCredentialsProvider credentialsProvider = null;

    private Environment environment = Environment.DEFAULT;

    private ApacheHttp5Client httpClient;

    private Optional<LogConfig> logging = Optional.empty();

    /**
     * Legacy bearer token desteği. Yeni kullanım için credentials(...) önerilir.
     */
    public DeviceParkApiClientBuilder token(String token) {
        this.token = token;
        return this;
    }

    public DeviceParkApiClientBuilder credentials(String clientId, String clientSecret) {
        this.credentialsProvider = StaticCredentialsProvider.create(clientId, clientSecret);
        return this;
    }

    public DeviceParkApiClientBuilder credentialsProvider(DeviceParkCredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public DeviceParkApiClientBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public DeviceParkApiClientBuilder endpoint(String endpoint) {
        this.environment = Environment.custom(endpoint);
        return this;
    }

    public DeviceParkApiClientBuilder url(String url) {
        this.environment = Environment.custom(url);
        return this;
    }

    /**
     * Sets the timeout (in seconds) for the client. Defaults to 60 seconds.
     */
    public DeviceParkApiClientBuilder timeout(int timeout) {
        this.timeout = Optional.of(timeout);
        return this;
    }

    /**
     * Sets the maximum number of retries for the client. Defaults to 2 retries.
     */
    public DeviceParkApiClientBuilder maxRetries(int maxRetries) {
        this.maxRetries = Optional.of(maxRetries);
        return this;
    }

    /**
     * Sets the underlying HTTP client.
     */
    public DeviceParkApiClientBuilder httpClient(ApacheHttp5Client httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * Configure logging for the SDK. Silent by default — no log output unless explicitly configured.
     */
    public DeviceParkApiClientBuilder logging(LogConfig logging) {
        this.logging = Optional.of(logging);
        return this;
    }

    /**
     * Add a custom header to be sent with all requests.
     * For headers that need to be computed dynamically or conditionally, use the setAdditional() method override instead.
     *
     * @param name The header name
     * @param value The header value
     * @return This builder for method chaining
     */
    public DeviceParkApiClientBuilder addHeader(String name, String value) {
        this.customHeaders.put(name, value);
        return this;
    }

    protected ClientOptions buildClientOptions() {
        ClientOptions.Builder builder = ClientOptions.builder();
        setEnvironment(builder);
        setAuthentication(builder);
        setHttpClient(builder);
        setTimeouts(builder);
        setRetries(builder);
        setLogging(builder);
        for (Map.Entry<String, String> header : this.customHeaders.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        setAdditional(builder);
        return builder.build();
    }

    protected void setEnvironment(ClientOptions.Builder builder) {
        builder.environment(this.environment);
    }

    protected void setAuthentication(ClientOptions.Builder builder) {
        if (this.token != null) {
            builder.bearerToken(this.token);
        }
        if (this.credentialsProvider != null) {
            builder.credentialsProvider(this.credentialsProvider);
        }
    }

    protected void setTimeouts(ClientOptions.Builder builder) {
        if (this.timeout.isPresent()) {
            builder.timeout(this.timeout.get());
        }
    }

    protected void setRetries(ClientOptions.Builder builder) {
        if (this.maxRetries.isPresent()) {
            builder.maxRetries(this.maxRetries.get());
        }
    }

    protected void setHttpClient(ClientOptions.Builder builder) {
        if (this.httpClient != null) {
            builder.httpClient(this.httpClient);
        }
    }

    protected void setLogging(ClientOptions.Builder builder) {
        if (this.logging.isPresent()) {
            builder.logging(this.logging.get());
        }
    }

    protected void setAdditional(ClientOptions.Builder builder) {
    }

    protected void validateConfiguration() {
    }

    public DeviceParkApiClient build() {
        if (credentialsProvider == null) {
            throw new RuntimeException("Please provide credentials(clientId, clientSecret)");
        }
        validateConfiguration();
        return new DeviceParkApiClient(buildClientOptions());
    }
}
