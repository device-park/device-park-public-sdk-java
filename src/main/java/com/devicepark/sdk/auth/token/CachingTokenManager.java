package com.devicepark.sdk.auth.token;

import com.devicepark.sdk.core.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;

/**
 * Bir {@link OAuth2TokenClient} üzerinde caching + lazy refresh sağlar.
 *
 * <p>Thread-safe: token expire olmadan ({@link #safetyMargin}) önce yenilenir.
 * AWS SDK'daki {@code CredentialsProvider} ile {@code refreshing token} mantığının muadilidir.</p>
 */
public final class CachingTokenManager implements TokenManager {

    private static final Logger LOG = LoggerFactory.getLogger(CachingTokenManager.class);

    private final OAuth2TokenClient tokenClient;
    private final Duration safetyMargin;
    private final Clock clock;

    private volatile AccessToken cached;

    private CachingTokenManager(Builder b) {
        this.tokenClient = Validate.notNull(b.tokenClient, "tokenClient");
        this.safetyMargin = b.safetyMargin;
        this.clock = b.clock;
    }

    public static Builder builder() { return new Builder(); }

    @Override
    public AccessToken getToken() {
        AccessToken local = cached;
        if (local != null && local.isValid(safetyMargin, clock.instant())) {
            return local;
        }
        return refresh();
    }

    @Override
    public synchronized AccessToken refresh() {
        // double-check
        AccessToken local = cached;
        if (local != null && local.isValid(safetyMargin, clock.instant())) {
            return local;
        }
        LOG.debug("Token yenileniyor");
        AccessToken fresh = tokenClient.requestToken();
        this.cached = fresh;
        return fresh;
    }

    public static final class Builder {
        private OAuth2TokenClient tokenClient;
        private Duration safetyMargin = Duration.ofSeconds(30);
        private Clock clock = Clock.systemUTC();

        public Builder tokenClient(OAuth2TokenClient v) { this.tokenClient = v; return this; }
        public Builder safetyMargin(Duration v) { this.safetyMargin = v; return this; }
        public Builder clock(Clock v) { this.clock = v; return this; }

        public CachingTokenManager build() { return new CachingTokenManager(this); }
    }
}

