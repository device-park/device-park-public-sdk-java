package com.devicepark.sdk.config;

import com.devicepark.sdk.core.log.LogLevel;

import javax.net.ssl.SSLContext;
import java.time.Duration;

/**
 * Fluent builder for {@link SdkConfig}.
 */
public final class SdkConfigBuilder {

    private String clientId;
    private String clientSecret;
    private String authEndpoint;
    private String scope = "openid";

    private String baseUrl;
    private Duration connectTimeout = Duration.ofSeconds(10);
    private Duration socketTimeout = Duration.ofSeconds(30);
    private int maxRetries = 2;
    private int maxTotalConnections = 50;
    private int maxPerRoute = 20;

    private int tokenRefreshBufferSeconds = 30;
    private Duration tokenCacheDuration = Duration.ofHours(1);

    private boolean trustAllCertificates = false;
    private SSLContext customSslContext;
    private String certificatePinning;

    private LogLevel loggingLevel = LogLevel.INFO;
    private boolean requestLogging = false;
    private boolean responseLogging = false;
    private boolean sensitiveDataMasking = true;

    public SdkConfigBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public SdkConfigBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public SdkConfigBuilder withAuthEndpoint(String authEndpoint) {
        this.authEndpoint = authEndpoint;
        return this;
    }

    public SdkConfigBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public SdkConfigBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public SdkConfigBuilder withConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public SdkConfigBuilder withSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public SdkConfigBuilder withMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public SdkConfigBuilder withMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public SdkConfigBuilder withMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    public SdkConfigBuilder withTokenRefreshBuffer(int seconds) {
        this.tokenRefreshBufferSeconds = seconds;
        return this;
    }

    public SdkConfigBuilder withTokenCacheDuration(Duration tokenCacheDuration) {
        this.tokenCacheDuration = tokenCacheDuration;
        return this;
    }

    public SdkConfigBuilder withTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
        return this;
    }

    public SdkConfigBuilder withCustomSSLContext(SSLContext customSslContext) {
        this.customSslContext = customSslContext;
        return this;
    }

    public SdkConfigBuilder withCertificatePinning(String certificatePinning) {
        this.certificatePinning = certificatePinning;
        return this;
    }

    public SdkConfigBuilder withLoggingLevel(LogLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
        return this;
    }

    public SdkConfigBuilder withRequestLogging(boolean requestLogging) {
        this.requestLogging = requestLogging;
        return this;
    }

    public SdkConfigBuilder withResponseLogging(boolean responseLogging) {
        this.responseLogging = responseLogging;
        return this;
    }

    public SdkConfigBuilder withSensitiveDataMasking(boolean sensitiveDataMasking) {
        this.sensitiveDataMasking = sensitiveDataMasking;
        return this;
    }

    public SdkConfig build() {
        validate();
        return new SdkConfig(
                clientId,
                clientSecret,
                authEndpoint,
                scope,
                baseUrl,
                connectTimeout,
                socketTimeout,
                maxRetries,
                maxTotalConnections,
                maxPerRoute,
                tokenRefreshBufferSeconds,
                tokenCacheDuration,
                trustAllCertificates,
                customSslContext,
                certificatePinning,
                loggingLevel,
                requestLogging,
                responseLogging,
                sensitiveDataMasking
        );
    }

    private void validate() {
        if (isBlank(clientId)) {
            throw new IllegalStateException("withClientId(...) is required");
        }
        if (isBlank(clientSecret)) {
            throw new IllegalStateException("withClientSecret(...) is required");
        }
        if (isBlank(authEndpoint)) {
            throw new IllegalStateException("withAuthEndpoint(...) is required");
        }
        if (isBlank(baseUrl)) {
            throw new IllegalStateException("withBaseUrl(...) is required");
        }
        if (connectTimeout == null || socketTimeout == null || tokenCacheDuration == null) {
            throw new IllegalStateException("Duration fields cannot be null");
        }
        if (maxRetries < 0 || maxTotalConnections <= 0 || maxPerRoute <= 0 || tokenRefreshBufferSeconds < 0) {
            throw new IllegalStateException("Numeric configuration values are out of range");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

