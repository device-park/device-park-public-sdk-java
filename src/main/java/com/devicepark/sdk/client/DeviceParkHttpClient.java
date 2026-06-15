package com.devicepark.sdk.client;

import com.devicepark.sdk.authentication.credentials.Credentials;
import com.devicepark.sdk.authentication.token.AccessToken;
import com.devicepark.sdk.core.AbstractApiService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class DeviceParkHttpClient extends AbstractApiService implements Closeable {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String baseUrl;
    private final CloseableHttpClient httpClient;
    private final Credentials credentials;
    private AccessToken cachedToken;

    public DeviceParkHttpClient(String baseUrl, Integer timeoutSeconds, Credentials credentials) {
        this.baseUrl = baseUrl;
        this.credentials = credentials;
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(timeoutSeconds != null ? timeoutSeconds : 20))
                .setConnectionRequestTimeout(Timeout.ofSeconds(timeoutSeconds != null ? timeoutSeconds : 20))
                .build();

        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public String get(String path) {
        try {
            HttpGet request = new HttpGet(buildUrl(path));
            addHeaders(request, null);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    public String get(String path, Map<String, ?> queryParams) {
        try {
            HttpGet request = new HttpGet(buildUri(baseUrl, path, queryParams));
            addHeaders(request, null);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    public String get(String path, Map<String, ?> queryParams, Map<String, String> headers) {
        try {
            HttpGet request = new HttpGet(buildUri(baseUrl, path, queryParams));
            addHeaders(request, headers);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    public String post(String path, String body, Map<String, String> headers) {
        try {
            HttpPost request = new HttpPost(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute POST request for path: " + path, e);
        }
    }

    public String put(String path, String body, Map<String, String> headers) {
        try {
            HttpPut request = new HttpPut(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute PUT request for path: " + path, e);
        }
    }

    public String delete(String path, Map<String, String> headers) {
        try {
            HttpDelete request = new HttpDelete(buildUrl(path));
            addHeaders(request, headers);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute DELETE request for path: " + path, e);
        }
    }

    private String execute(ClassicHttpRequest request) throws IOException {
        return httpClient.execute(request, response -> {
            int status = response.getCode();
            String responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
            if (status >= 200 && status < 300) {
                return responseBody;
            } else {
                throw new IOException("HTTP Request Failed with status: " + status + " body: " + responseBody);
            }
        });
    }

    private String buildUrl(String path) {
        if (path == null) {
            return baseUrl;
        }
        if (baseUrl.endsWith("/") && path.startsWith("/")) {
            return baseUrl + path.substring(1);
        } else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            return baseUrl + "/" + path;
        }
        return baseUrl + path;
    }

    private synchronized AccessToken getValidAccessToken() throws IOException {
        if (cachedToken == null || !cachedToken.isValid(java.time.Duration.ofSeconds(60), java.time.Instant.now())) {
            cachedToken = fetchAccessToken();
        }
        return cachedToken;
    }

    private AccessToken fetchAccessToken() throws IOException {
        HttpPost request = new HttpPost(this.baseUrl + "/uaa/oauth2/token");
        String basicAuth = Base64.getEncoder().encodeToString(
                (credentials.getClientId() + ":" + credentials.getClientSecret()).getBytes(StandardCharsets.UTF_8));
        request.addHeader("Authorization", "Basic " + basicAuth);
        String formParams = "grant_type=client_credentials&scope=openid";
        request.setEntity(new StringEntity(formParams, ContentType.APPLICATION_FORM_URLENCODED));
        String responseBody = execute(request);
        return MAPPER.readValue(responseBody, AccessToken.class);
    }

    private void addHeaders(ClassicHttpRequest request, Map<String, String> headers) throws IOException {
        request.addHeader("Authorization", getValidAccessToken().authorizationHeader());
        Optional.ofNullable(headers).ifPresent(map -> map.forEach(request::addHeader));
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
