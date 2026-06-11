package com.devicepark.sdk.config;

import com.devicepark.sdk.core.log.LogLevel;

import javax.net.ssl.SSLContext;
import java.time.Duration;

/**
 * Immutable configuration object for the new SDK entry point.
 */
public final class SdkConfig {

    private final String clientId;
    private final String clientSecret;
    private final String authEndpoint;
    private final String scope;

    private final String baseUrl;
    private final Duration connectTimeout;
    private final Duration socketTimeout;
    private final int maxRetries;
    private final int maxTotalConnections;
    private final int maxPerRoute;

    private final int tokenRefreshBufferSeconds;
    private final Duration tokenCacheDuration;

    private final boolean trustAllCertificates;
    private final SSLContext customSslContext;
    private final String certificatePinning;

    private final LogLevel loggingLevel;
    private final boolean requestLogging;
    private final boolean responseLogging;
    private final boolean sensitiveDataMasking;

    SdkConfig(String clientId,
              String clientSecret,
              String authEndpoint,
              String scope,
              String baseUrl,
              Duration connectTimeout,
              Duration socketTimeout,
              int maxRetries,
              int maxTotalConnections,
              int maxPerRoute,
              int tokenRefreshBufferSeconds,
              Duration tokenCacheDuration,
              boolean trustAllCertificates,
              SSLContext customSslContext,
              String certificatePinning,
              LogLevel loggingLevel,
              boolean requestLogging,
              boolean responseLogging,
              boolean sensitiveDataMasking) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authEndpoint = authEndpoint;
        this.scope = scope;
        this.baseUrl = baseUrl;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.maxRetries = maxRetries;
        this.maxTotalConnections = maxTotalConnections;
        this.maxPerRoute = maxPerRoute;
        this.tokenRefreshBufferSeconds = tokenRefreshBufferSeconds;
        this.tokenCacheDuration = tokenCacheDuration;
        this.trustAllCertificates = trustAllCertificates;
        this.customSslContext = customSslContext;
        this.certificatePinning = certificatePinning;
        this.loggingLevel = loggingLevel;
        this.requestLogging = requestLogging;
        this.responseLogging = responseLogging;
        this.sensitiveDataMasking = sensitiveDataMasking;
    }

    public String clientId() {
        return clientId;
    }

    public String clientSecret() {
        return clientSecret;
    }

    public String authEndpoint() {
        return authEndpoint;
    }

    public String scope() {
        return scope;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    public Duration socketTimeout() {
        return socketTimeout;
    }

    public int maxRetries() {
        return maxRetries;
    }

    public int maxTotalConnections() {
        return maxTotalConnections;
    }

    public int maxPerRoute() {
        return maxPerRoute;
    }

    public int tokenRefreshBufferSeconds() {
        return tokenRefreshBufferSeconds;
    }

    public Duration tokenCacheDuration() {
        return tokenCacheDuration;
    }

    public boolean trustAllCertificates() {
        return trustAllCertificates;
    }

    public SSLContext customSslContext() {
        return customSslContext;
    }

    public String certificatePinning() {
        return certificatePinning;
    }

    public LogLevel loggingLevel() {
        return loggingLevel;
    }

    public boolean requestLogging() {
        return requestLogging;
    }

    public boolean responseLogging() {
        return responseLogging;
    }

    public boolean sensitiveDataMasking() {
        return sensitiveDataMasking;
    }
}

