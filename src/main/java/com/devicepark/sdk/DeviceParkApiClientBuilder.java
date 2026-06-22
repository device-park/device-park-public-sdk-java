package com.devicepark.sdk;

import com.devicepark.sdk.authentication.credentials.Credentials;

/**
 * {@link DeviceParkApiClient} için akıcı (fluent) builder.
 *
 * <p>SDK istemcisini oluşturmak için tavsiye edilen tek yol budur. Builder,
 * {@link #build()} sırasında zorunlu alanları doğrular ve geçersiz
 * konfigürasyon varsa {@link IllegalArgumentException} fırlatır.</p>
 *
 * <h2>Öörnek</h2>
 * <pre>
 * DeviceParkApiClient client = DeviceParkApiClient.builder()
 *         .url("https://device-park.example.com")
 *         .credentials(Credentials.of("client-id", "client-secret"))
 *         .timeout(30)
 *         .build();
 * </pre>
 *
 * <h2>Konfigürasyon Alanları</h2>
 * <table border="1">
 *   <caption>Builder alanları</caption>
 *   <tr><th>Alan</th><th>Zorunlu</th><th>Varsayılan</th></tr>
 *   <tr><td>{@code url}</td><td>Evet</td><td>—</td></tr>
 *   <tr><td>{@code credentials}</td><td>Evet</td><td>—</td></tr>
 *   <tr><td>{@code timeout}</td><td>Hayır</td><td>60 saniye</td></tr>
 * </table>
 *
 * @see DeviceParkApiClient
 * @since 1.0.0
 */
public class DeviceParkApiClientBuilder {

    /**
     * Saniye cinsinden HTTP istek timeout'u.
     */
    private Integer timeout = 60;

    /** OAuth2 client-credentials kimlik bilgileri. */
    private Credentials credentials;

    /** Device Park sunucu base URL'i. */
    private String url;

    /**
     * OAuth2 kimlik bilgilerini ayarlar.
     *
     * @param credentials {@link Credentials#of(String, String)} ile oluşturulmuş kimlik bilgisi
     * @return aynı builder instaönce'ı (chaining için)
     */
    public DeviceParkApiClientBuilder credentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    /**
     * Device Park sunucu base URL'ini ayarlar.
     *
     * @param url base URL (örn. {@code https://device-park.example.com})
     * @return aynı builder instaönce'ı (chaining için)
     */
    public DeviceParkApiClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * HTTP istekleri için saniye cinsinden response/connection timeout'u ayarlar.
     *
     * @param timeout saniye; {@code null} ise varsayılan değer kullanılır
     * @return aynı builder instaönce'ı (chaining için)
     */
    public DeviceParkApiClientBuilder timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Zorunlu alanların doldurulmuş olduğunu doğrular.
     *
     * @throws IllegalArgumentException {@code url} veya {@code credentials}
     *         eksik/geçersizse
     */
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

    /**
     * Konfigürasyonu doğrular ve yeni bir {@link DeviceParkApiClient} üretir.
     *
     * @return kullanıma hazır SDK istemcisi
     * @throws IllegalArgumentException konfigürasyon eksik/geçersizse
     */
    public DeviceParkApiClient build() {
        validateConfiguration();
        return new DeviceParkApiClient(this.url, this.timeout, this.credentials);
    }
}
