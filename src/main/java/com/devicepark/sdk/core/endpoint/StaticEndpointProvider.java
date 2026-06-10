package com.devicepark.sdk.core.endpoint;

import com.devicepark.sdk.core.util.Validate;

/**
 * Kod ile verilen sabit endpoint'i döndüren provider.
 *
 * <pre>
 * var provider = StaticEndpointProvider.create("https://gateway.devicepark.io");
 * </pre>
 */
public final class StaticEndpointProvider implements EndpointProvider {

    private final Endpoint endpoint;

    private StaticEndpointProvider(Endpoint endpoint) {
        this.endpoint = Validate.notNull(endpoint, "endpoint");
    }

    public static StaticEndpointProvider create(Endpoint endpoint) {
        return new StaticEndpointProvider(endpoint);
    }

    public static StaticEndpointProvider create(String uri) {
        return new StaticEndpointProvider(Endpoint.of(uri));
    }

    @Override
    public Endpoint resolveEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "StaticEndpointProvider(" + endpoint + ")";
    }
}

