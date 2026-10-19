package io.testinium.devicepark.model.sessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Screen-stream WebSocket address returned by session-api, without an access token.
 */
public final class StreamUrl {

    private final String url;

    @JsonCreator
    public StreamUrl(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
