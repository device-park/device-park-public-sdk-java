package com.devicepark.sdk;

import com.devicepark.sdk.authentication.credentials.Credentials;
import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.util.Suppliers;
import com.devicepark.sdk.management.devices.DevicesApi;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

public class DeviceParkApiClient implements Closeable {
    protected final DeviceParkHttpClient httpClient;
    protected final Supplier<DevicesApi> devices;

    public DeviceParkApiClient(String uri, Integer timeout, Credentials credentials) {
        this.httpClient = new DeviceParkHttpClient(uri, timeout, credentials);
        this.devices = Suppliers.memoize(() -> new DevicesApi(this.httpClient));
    }

    public static DeviceParkApiClientBuilder builder() {
        return new DeviceParkApiClientBuilder();
    }

    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}

