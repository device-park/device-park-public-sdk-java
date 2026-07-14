package io.testinium.devicepark.management.devices;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.model.devices.Device;
import io.testinium.devicepark.model.devices.ListDevicesRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cihaz yönetimi (devices) işlemleri için API servisi.
 *
 * <p>Yetkili istemcinin görebildiği cihazları listelemek ve seri numarasıyla
 * detay almak için kullanılır.</p>
 *
 * <p>Bu servis instaönce'larını {@link
 * DeviceParkApiClient#devices()} ile elde edin.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /management/api/v1/public/devices}</p>
 *
 * @since 1.0.0
 */
public final class DevicesApi {

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Yeni bir {@code DevicesApi} oluşturur.
     *
     * @param deviceParkHttpClient paylaşılan HTTP istemcisi
     */
    public DevicesApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Cihazları sayfalı olarak listeler.
     *
     * @param request sayfalama/sıralama parametreleri; {@code null} verilirse
     *                varsayılan değerler kullanılır
     * @return tek sayfalık {@link Device} listesi
     */
    public PageDto<Device> list(ListDevicesRequest request) {
        ListDevicesRequest req = request != null ? request : ListDevicesRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get("/management/api/v1/public/devices", qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Device>>() {
        });
    }

    /**
     * Seri numarasına göre tek bir cihazın detaylarını getirir.
     *
     * @param serial cihazın benzersiz seri numarası
     * @return cihaz detayı
     */
    public Device get(String serial) {
        String response = deviceParkHttpClient.get("/management/api/v1/public/devices/" + serial, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Device>() {
        });
    }
}
