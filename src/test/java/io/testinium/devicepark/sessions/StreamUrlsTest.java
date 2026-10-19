package io.testinium.devicepark.sessions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamUrlsTest {

    @Test
    void appendsTokenAsQueryParameter() {
        String url = StreamUrls.withAccessToken(
                "wss://stream.example/ws/v2/devicepark/sessions/12345/stream",
                "abc.def");

        assertEquals(
                "wss://stream.example/ws/v2/devicepark/sessions/12345/stream?token=abc.def",
                url);
    }

    @Test
    void encodesTokenAndKeepsExistingQuery() {
        String url = StreamUrls.withAccessToken(
                "wss://stream.example/stream?session=1",
                "a+b/c=");

        assertEquals("wss://stream.example/stream?session=1&token=a%2Bb%2Fc%3D", url);
    }

    @Test
    void rejectsBlankUrl() {
        assertThrows(IllegalArgumentException.class, () -> StreamUrls.withAccessToken("  ", "token"));
    }
}
