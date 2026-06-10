package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.util.Validate;

import java.util.Objects;

/**
 * Statik (immutable) {@link DeviceParkCredentials} implementasyonu.
 */
public final class BasicCredentials implements DeviceParkCredentials {

    private final String clientId;
    private final String clientSecret;

    private BasicCredentials(String clientId, String clientSecret) {
        this.clientId = Validate.notBlank(clientId, "clientId");
        this.clientSecret = Validate.notBlank(clientSecret, "clientSecret");
    }

    public static BasicCredentials create(String clientId, String clientSecret) {
        return new BasicCredentials(clientId, clientSecret);
    }

    @Override
    public String clientId() {
        return clientId;
    }

    @Override
    public String clientSecret() {
        return clientSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicCredentials)) return false;
        BasicCredentials that = (BasicCredentials) o;
        return clientId.equals(that.clientId) && clientSecret.equals(that.clientSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret);
    }

    @Override
    public String toString() {
        // secret'i asla loglama
        return "BasicCredentials(clientId=" + clientId + ", clientSecret=***)";
    }
}

