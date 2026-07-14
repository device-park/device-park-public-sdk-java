package io.testinium.devicepark.core;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API servisleri için ortak yardımcılar sağlayan soyut taban sınıf.
 *
 * <p>Şu an yalnızca query string oluşturma ve URI birleştirme yardımcısı
 * sağlar. Yeni paylaşılan davranışlar buraya eklenebilir.</p>
 *
 * <p>Bu sınıf SDK'nın iç (internal) parçasıdır.</p>
 *
 * @since 1.0.0
 */
public abstract class AbstractApiService {

    /**
     * UTF-8 ile {@code application/x-www-form-urlencoded} formatına encode eder.
     *
     * @param value encode edilecek ham değer
     * @return URL-encoded değer
     */
    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Verilen base URL, path ve query parametrelerinden tam {@link URI} oluşturur.
     *
     * <p>{@code null} değerli query parametreleri atlanır. Anahtar/değerler
     * UTF-8 URL-encode edilir. Parametre sırası, sağlanan {@link Map} iterasyon
     * sırasını korur (bu yüzden {@link LinkedHashMap} önerilir).</p>
     *
     * @param baseurl     scheme + host + opsiyonel port
     * @param path        endpoint path'i (başında {@code /} olabilir/olmayabilir)
     * @param queryParams query string anahtar-değer çiftleri; {@code null} olabilir
     * @return birleştirilmiş tam URI
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
