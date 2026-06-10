package com.devicepark.sdk;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.endpoint.StaticEndpointProvider;
import com.devicepark.sdk.core.http.ApacheHttp5Client;
import com.devicepark.sdk.core.http.ClientOptions;
import com.devicepark.sdk.core.util.Suppliers;
import com.devicepark.sdk.management.DeviceParkManagementService;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

public class DeviceParkApiClient implements Closeable {
    protected final ClientOptions clientOptions;

    protected final DeviceParkAuthClient authClient;

    protected final ApacheHttp5Client httpClient;

    protected final EndpointProvider endpointProvider;

    protected final Supplier<DeviceParkManagementService> managementService;

    public DeviceParkApiClient(ClientOptions clientOptions) {
        this.clientOptions = clientOptions;
        this.httpClient = clientOptions.httpClient();
        this.endpointProvider = StaticEndpointProvider.create(clientOptions.environment().getUrl());

        this.authClient = clientOptions.credentialsProvider()
                .map(provider -> DeviceParkAuthClient.builder()
                        .httpClient(this.httpClient)
                        .endpointProvider(this.endpointProvider)
                        .credentialsProvider(provider)
                        .build())
                .orElseThrow(() -> new IllegalStateException("Management service requires credentials(...) configuration"));

        this.managementService = Suppliers.memoize(() -> new DeviceParkManagementService(
                this.authClient,
                this.httpClient,
                this.endpointProvider));
    }

    public DeviceParkManagementService managementService() {
        return this.managementService.get();
    }

    public DeviceParkManagementService management() {
        return this.managementService();
    }

    public static DeviceParkApiClientBuilder builder() {
        return new DeviceParkApiClientBuilder();
    }

    @Override
    public void close() throws IOException {
        this.authClient.close();
        this.httpClient.close();
    }
}