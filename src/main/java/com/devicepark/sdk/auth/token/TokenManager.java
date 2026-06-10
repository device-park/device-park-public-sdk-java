package com.devicepark.sdk.auth.token;

/**
 * Geçerli access token'ı sağlar. Cache + auto-refresh implementasyon detayıdır.
 */
public interface TokenManager {

    /** @return geçerli (gerekirse refresh edilmiş) access token */
    AccessToken getToken();

    /** Cache'i geçersiz kılar ve token'ı zorla yeniler. */
    AccessToken refresh();
}

