package com.devicepark.sdk.core.retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Basit exponential backoff + jitter retry policy konfigürasyonu.
 */
public final class RetryPolicy {

    private final int maxAttempts;
    private final Duration baseDelay;
    private final Duration maxDelay;
    private final Set<Integer> retryableStatusCodes;

    private RetryPolicy(Builder b) {
        this.maxAttempts = b.maxAttempts;
        this.baseDelay = b.baseDelay;
        this.maxDelay = b.maxDelay;
        // Java 8 compatible immutable snapshot
        this.retryableStatusCodes = Collections.unmodifiableSet(new HashSet<>(b.retryableStatusCodes));
    }

    public int maxAttempts() { return maxAttempts; }
    public Duration baseDelay() { return baseDelay; }
    public Duration maxDelay() { return maxDelay; }
    public Set<Integer> retryableStatusCodes() { return retryableStatusCodes; }

    public boolean isRetryableStatus(int status) {
        return retryableStatusCodes.contains(status);
    }

    /** Sensible default: 3 deneme, 100ms base, 2s max, 408/429/500/502/503/504 retry. */
    public static RetryPolicy defaults() {
        return builder().build();
    }

    public static RetryPolicy none() {
        return builder().maxAttempts(1).build();
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private int maxAttempts = 3;
        private Duration baseDelay = Duration.ofMillis(100);
        private Duration maxDelay = Duration.ofSeconds(2);
        private Set<Integer> retryableStatusCodes = new HashSet<>(Arrays.asList(408, 429, 500, 502, 503, 504));

        public Builder maxAttempts(int v) { this.maxAttempts = v; return this; }
        public Builder baseDelay(Duration v) { this.baseDelay = v; return this; }
        public Builder maxDelay(Duration v) { this.maxDelay = v; return this; }
        public Builder retryableStatusCodes(Set<Integer> v) { this.retryableStatusCodes = v; return this; }

        public RetryPolicy build() { return new RetryPolicy(this); }
    }
}
