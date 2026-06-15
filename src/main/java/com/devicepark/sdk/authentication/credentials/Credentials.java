package com.devicepark.sdk.authentication.credentials;

public final class Credentials {

    private final String clientId;
    private final String clientSecret;

    private Credentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public static Credentials of(String clientId, String clientSecret) {
        if (clientId == null || clientId.trim().isEmpty()) {
            throw new IllegalArgumentException("clientId cannot be null or empty");
        }
        if (clientSecret == null || clientSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("clientSecret cannot be null or empty");
        }
        return new Credentials(clientId, clientSecret);
    }
}

