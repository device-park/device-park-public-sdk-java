package com.devicepark.sdk.core.http;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.auth.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Auth-aware HTTP decorator for management traffic.
 *
 * <p>Interception behavior:
 * <ul>
 *   <li>Adds {@code Authorization: Bearer ...} to outgoing requests</li>
 *   <li>On HTTP 401, refreshes token and retries once</li>
 * </ul>
 * </p>
 */
public final class AuthRefreshingHttpClient implements SdkHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuthRefreshingHttpClient.class);

    private final SdkHttpClient delegate;
    private final DeviceParkAuthClient authClient;

    public AuthRefreshingHttpClient(SdkHttpClient delegate, DeviceParkAuthClient authClient) {
        this.delegate = delegate;
        this.authClient = authClient;
    }

    @Override
    public SdkHttpResponse execute(SdkHttpRequest request) {
        SdkHttpResponse response = delegate.execute(withAuthorization(request, authClient.authorizationHeader()));
        if (response.statusCode() != 401) {
            return response;
        }

        LOG.debug("401 received, refreshing token and retrying once: {}", request.uri());
        authClient.refreshToken();

        SdkHttpResponse retried = delegate.execute(withAuthorization(request, authClient.authorizationHeader()));
        if (retried.statusCode() == 401) {
            throw new AuthenticationException(
                    "Received HTTP 401 even after token refresh: " + retried.bodyAsString());
        }
        return retried;
    }

    private static SdkHttpRequest withAuthorization(SdkHttpRequest original, String authorizationHeader) {
        SdkHttpRequest.Builder builder = SdkHttpRequest.builder()
                .method(original.method())
                .uri(original.uri());

        for (Map.Entry<String, String> header : original.headers().entrySet()) {
            builder.header(header.getKey(), header.getValue());
        }
        builder.header("Authorization", authorizationHeader);

        if (original.body() != null) {
            builder.body(original.body(), original.contentType());
        }
        return builder.build();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}

