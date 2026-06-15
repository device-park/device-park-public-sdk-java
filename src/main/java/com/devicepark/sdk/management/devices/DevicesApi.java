package com.devicepark.sdk.management.devices;

import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.common.Sorting;
import com.devicepark.sdk.model.devices.Device;
import com.devicepark.sdk.model.devices.ListDevicesRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.Map;


public final class DevicesApi {

    private final DeviceParkHttpClient deviceParkHttpClient;

    public DevicesApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Default sayfalama/sıralama değerleri ile cihazları listeler.
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


    /* Detaylı bir findBySerial,   */
}
