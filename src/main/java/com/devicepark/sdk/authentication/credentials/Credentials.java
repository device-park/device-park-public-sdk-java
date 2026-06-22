package com.devicepark.sdk.authentication.credentials;

/**
 * OAuth2 client-credentials akışında kullanılan değiştirilemez (immutable)
 * kimlik bilgisi tutucusu.
 *
 * <p>Yalnızca {@link #of(String, String)} fabrika metodu üzerinden oluşturulabilir;
 * her iki alanın da boş veya {@code null} olmaması garanti edilir.</p>
 *
 * <h2>Öörnek</h2>
 * <pre>
 * Credentials credentials = Credentials.of("my-client-id", "my-client-secret");
 * </pre>
 *
 * <h2>Güvenlik</h2>
 * <p>{@code clientSecret} hassas bir bilgidir. Loglara, exception mesajlarına
 * veya kalıcı depolamaya yazmaktan kaçının.</p>
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

