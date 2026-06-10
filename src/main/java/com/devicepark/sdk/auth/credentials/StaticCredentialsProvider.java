package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.util.Validate;

/**
 * Sabit credentials sağlayan provider. Test ve hard-coded entegrasyonlar için.
 *
 * <pre>
 * var provider = StaticCredentialsProvider.create(
 *     BasicCredentials.create("my-client-id", "my-client-secret"));
 * </pre>
 */
public final class StaticCredentialsProvider implements DeviceParkCredentialsProvider {

    private final DeviceParkCredentials credentials;

    private StaticCredentialsProvider(DeviceParkCredentials credentials) {
        this.credentials = Validate.notNull(credentials, "credentials");
    }

    public static StaticCredentialsProvider create(DeviceParkCredentials credentials) {
        return new StaticCredentialsProvider(credentials);
    }

    public static StaticCredentialsProvider create(String clientId, String clientSecret) {
        return new StaticCredentialsProvider(BasicCredentials.create(clientId, clientSecret));
    }

    @Override
    public DeviceParkCredentials resolveCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return "StaticCredentialsProvider(" + credentials + ")";
    }
}

