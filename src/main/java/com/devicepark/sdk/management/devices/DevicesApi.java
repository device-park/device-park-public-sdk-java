package com.devicepark.sdk.management.devices;

import com.devicepark.sdk.core.http.SdkHttpResponse;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.management.internal.ManagementRequestExecutor;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.common.Sorting;
import com.devicepark.sdk.model.devices.Device;
import com.devicepark.sdk.model.devices.ListDevicesRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Devices kaynağına ait API'ler.
 * {@link com.devicepark.sdk.management.DeviceParkManagementService#devices()} üzerinden erişilir.
 *
 * <pre>
 * client.management().devices().list(ListDevicesRequest.builder().page(0).size(20).build());
 * </pre>
 */
public final class DevicesApi {

    private final ManagementRequestExecutor requests;

    public DevicesApi(ManagementRequestExecutor requests) {
        this.requests = requests;
    }

    /**
     * Default sayfalama/sıralama değerleri ile cihazları listeler.
     */
    public PageDto<Device> list() {
        return list(ListDevicesRequest.builder().build());
    }

    /**
     * {@code GET /management/api/v1/public/devices} — yetkili müşterinin görebileceği
     * cihazları sayfalı olarak listeler.
     *
     * @param request sayfalama / sıralama parametreleri (null verilirse default'lar kullanılır)
     * @return {@link PageDto} içinde {@link Device} listesi
     */
    public PageDto<Device> list(ListDevicesRequest request) {
        ListDevicesRequest req = request != null ? request : ListDevicesRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        SdkHttpResponse resp = requests.get("/devices", qs);
        return JsonMapper.fromJson(resp.body(), new TypeReference<PageDto<Device>>() {});
    }
}
