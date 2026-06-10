package com.devicepark.sdk.auth;

import com.devicepark.sdk.auth.credentials.DefaultCredentialsProviderChain;
import com.devicepark.sdk.auth.credentials.DeviceParkCredentialsProvider;
import com.devicepark.sdk.auth.credentials.StaticCredentialsProvider;
import com.devicepark.sdk.auth.token.AccessToken;
import com.devicepark.sdk.auth.token.CachingTokenManager;
import com.devicepark.sdk.auth.token.OAuth2TokenClient;
import com.devicepark.sdk.auth.token.TokenManager;
import com.devicepark.sdk.core.endpoint.DefaultEndpointProviderChain;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.endpoint.StaticEndpointProvider;
import com.devicepark.sdk.core.http.ApacheHttp5Client;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.retry.RetryPolicy;
import com.devicepark.sdk.core.retry.RetryableHttpClient;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;

/**
 * Device Park Authentication SDK'sının ana giriş noktası. AWS SDK
 * service client'larının ({@code S3Client}, {@code DynamoDbClient} vb.) muadilidir.
 *
 * <p>Tipik kullanım:</p>
 * <pre>
 * try (var client = DeviceParkAuthClient.builder()
 *         .endpoint("https://dev-devicepark.testinium.io")
 *         .credentials("client-id", "client-secret")
 *         .build()) {
 *
 *     String header = client.authorizationHeader();
 *     // header = "Bearer eyJhbGciOi..."
 * }
 * </pre>
 *
 * <p>Token endpoint path'i ({@code /uaa/oauth2/token}) ve scope ({@code openid})
 * Device Park için sabittir; gerekirse {@link Builder#tokenPath(String)} ve
 * {@link Builder#scope(String)} ile override edilebilir.</p>
 *
 * <p>Veya tüm kaynakları default chain'den çözmek için:</p>
 * <pre>
 * try (var client = DeviceParkAuthClient.create()) {
 *     AccessToken token = client.getToken();
 * }
 * </pre>
 */
public final class DeviceParkAuthClient implements Closeable {

    private final TokenManager tokenManager;
    private final SdkHttpClient httpClient;
    private final EndpointProvider endpointProvider;
    private final boolean ownsHttpClient;

    private DeviceParkAuthClient(TokenManager tm, SdkHttpClient hc, EndpointProvider ep, boolean ownsHc) {
        this.tokenManager = tm;
        this.httpClient = hc;
        this.endpointProvider = ep;
        this.ownsHttpClient = ownsHc;
    }

    /** Tüm bağımlılıklar default chain'den çözülerek bir client oluşturur. */
    public static DeviceParkAuthClient create() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /** Geçerli access token'ı (gerekirse refresh ederek) döner. */
    public AccessToken getToken() {
        return tokenManager.getToken();
    }

    /** Hazır {@code Authorization} header değeri döner: {@code Bearer <token>}. */
    public String authorizationHeader() {
        return tokenManager.getToken().authorizationHeader();
    }

    /** Cache'i geçersiz kılar ve token'ı zorla yeniler. */
    public AccessToken refreshToken() {
        return tokenManager.refresh();
    }

    /**
     * Bu auth client'ın kullandığı endpoint provider. Aynı endpoint'i paylaşmak isteyen
     * diğer SDK client'ları (örn. {@code DeviceParkManagementService}) tarafından kullanılır.
     */
    public EndpointProvider endpointProvider() {
        return endpointProvider;
    }

    @Override
    public void close() throws IOException {
        if (ownsHttpClient) {
            httpClient.close();
        }
    }

    public static final class Builder {
        /** Device Park UAA OAuth2 token endpoint path'i. */
        private static final String DEFAULT_TOKEN_PATH = "/uaa/oauth2/token";
        /** Device Park için varsayılan OAuth2 scope. */
        private static final String DEFAULT_SCOPE = "openid";

        private DeviceParkCredentialsProvider credentialsProvider;
        private EndpointProvider endpointProvider;
        private SdkHttpClient httpClient;
        private RetryPolicy retryPolicy = RetryPolicy.defaults();
        private String tokenPath = DEFAULT_TOKEN_PATH;
        private String scope = DEFAULT_SCOPE;
        private Duration tokenSafetyMargin = Duration.ofSeconds(30);

        public Builder credentialsProvider(DeviceParkCredentialsProvider v) {
            this.credentialsProvider = v; return this;
        }
        public Builder credentials(String clientId, String clientSecret) {
            this.credentialsProvider = StaticCredentialsProvider.create(clientId, clientSecret);
            return this;
        }
        public Builder endpointProvider(EndpointProvider v) {
            this.endpointProvider = v; return this;
        }
        public Builder endpoint(String uri) {
            this.endpointProvider = StaticEndpointProvider.create(uri);
            return this;
        }
        public Builder httpClient(SdkHttpClient v) { this.httpClient = v; return this; }
        public Builder retryPolicy(RetryPolicy v) { this.retryPolicy = v; return this; }
        public Builder tokenPath(String v) { this.tokenPath = v; return this; }
        public Builder scope(String v) { this.scope = v; return this; }
        public Builder tokenSafetyMargin(Duration v) { this.tokenSafetyMargin = v; return this; }

        public DeviceParkAuthClient build() {
            DeviceParkCredentialsProvider creds = credentialsProvider != null
                    ? credentialsProvider
                    : DefaultCredentialsProviderChain.create();
            EndpointProvider ep = endpointProvider != null
                    ? endpointProvider
                    : DefaultEndpointProviderChain.create();

            boolean ownsHttp = (httpClient == null);
            SdkHttpClient base = httpClient != null ? httpClient : ApacheHttp5Client.create();
            SdkHttpClient effective = retryPolicy != null && retryPolicy.maxAttempts() > 1
                    ? new RetryableHttpClient(base, retryPolicy)
                    : base;

            OAuth2TokenClient tokenClient = OAuth2TokenClient.builder()
                    .httpClient(effective)
                    .endpointProvider(ep)
                    .credentialsProvider(creds)
                    .tokenPath(tokenPath)
                    .scope(scope)
                    .build();

            TokenManager tm = CachingTokenManager.builder()
                    .tokenClient(tokenClient)
                    .safetyMargin(tokenSafetyMargin)
                    .build();

            return new DeviceParkAuthClient(tm, base, ep, ownsHttp);
        }
    }
}

