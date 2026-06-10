package com.devicepark.sdk.core.http;

import com.devicepark.sdk.core.exception.SdkClientException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link SdkHttpClient}'ın Apache HttpClient 5 implementasyonu.
 */
public final class ApacheHttp5Client implements SdkHttpClient {

    private final CloseableHttpClient httpClient;

    private ApacheHttp5Client(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static ApacheHttp5Client create() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public SdkHttpResponse execute(SdkHttpRequest request) {
        HttpUriRequestBase apacheReq = new HttpUriRequestBase(request.method().name(), request.uri());

        request.headers().forEach(apacheReq::setHeader);

        if (request.body() != null) {
            ContentType ct = request.contentType() != null
                    ? ContentType.parse(request.contentType())
                    : ContentType.APPLICATION_OCTET_STREAM;
            apacheReq.setEntity(new ByteArrayEntity(request.body(), ct));
        }

        try {
            return httpClient.execute(apacheReq, response -> {
                Map<String, String> headers = new LinkedHashMap<>();
                for (Header h : response.getHeaders()) {
                    headers.put(h.getName(), h.getValue());
                }
                byte[] body = response.getEntity() != null
                        ? EntityUtils.toByteArray(response.getEntity())
                        : new byte[0];
                return new SdkHttpResponse(response.getCode(), headers, body);
            });
        } catch (IOException e) {
            throw new SdkClientException("HTTP isteği başarısız: " + request.uri(), e);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    public static final class Builder {
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration responseTimeout = Duration.ofSeconds(30);
        private int maxConnTotal = 50;
        private int maxConnPerRoute = 20;
        private final List<HttpRequestInterceptor> requestInterceptors = new ArrayList<>();

        public Builder connectTimeout(Duration d) { this.connectTimeout = d; return this; }
        public Builder responseTimeout(Duration d) { this.responseTimeout = d; return this; }
        public Builder maxConnTotal(int v) { this.maxConnTotal = v; return this; }
        public Builder maxConnPerRoute(int v) { this.maxConnPerRoute = v; return this; }
        public Builder addRequestInterceptorLast(HttpRequestInterceptor interceptor) {
            this.requestInterceptors.add(interceptor);
            return this;
        }

        public ApacheHttp5Client build() {
            PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                    .setMaxConnTotal(maxConnTotal)
                    .setMaxConnPerRoute(maxConnPerRoute)
                    .build();

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout.toMillis()))
                    .setResponseTimeout(Timeout.ofMilliseconds(responseTimeout.toMillis()))
                    .build();

            HttpClientBuilder httpBuilder = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig);

            for (HttpRequestInterceptor interceptor : requestInterceptors) {
                httpBuilder.addRequestInterceptorLast(interceptor);
            }

            CloseableHttpClient client = httpBuilder.build();

            return new ApacheHttp5Client(client);
        }
    }
}

