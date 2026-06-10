package com.devicepark.sdk.core.endpoint;

import com.devicepark.sdk.core.util.Validate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Device Park gateway / API endpoint'ini temsil eden immutable value object.
 *
 * <p>AWS SDK'daki endpoint kavramının muadilidir. SDK içinde token alma ve
 * sonraki API çağrıları bu endpoint üzerine bina edilir.</p>
 */
public final class Endpoint {

    private final URI uri;

    private Endpoint(URI uri) {
        this.uri = Validate.notNull(uri, "uri");
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalArgumentException(
                    "Endpoint URI absolute olmalı (scheme + host içermeli): " + uri);
        }
        if (!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException(
                    "Endpoint scheme yalnızca http veya https olabilir: " + uri.getScheme());
        }
    }

    public static Endpoint of(URI uri) {
        return new Endpoint(normalize(uri));
    }

    public static Endpoint of(String uri) {
        Validate.notBlank(uri, "uri");
        try {
            return of(new URI(uri));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Geçersiz endpoint URI: " + uri, e);
        }
    }

    /** Sondaki '/' karakterini kaldırarak normalize eder. */
    private static URI normalize(URI uri) {
        String s = uri.toString();
        if (s.endsWith("/")) {
            return URI.create(s.substring(0, s.length() - 1));
        }
        return uri;
    }

    public URI uri() {
        return uri;
    }

    /**
     * Bu endpoint'e göreli (relative) bir path için tam URI üretir.
     * Örn. {@code resolve("/oauth/token")}.
     */
    public URI resolve(String path) {
        Validate.notBlank(path, "path");
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        return URI.create(uri + normalizedPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;
        Endpoint that = (Endpoint) o;
        return uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }

    @Override
    public String toString() {
        return "Endpoint(" + uri + ")";
    }
}

