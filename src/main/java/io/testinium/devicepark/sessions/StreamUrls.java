package io.testinium.devicepark.sessions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Joins a token-less screen-stream URL with the caller's current access token.
 */
public final class StreamUrls {

    private StreamUrls() {
    }

    public static String withAccessToken(String streamUrl, String accessToken) {
        if (streamUrl == null || streamUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("stream url cannot be null or empty");
        }
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalArgumentException("access token cannot be null or empty");
        }
        String separator = streamUrl.indexOf('?') >= 0 ? "&" : "?";
        return streamUrl + separator + "token=" + encode(accessToken);
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
