package com.devicepark.sdk.core;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractApiService {

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public URI buildUri(String baseurl, String path, Map<String, ?> queryParams) {
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
        String full = baseurl + path + (qs.length() > 0 ? "?" + qs : "");
        return URI.create(full);
    }
}
