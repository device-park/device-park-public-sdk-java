package com.devicepark.sdk.management;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.core.endpoint.EndpointProvider;
import com.devicepark.sdk.core.http.AuthRefreshingHttpClient;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.management.devices.DevicesApi;
import com.devicepark.sdk.management.internal.DefaultManagementRequestExecutor;
import com.devicepark.sdk.management.internal.ManagementRequestExecutor;

/**
 * Device Park <b>Management</b> servis grubu. {@link com.devicepark.sdk.DeviceParkApiClient}
 * tarafından oluşturulup inject edilir; lifecycle (close) root client tarafından yönetilir.
 *
 * <p>Kullanım:</p>
 * <pre>
 * try (DeviceParkApiClient client = DeviceParkApiClient.builder()
 *         .endpoint("https://dev-devicepark.testinium.io")
 *         .credentials("client-id", "client-secret")
 *         .build()) {
 *
 *     var page = client.management().devices().list(
 *             ListDevicesRequest.builder().page(0).size(20).build());
 *
 *     page.data().forEach(System.out::println);
 * }
 * </pre>
 *
 * <p>İleride eklenecek resource'lar (sessions, storage, ...) için aynı pattern:
 * {@code client.management().sessions()}, {@code client.management().storage()} vb.</p>
 */
public final class DeviceParkManagementService {

    private final DevicesApi devices;
    // İleride: private final SessionsApi sessions;
    // İleride: private final StorageApi storage;

    /**
     * Sadece SDK içi kullanım için. Root {@code DeviceParkApiClient} tarafından çağrılır.
     *
     * @param authClient paylaşımlı authentication client
     * @param httpClient paylaşımlı HTTP client (retry decorator ile sarılı olabilir)
     * @param endpointProvider paylaşımlı endpoint çözücü
     */
    public DeviceParkManagementService(DeviceParkAuthClient authClient,
                                       SdkHttpClient httpClient,
                                       EndpointProvider endpointProvider) {
        this(authClient, httpClient, endpointProvider, true);
    }

    /**
     * @param decorateWithSdkAuthDecorator true ise SDK auth decorator kullanılır,
     *                                     false ise HTTP client zaten auth-aware kabul edilir.
     */
    public DeviceParkManagementService(DeviceParkAuthClient authClient,
                                       SdkHttpClient httpClient,
                                       EndpointProvider endpointProvider,
                                       boolean decorateWithSdkAuthDecorator) {
        SdkHttpClient effectiveHttpClient = decorateWithSdkAuthDecorator
                ? new AuthRefreshingHttpClient(httpClient, authClient)
                : httpClient;
        ManagementRequestExecutor requestExecutor =
                new DefaultManagementRequestExecutor(effectiveHttpClient, endpointProvider);
        this.devices = new DevicesApi(requestExecutor);
        // İleride buraya sessions/storage init edilecek.
    }

    /** Devices kaynağına ait API'ler. */
    public DevicesApi devices() {
        return devices;
    }

    // İleride:
    // public SessionsApi sessions() { return sessions; }
    // public StorageApi storage() { return storage; }
}
