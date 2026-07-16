package io.testinium.devicepark.core;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract base class providing common helpers for API services.
 *
 * <p>Currently provides only query string building and URI concatenation helpers.
 * New shared behaviors can be added here.</p>
 *
 * <p>This class is an internal part of the SDK.</p>
 *
 * @since 1.0.0
 */
public abstract class AbstractApiService {

    /**
     * Encodes to {@code application/x-www-form-urlencoded} format using UTF-8.
     *
     * @param value raw value to be encoded
     * @return URL-encoded value
     */
    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Builds a complete {@link URI} from the given base URL, path, and query parameters.
     *
     * <p>Query parameters with {@code null} values are skipped. Keys and values
     * are UTF-8 URL-encoded. Parameter order follows the {@link Map} iteration
     * order (hence {@link LinkedHashMap} is recommended).</p>
     *
     * @param baseurl     scheme + host + optional port
     * @param path        endpoint path (may or may not start with {@code /})
     * @param queryParams query string key-value pairs; may be {@code null}
     * @return combined full URI
     */
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
                qs.append(urlEncode(e.getKey())).append('=').append(urlEncode(e.getValue().toString()));
            }
        }
        String full = baseurl + path + (qs.length() > 0 ? "?" + qs : "");
        return URI.create(full);
    }
}
