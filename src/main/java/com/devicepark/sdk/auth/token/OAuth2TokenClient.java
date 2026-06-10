package com.devicepark.sdk.auth.token;

import com.devicepark.sdk.auth.credentials.DeviceParkCredentials;
import com.devicepark.sdk.auth.credentials.DeviceParkCredentialsProvider;
import com.devicepark.sdk.auth.exception.AuthenticationException;
import com.devicepark.sdk.core.endpoint.Endpoint;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.http.HttpMethod;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.http.SdkHttpRequest;
import com.devicepark.sdk.core.http.SdkHttpResponse;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.core.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * OAuth2 Client Credentials akışını çalıştıran low-level token client.
 *
 * <p>POST {endpoint}{tokenPath}
 *   Authorization: Basic base64(clientId:clientSecret)
 *   Content-Type: application/x-www-form-urlencoded
 *   body: grant_type=client_credentials[&amp;scope=...]
 * </p>
 */

public final class OAuth2TokenClient {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2TokenClient.class);

    private final SdkHttpClient httpClient;
    private final EndpointProvider endpointProvider;
    private final DeviceParkCredentialsProvider credentialsProvider;
    private final String tokenPath;
    private final String scope;

    private OAuth2TokenClient(Builder b) {
        this.httpClient = Validate.notNull(b.httpClient, "httpClient");
        this.endpointProvider = Validate.notNull(b.endpointProvider, "endpointProvider");
        this.credentialsProvider = Validate.notNull(b.credentialsProvider, "credentialsProvider");
        this.tokenPath = b.tokenPath;
        this.scope = b.scope;
    }

    public static Builder builder() { return new Builder(); }

    public AccessToken requestToken() {
        Endpoint endpoint = endpointProvider.resolveEndpoint();
        DeviceParkCredentials creds = credentialsProvider.resolveCredentials();

        URI tokenUri = endpoint.resolve(tokenPath);
        String basic = Base64.getEncoder().encodeToString(
                (creds.clientId() + ":" + creds.clientSecret()).getBytes(StandardCharsets.UTF_8));

        StringBuilder form = new StringBuilder("grant_type=client_credentials");
        if (!Validate.isBlank(scope)) {
            try {
                form.append("&scope=").append(URLEncoder.encode(scope, StandardCharsets.UTF_8.name()));
            } catch (java.io.UnsupportedEncodingException e) {
                // UTF-8 her JVM'de zorunludur; ulaşılamaz.
                throw new IllegalStateException(e);
            }
        }

        SdkHttpRequest req = SdkHttpRequest.builder()
                .method(HttpMethod.POST)
                .uri(tokenUri)
                .header("Authorization", "Basic " + basic)
                .header("Accept", "application/json")
                .body(form.toString().getBytes(StandardCharsets.UTF_8),
                        "application/x-www-form-urlencoded; charset=utf-8")
                .build();

        LOG.debug("Token isteği gönderiliyor: {}", tokenUri);
        SdkHttpResponse resp = httpClient.execute(req);

        if (!resp.isSuccessful()) {
            throw new AuthenticationException(
                    "Token alımı başarısız (HTTP " + resp.statusCode() + "): " + resp.bodyAsString());
        }

        try {
            return JsonMapper.get().readValue(resp.body(), AccessToken.class);
        } catch (Exception e) {
            throw new AuthenticationException("Token yanıtı parse edilemedi", e);
        }
    }

    public static final class Builder {
        private SdkHttpClient httpClient;
        private EndpointProvider endpointProvider;
        private DeviceParkCredentialsProvider credentialsProvider;
        private String tokenPath = "/oauth/token";
        private String scope;

        public Builder httpClient(SdkHttpClient v) { this.httpClient = v; return this; }
        public Builder endpointProvider(EndpointProvider v) { this.endpointProvider = v; return this; }
        public Builder credentialsProvider(DeviceParkCredentialsProvider v) { this.credentialsProvider = v; return this; }
        public Builder tokenPath(String v) { this.tokenPath = v; return this; }
        public Builder scope(String v) { this.scope = v; return this; }

        public OAuth2TokenClient build() { return new OAuth2TokenClient(this); }
    }
}

