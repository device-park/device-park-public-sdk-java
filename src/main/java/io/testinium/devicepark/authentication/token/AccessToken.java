package io.testinium.devicepark.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Access token returned from the OAuth2 token endpoint. Compatible with snake_case JSON.
 *
 * <p>Standard OAuth2 fields: {@code access_token}, {@code token_type},
 * {@code expires_in}, {@code refresh_token}, {@code scope}.</p>
 */
public final class AccessToken {

    private final String accessToken;
    private final String tokenType;
    private final long expiresInSeconds;
    private final Instant issuedAt;

    public AccessToken(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") long expiresInSeconds) {
        this(accessToken, tokenType, expiresInSeconds, Instant.now());
    }

    public AccessToken(String accessToken, String tokenType, long expiresInSeconds,
                       Instant issuedAt) {
        this.accessToken = Objects.requireNonNull(accessToken, "accessToken");
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.expiresInSeconds = expiresInSeconds;
        this.issuedAt = issuedAt;
    }

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

