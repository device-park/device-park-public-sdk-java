package com.devicepark.sdk;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.config.SdkConfig;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.endpoint.StaticEndpointProvider;
import com.devicepark.sdk.core.http.ApacheHttp5Client;
import com.devicepark.sdk.core.http.AuthRefreshExecChainHandler;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.retry.RetryPolicy;
import com.devicepark.sdk.core.retry.RetryableHttpClient;
import com.devicepark.sdk.core.util.Suppliers;
import com.devicepark.sdk.management.DeviceParkManagementService;
import com.devicepark.sdk.management.devices.DevicesApi;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * New SDK entry point using {@link SdkConfig}.
 */
public final class ApiClient implements Closeable {

    private final SdkConfig config;
    private final ApacheHttp5Client authHttpClient;
    private final ApacheHttp5Client managementHttpClient;
    private final DeviceParkAuthClient authClient;
    private final EndpointProvider endpointProvider;
    private final Supplier<DeviceParkManagementService> managementService;

    public ApiClient(SdkConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config is required");
        }
        this.config = config;

        this.authHttpClient = ApacheHttp5Client.builder()
                .connectTimeout(config.connectTimeout())
                .responseTimeout(config.socketTimeout())
                .maxConnTotal(config.maxTotalConnections())
                .maxConnPerRoute(config.maxPerRoute())
                .build();

        this.endpointProvider = StaticEndpointProvider.create(config.baseUrl());
        EndpointProvider authEndpointProvider = StaticEndpointProvider.create(config.authEndpoint());

        this.authClient = DeviceParkAuthClient.builder()
                .httpClient(this.authHttpClient)
                .endpointProvider(authEndpointProvider)
                .credentials(config.clientId(), config.clientSecret())
                .scope(config.scope())
                .tokenSafetyMargin(Duration.ofSeconds(config.tokenRefreshBufferSeconds()))
                .retryPolicy(resolveRetryPolicy(config.maxRetries()))
                .build();

        this.managementHttpClient = ApacheHttp5Client.builder()
                .connectTimeout(config.connectTimeout())
                .responseTimeout(config.socketTimeout())
                .maxConnTotal(config.maxTotalConnections())
                .maxConnPerRoute(config.maxPerRoute())
                .addExecInterceptorLast("devicepark-auth-refresh", new AuthRefreshExecChainHandler(this.authClient))
                .build();

        this.managementService = Suppliers.memoize(() -> {
            SdkHttpClient retriedHttpClient = new RetryableHttpClient(
                    this.managementHttpClient,
                    resolveRetryPolicy(config.maxRetries()));
            return new DeviceParkManagementService(this.authClient, retriedHttpClient, this.endpointProvider, false);
        });
    }

    public SdkConfig config() {
        return config;
    }

    public DeviceParkManagementService management() {
        return managementService.get();
    }

    public DevicesApi devices() {
        return management().devices();
    }

    @Override
    public void close() throws IOException {
        this.authClient.close();
        this.managementHttpClient.close();
        this.authHttpClient.close();
    }

    private static RetryPolicy resolveRetryPolicy(int maxRetries) {
        int maxAttempts = Math.max(1, maxRetries + 1);
        return RetryPolicy.builder().maxAttempts(maxAttempts).build();
    }
}
