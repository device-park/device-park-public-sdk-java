package com.devicepark.sdk.management.internal;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.auth.exception.AuthenticationException;
import com.devicepark.sdk.core.endpoint.Endpoint;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.http.HttpMethod;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.http.SdkHttpRequest;
import com.devicepark.sdk.core.http.SdkHttpResponse;
import com.devicepark.sdk.management.exception.ManagementServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Management modülü içinde tüm sub-API'lerin (devices, sessions, storage, ...) paylaştığı
 * runtime. URL inşası, auth header enjeksiyonu, 401 retry, hata maplemesini yönetir.
 *
 * <p>Bu sınıf SDK kullanıcısına açık değildir — yalnızca
 * {@code com.devicepark.sdk.management} paketinden erişilir.</p>
 */
public final class ManagementApiRuntime {

    private static final Logger LOG = LoggerFactory.getLogger(ManagementApiRuntime.class);

    /** Tüm management endpointlerinin paylaştığı URL prefix'i. */
    public static final String API_PREFIX = "/management/api/v1/public";

    private final SdkHttpClient httpClient;
    private final DeviceParkAuthClient authClient;
    private final EndpointProvider endpointProvider;

    public ManagementApiRuntime(SdkHttpClient httpClient,
                                DeviceParkAuthClient authClient,
                                EndpointProvider endpointProvider) {
        this.httpClient = httpClient;
        this.authClient = authClient;
        this.endpointProvider = endpointProvider;
    }

    /**
     * Verilen path'e (API_PREFIX'ten sonra) GET isteği atar.
     *
     * @param path örn. {@code "/devices"}
     * @param queryParams sıralı query string için anahtar-değer map (null değerler atlanır)
     */
    public SdkHttpResponse get(String path, Map<String, ?> queryParams) {
        URI uri = buildUri(path, queryParams);
        SdkHttpRequest req = SdkHttpRequest.builder()
                .method(HttpMethod.GET)
                .uri(uri)
                .header("Accept", "application/json")
                .header("Authorization", authClient.authorizationHeader())
                .build();

        SdkHttpResponse resp = httpClient.execute(req);

        if (resp.statusCode() == 401) {
            LOG.debug("401 alındı, token refresh edilip retry yapılıyor: {}", uri);
            authClient.refreshToken();
            SdkHttpRequest retried = SdkHttpRequest.builder()
                    .method(HttpMethod.GET)
                    .uri(uri)
                    .header("Accept", "application/json")
                    .header("Authorization", authClient.authorizationHeader())
                    .build();
            resp = httpClient.execute(retried);
            if (resp.statusCode() == 401) {
                throw new AuthenticationException(
                        "Token yenilenmesine rağmen 401 alındı: " + resp.bodyAsString());
            }
        }

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
                if (e.getValue() == null) continue;
                if (qs.length() > 0) qs.append('&');
                qs.append(e.getKey()).append('=').append(urlEncode(e.getValue().toString()));
            }
        }
        String full = endpoint.uri() + API_PREFIX + path + (qs.length() > 0 ? "?" + qs : "");
        return URI.create(full);
    }

    private static String urlEncode(String value) {
        try {
            // Java 8 uyumlu: URLEncoder.encode(String, Charset) overload'ı JDK 10+ olduğu için isimle çağırıyoruz.
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            // UTF-8 her JVM'de zorunludur; ulaşılamaz.
            throw new IllegalStateException(e);
        }
    }
}

