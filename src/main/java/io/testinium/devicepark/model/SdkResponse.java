package io.testinium.devicepark.model;

import org.apache.hc.core5.http.HttpStatus;

/**
 * Genel amaçlı SDK yanıtı zarfı (envelope).
 *
 * <p>Şu an iç (internal) kullanım için yer tutucu olarak bulunur; ileride
 * standart hata/başarı yanıtı taşımak için genişletilebilir.</p>
 *
 * @since 1.0.0
 */
public class SdkResponse {
    private final HttpStatus status;
    private final String message;

    public SdkResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
