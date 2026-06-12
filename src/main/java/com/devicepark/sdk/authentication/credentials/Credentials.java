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
}

