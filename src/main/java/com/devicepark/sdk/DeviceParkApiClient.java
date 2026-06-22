package com.devicepark.sdk;

import com.devicepark.sdk.allocation.AllocationApi;
import com.devicepark.sdk.applications.ApplicationApi;
import com.devicepark.sdk.authentication.credentials.Credentials;
import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.util.Suppliers;
import com.devicepark.sdk.management.devices.DevicesApi;
import com.devicepark.sdk.management.pools.PoolsApi;
import com.devicepark.sdk.sessions.SessionApi;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Device Park Public SDK için ana giriş noktası (entry point).
 *
 * <p>{@code DeviceParkApiClient}, Device Park platformunun tüm public REST
 * API'lerine tip-güvenli (type-safe) erişim sağlayan modüler API
 * istemcilerini (devices, pools, allocations, sessions, applications) tek bir
 * çatı altında toplar.
 *
 * <h2>Öözellikler</h2>
 * <ul>
 *   <li>OAuth2 client-credentials akışı ile otomatik token yönetimi (cache + refresh)</li>
 *   <li>Modül başına lazy ve memoized API servisleri</li>
 *   <li>Apache HttpClient 5 tabanlı, timeout konfigürasyonlu HTTP katmanı</li>
 *   <li>Jackson ile snake_case ve camelCase JSON desteği, {@code java.time} tipleri</li>
 *   <li>{@link Closeable} arayüzü ile kaynakların güvenli serbest bırakılması</li>
 * </ul>
 *
 * <h2>Hızlı Başlangıç</h2>
 * <pre>
 * Credentials credentials = Credentials.of("client-id", "client-secret");
 *
 * try (DeviceParkApiClient client = DeviceParkApiClient.builder()
 *         .url("https://device-park.example.com")
 *         .credentials(credentials)
 *         .timeout(60)
 *         .build()) {
 *
 *     PageDto&lt;Device&gt; devices = client.devices().list(
 *             ListDevicesRequest.builder().page(0).size(20).build());
 *
 *     devices.data().forEach(System.out::println);
 * }
 * </pre>
 *
 * <h2>Thread-Safety</h2>
 * <p>Bu sınıfın instaönce'ları thread-safe'tir; aynı instaönce birden çok
 * thread'den paylaşılarak kullanılabilir. Bir uygulama ömründe genellikle
 * tek bir {@code DeviceParkApiClient} oluşturmak yeterlidir.</p>
 *
 * <h2>Kaynak Yönetimi</h2>
 * <p>Sınıf {@link Closeable} arayüzünü uygular. Try-with-resources kullanımı
 * şiddetle önerilir.</p>
 *
 * @see DeviceParkApiClientBuilder
 * @see Credentials
 * @since 1.0.0
 */
public class DeviceParkApiClient implements Closeable {

    /**
     * Tüm API servisleri tarafından paylaşılan altta yatan HTTP istemci.
     */
    protected final DeviceParkHttpClient httpClient;

    /** Lazy/memoized {@link DevicesApi} sağlayıcı. */
    protected final Supplier<DevicesApi> devices;

    /**
     * Lazy/memoized {@link PoolsApi} sağlayıcı.
     */
    protected final Supplier<PoolsApi> pools;

    /**
     * Lazy/memoized {@link AllocationApi} sağlayıcı.
     */
    protected final Supplier<AllocationApi> allocations;

    /**
     * Lazy/memoized {@link SessionApi} sağlayıcı.
     */
    protected final Supplier<SessionApi> sessions;

    /**
     * Lazy/memoized {@link ApplicationApi} sağlayıcı.
     */
    protected final Supplier<ApplicationApi> applications;

    /**
     * Yeni bir {@code DeviceParkApiClient} oluşturur.
     *
     * <p>Doğrudan kullanım yerine {@link #builder()} fabrikasını tercih edin;
     * builder geçerli bir konfigürasyon doğrulaması yapar.</p>
     *
     * @param uri         Device Park sunucusunun base URL'i (örn. {@code https://dp.example.com})
     * @param timeout     HTTP istekleri için saniye cinsinden timeout; {@code null} ise varsayılan kullanılır
     * @param credentials OAuth2 client-credentials kimlik bilgileri (zorunlu)
     */
    public DeviceParkApiClient(String uri, Integer timeout, Credentials credentials) {
        this.httpClient = new DeviceParkHttpClient(uri, timeout, credentials);
        this.devices = Suppliers.memoize(() -> new DevicesApi(this.httpClient));
        this.pools = Suppliers.memoize(() -> new PoolsApi(this.httpClient));
        this.allocations = Suppliers.memoize(() -> new AllocationApi(this.httpClient));
        this.sessions = Suppliers.memoize(() -> new SessionApi(this.httpClient));
        this.applications = Suppliers.memoize(() -> new ApplicationApi(this.httpClient));
    }

    /**
     * Akıcı (fluent) konfigürasyon için yeni bir
     * {@link DeviceParkApiClientBuilder} döndürür.
     *
     * @return yeni builder instaönce'ı
     */
    public static DeviceParkApiClientBuilder builder() {
        return new DeviceParkApiClientBuilder();
    }

    /**
     * Cihaz yönetimi (Devices) API'sine erişim sağlar.
     *
     * @return paylaşımlı (memoized) {@link DevicesApi} instaönce'ı
     */
    public DevicesApi devices() {
        return devices.get();
    }

    /**
     * Cihaz havuzu (Pools) API'sine erişim sağlar.
     *
     * @return paylaşımlı (memoized) {@link PoolsApi} instaönce'ı
     */
    public PoolsApi pools() {
        return pools.get();
    }

    /**
     * Cihaz tahsisi (Allocations) API'sine erişim sağlar.
     *
     * @return paylaşımlı (memoized) {@link AllocationApi} instaönce'ı
     */
    public AllocationApi allocations() {
        return allocations.get();
    }

    /**
     * Test oturumu (Sessions) API'sine erişim sağlar.
     *
     * @return paylaşımlı (memoized) {@link SessionApi} instaönce'ı
     */
    public SessionApi sessions() {
        return sessions.get();
    }

    /**
     * Uygulama (APK/IPA) yönetimi (Applications) API'sine erişim sağlar.
     *
     * @return paylaşımlı (memoized) {@link ApplicationApi} instaönce'ı
     */
    public ApplicationApi applications() {
        return applications.get();
    }

    /**
     * Altta yatan HTTP istemcisini ve bağlantı havuzunu kapatır.
     *
     * @throws IOException HTTP istemcisi kapatılırken bir G/Ç hatası oluşursa
     */
    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}
