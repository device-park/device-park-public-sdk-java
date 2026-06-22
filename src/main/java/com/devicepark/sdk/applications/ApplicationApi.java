package com.devicepark.sdk.applications;

import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.model.applications.Application;
import com.devicepark.sdk.model.applications.ApplicationPaginationRequest;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.common.Sorting;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mobil uygulama (APK/IPA) yönetimi için API servisi.
 *
 * <p>Yüklenen uygulamalar testler sırasında cihazlara kurulabilir. Aynı
 * {@code fileKey} altında birden fazla revizyon (version) saklanabilir.</p>
 *
 * <p>Bu servis instaönce'larını {@link
 * com.devicepark.sdk.DeviceParkApiClient#applications()} ile elde edin.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /storage/api/v1/public/applications}</p>
 *
 * @since 1.0.0
 */
public final class ApplicationApi {

    private static final String APPLICATION_PATH = "/storage/api/v1/public/applications";

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Yeni bir {@code ApplicationApi} oluşturur.
     *
     * <p>Genellikle doğrudan çağrılmaz; {@link
     * com.devicepark.sdk.DeviceParkApiClient#applications()} kullanın.</p>
     *
     * @param deviceParkHttpClient paylaşılan HTTP istemcisi
     */
    public ApplicationApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Yetkili istemciye ait uygulamaları sayfalı olarak listeler.
     *
     * @param request sayfalama/sıralama parametreleri; {@code null} verilirse
     *                varsayılan değerler kullanılır
     * @return tek sayfalık {@link Application} listesi
     */
    public PageDto<Application> list(ApplicationPaginationRequest request) {
        ApplicationPaginationRequest req = request != null ? request : ApplicationPaginationRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get(APPLICATION_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Application>>() {
        });
    }

    /**
     * {@code application/octet-stream} ile {@link InputStream} üzerinden dosya
     * yükler.
     *
     * <p>Stream'in kapatılması çağıran tarafın sorumluluğundadır
     * (try-with-resources önerilir).</p>
     *
     * <h3>Öörnek</h3>
     * <pre>
     * try (InputStream in = Files.newInputStream(Paths.get("/path/app.apk"))) {
     *     Application app = client.applications().upload(in, "app.apk", "1.0.0");
     *     System.out.println(app.fileKey());
     * }
     * </pre>
     *
     * @param fileStream dosya içeriği (zorunlu)
     * @param fileName   sunucuda kullanılacak dosya adı; {@code file-name} header
     *                   olarak iletilir (zorunlu)
     * @param version    uygulama versiyonu, en fazla 50 karakter (zorunlu)
     * @return yüklenen uygulamanın metadata'sı
     * @throws IllegalArgumentException herhangi bir parametre boş/null ise veya
     *                                  {@code version} 50 karakteri aşarsa
     */
    public Application upload(InputStream fileStream, String fileName, String version) {
        if (fileStream == null) {
            throw new IllegalArgumentException("fileStream cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("fileName cannot be null or empty");
        }
        validateVersion(version);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("file-name", fileName);
        headers.put("version", version);

        String response = deviceParkHttpClient.postStream(APPLICATION_PATH, fileStream, headers);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Application>() {
        });
    }

    /**
     * Verilen {@code fileKey}'e ait uygulamayı (ve tüm revizyonlarını) siler.
     *
     * @param fileKey silinecek uygulamanın anahtarı
     * @throws IllegalArgumentException {@code fileKey} boş/null ise
     */
    public void delete(String fileKey) {
        if (fileKey == null || fileKey.trim().isEmpty()) {
            throw new IllegalArgumentException("fileKey cannot be null or empty");
        }
        deviceParkHttpClient.delete(APPLICATION_PATH + "/" + fileKey, null);
    }

    private static void validateVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("version cannot be null or empty");
        }
        if (version.length() > 50) {
            throw new IllegalArgumentException("version length must be at most 50 characters");
        }
    }
}
