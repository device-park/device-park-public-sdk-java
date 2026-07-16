package io.testinium.devicepark;

import io.testinium.devicepark.authentication.credentials.Credentials;

/**
 * Fluent builder for {@link DeviceParkApiClient}.
 *
 * <p>This is the recommended way to create an SDK client. The builder validates
 * required fields during {@link #build()} and throws {@link IllegalArgumentException}
 * if configuration is invalid.</p>
 *
 * <h2>Example</h2>
 * <pre>
 * DeviceParkApiClient client = DeviceParkApiClient.builder()
 *         .url("https://device-park.example.com")
 *         .credentials(Credentials.of("client-id", "client-secret"))
 *         .timeout(30)
 *         .build();
 * </pre>
 *
 * <h2>Configuration Fields</h2>
 * <table border="1">
 *   <caption>Builder fields</caption>
 *   <tr><th>Field</th><th>Required</th><th>Default</th></tr>
 *   <tr><td>{@code url}</td><td>Yes</td><td>—</td></tr>
 *   <tr><td>{@code credentials}</td><td>Yes</td><td>—</td></tr>
 *   <tr><td>{@code timeout}</td><td>No</td><td>60 seconds</td></tr>
 * </table>
 *
 * @see DeviceParkApiClient
 * @since 1.0.0
 */
public class DeviceParkApiClientBuilder {

    /**
     * HTTP request timeout in seconds.
     */
    private Integer timeout = 60;

    /**
     * OAuth2 client-credentials credentials.
     */
    private Credentials credentials;

    /** Device Park server base URL. */
    private String url;

    /**
     * Sets the OAuth2 credentials.
     *
     * @param credentials credentials created via {@link Credentials#of(String, String)}
     * @return the same builder instance (for chaining)
     */
    public DeviceParkApiClientBuilder credentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    /**
     * Sets the Device Park server base URL.
     *
     * @param url base URL (e.g., {@code https://device-park.example.com})
     * @return the same builder instance (for chaining)
     */
    public DeviceParkApiClientBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the HTTP request timeout in seconds for response/connection timeout.
     *
     * @param timeout timeout in seconds; if {@code null}, the default value is used
     * @return the same builder instance (for chaining)
     */
    public DeviceParkApiClientBuilder timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Validates that required fields are filled.
     *
     * @throws IllegalArgumentException if {@code url} or {@code credentials}
     *         are missing or invalid
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
     * Validates configuration and produces a new {@link DeviceParkApiClient}.
     *
     * @return a ready-to-use SDK client
     * @throws IllegalArgumentException if configuration is missing or invalid
     */
    public DeviceParkApiClient build() {
        validateConfiguration();
        return new DeviceParkApiClient(this.url, this.timeout, this.credentials);
    }
}
