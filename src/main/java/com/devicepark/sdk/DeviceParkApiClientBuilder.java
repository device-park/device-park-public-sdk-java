package com.devicepark.sdk;

import com.devicepark.sdk.authentication.credentials.Credentials;

public class DeviceParkApiClientBuilder {
    private Integer timeout = 60;
    private Credentials credentials;
    private String url;

    public DeviceParkApiClientBuilder credentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public DeviceParkApiClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    public DeviceParkApiClientBuilder timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }



    protected void validateConfiguration() {
        if (this.url == null || this.url.trim().isEmpty()) {
            throw new IllegalArgumentException("url cannot be null or empty");
        }
        if (this.credentials == null
                || this.credentials.getClientId().isEmpty()
                || this.credentials.getClientSecret().isEmpty()) {
            throw new IllegalArgumentException("Credentials provider cannot be null or empty");
        }
    }

    public DeviceParkApiClient build() {
        validateConfiguration();
        return new DeviceParkApiClient(this.url, this.timeout, this.credentials);
    }
}
