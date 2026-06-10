package com.devicepark.sdk.core.http;

import java.io.Closeable;

/**
 * SDK'nın HTTP transport soyutlaması. Implementasyonlar (örn. Apache HttpClient 5)
 * bu arayüzü gerçekler ve transport katmanı değiştirilebilir kalır.
 */
public interface SdkHttpClient extends Closeable {

    /**
     * Senkron istek gönderir.
     *
     * @throws com.devicepark.sdk.core.exception.SdkClientException IO/transport hataları için
     */
    SdkHttpResponse execute(SdkHttpRequest request);
}

