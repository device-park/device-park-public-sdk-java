package com.devicepark.sdk.auth.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * OAuth2 token endpoint'inden dönen access token. Snake_case JSON ile uyumlu.
 *
 * <p>Standart OAuth2 alanları: {@code access_token}, {@code token_type},
 * {@code expires_in}, {@code refresh_token}, {@code scope}.</p>
 */
public final class AccessToken {

    private final String accessToken;
    private final String tokenType;
    private final long expiresInSeconds;
    private final String refreshToken;
    private final String scope;
    private final Instant issuedAt;

    public AccessToken(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") long expiresInSeconds,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("scope") String scope) {
        this(accessToken, tokenType, expiresInSeconds, refreshToken, scope, Instant.now());
    }

    public AccessToken(String accessToken, String tokenType, long expiresInSeconds,
                       String refreshToken, String scope, Instant issuedAt) {
        this.accessToken = Objects.requireNonNull(accessToken, "accessToken");
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.expiresInSeconds = expiresInSeconds;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.issuedAt = issuedAt;
    }

    public String accessToken() { return accessToken; }
    public String tokenType() { return tokenType; }
    public long expiresInSeconds() { return expiresInSeconds; }
    public String refreshToken() { return refreshToken; }
    public String scope() { return scope; }
    public Instant issuedAt() { return issuedAt; }

    public Instant expiresAt() {
        return issuedAt.plusSeconds(expiresInSeconds);
    }

    /**
     * @return token şu anda geçerli mi (verilen güvenlik payı kadar erken expired sayılır)
     */
    public boolean isValid(Duration safetyMargin, Instant now) {
        return now.isBefore(expiresAt().minus(safetyMargin));
    }

    public String authorizationHeader() {
        return tokenType + " " + accessToken;
    }

    @Override
    public String toString() {
        return "AccessToken(type=" + tokenType + ", expiresAt=" + expiresAt() + ", access=***)";
    }
}

