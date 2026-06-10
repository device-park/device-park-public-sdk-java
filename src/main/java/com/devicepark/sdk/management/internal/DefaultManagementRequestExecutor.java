package com.devicepark.sdk.management.internal;

import com.devicepark.sdk.core.endpoint.Endpoint;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.http.HttpMethod;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.http.SdkHttpRequest;
import com.devicepark.sdk.core.http.SdkHttpResponse;
import com.devicepark.sdk.management.exception.ManagementServiceException;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default implementation that centralizes URI building and error mapping for Management APIs.
 */
public final class DefaultManagementRequestExecutor implements ManagementRequestExecutor {

    /** Shared path prefix for management endpoints. */
    public static final String API_PREFIX = "/management/api/v1/public";

    private final SdkHttpClient httpClient;
    private final EndpointProvider endpointProvider;

    public DefaultManagementRequestExecutor(SdkHttpClient httpClient,
                                            EndpointProvider endpointProvider) {
        this.httpClient = httpClient;
        this.endpointProvider = endpointProvider;
    }

    @Override
    public SdkHttpResponse get(String path, Map<String, ?> queryParams) {
        URI uri = buildUri(path, queryParams);
        SdkHttpRequest req = SdkHttpRequest.builder()
                .method(HttpMethod.GET)
                .uri(uri)
                .header("Accept", "application/json")
                .build();

        SdkHttpResponse resp = httpClient.execute(req);
        if (!resp.isSuccessful()) {
            throw new ManagementServiceException(resp.statusCode(),
                    "Management API hatası (HTTP " + resp.statusCode() + "): " + resp.bodyAsString());
        }
        return resp;
    }

    private URI buildUri(String path, Map<String, ?> queryParams) {
        Endpoint endpoint = endpointProvider.resolveEndpoint();
        StringBuilder qs = new StringBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, ?> e : new LinkedHashMap<>(queryParams).entrySet()) {
                if (e.getValue() == null) {
                    continue;
                }
                if (qs.length() > 0) {
                    qs.append('&');
                }
                qs.append(e.getKey()).append('=').append(urlEncode(e.getValue().toString()));
            }
        }
        String full = endpoint.uri() + API_PREFIX + path + (qs.length() > 0 ? "?" + qs : "");
        return URI.create(full);
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}

