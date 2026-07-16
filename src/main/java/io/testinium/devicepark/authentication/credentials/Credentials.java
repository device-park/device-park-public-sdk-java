package io.testinium.devicepark.authentication.credentials;

/**
 * Immutable credentials holder used in the OAuth2 client-credentials flow.
 *
 * <p>Can only be created via the {@link #of(String, String)} factory method;
 * both fields are guaranteed to be non-empty and non-{@code null}.</p>
 *
 * <h2>Example</h2>
 * <pre>
 * Credentials credentials = Credentials.of("my-client-id", "my-client-secret");
 * </pre>
 *
 * <h2>Security</h2>
 * <p>The {@code clientSecret} is sensitive information. Avoid writing it to
 * logs, exception messages, or persistent storage.</p>
 *
 * @since 1.0.0
 */
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

