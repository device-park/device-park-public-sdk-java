package com.devicepark.sdk.management;

import com.devicepark.sdk.DeviceParkApiClient;
import com.devicepark.sdk.DeviceParkApiClientBuilder;
import com.devicepark.sdk.auth.credentials.DeviceParkCredentialsProvider;
import com.devicepark.sdk.core.endpoint.Environment;
import com.devicepark.sdk.core.http.ApacheHttp5Client;
import com.devicepark.sdk.core.log.LogConfig;
import com.devicepark.sdk.management.devices.DevicesApi;

import java.io.Closeable;
import java.io.IOException;

/**
 * Management odakli facade.
 * Kullaniciya doğrudan devices() gibi management API'lerini sunar.
 */
public final class DeviceParkManagementClient implements Closeable {

    private final DeviceParkApiClient apiClient;

    private DeviceParkManagementClient(DeviceParkApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public static Builder builder() {
        return new Builder();
    }

    public DevicesApi devices() {
        return apiClient.management().devices();
    }

    public DeviceParkManagementService management() {
        return apiClient.management();
    }

    @Override
    public void close() throws IOException {
        apiClient.close();
    }

    public static final class Builder {
        private final DeviceParkApiClientBuilder delegate = DeviceParkApiClient.builder();

        public Builder endpoint(String endpoint) {
            delegate.endpoint(endpoint);
            return this;
        }

        public Builder url(String url) {
            delegate.url(url);
            return this;
        }

        public Builder environment(Environment environment) {
            delegate.environment(environment);
            return this;
        }

        public Builder credentials(String clientId, String clientSecret) {
            delegate.credentials(clientId, clientSecret);
            return this;
        }

        public Builder credentialsProvider(DeviceParkCredentialsProvider credentialsProvider) {
            delegate.credentialsProvider(credentialsProvider);
            return this;
        }

        public Builder token(String token) {
            delegate.token(token);
            return this;
        }

        public Builder timeout(int timeout) {
            delegate.timeout(timeout);
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            delegate.maxRetries(maxRetries);
            return this;
        }

        public Builder httpClient(ApacheHttp5Client httpClient) {
            delegate.httpClient(httpClient);
            return this;
        }

        public Builder logging(LogConfig logConfig) {
            delegate.logging(logConfig);
            return this;
        }

        public Builder addHeader(String name, String value) {
            delegate.addHeader(name, value);
            return this;
        }

        public DeviceParkManagementClient build() {
            return new DeviceParkManagementClient(delegate.build());
        }
    }
}

