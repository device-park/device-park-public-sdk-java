package com.devicepark.sdk.core.http;

import com.devicepark.sdk.core.util.Validate;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Apache HttpClient 5 request interceptor that injects Authorization header.
 */
public final class ApacheBearerAuthRequestInterceptor implements HttpRequestInterceptor {

    private final Supplier<String> authorizationHeaderSupplier;

    public ApacheBearerAuthRequestInterceptor(Supplier<String> authorizationHeaderSupplier) {
        this.authorizationHeaderSupplier = Validate.notNull(authorizationHeaderSupplier, "authorizationHeaderSupplier");
    }

    @Override
    public void process(HttpRequest request, EntityDetails entity, HttpContext context)
            throws HttpException, IOException {
        if (request.containsHeader("Authorization")) {
            return;
        }
        String authorizationHeader = authorizationHeaderSupplier.get();
        if (authorizationHeader != null && !authorizationHeader.trim().isEmpty()) {
            request.setHeader("Authorization", authorizationHeader);
        }
    }
}

