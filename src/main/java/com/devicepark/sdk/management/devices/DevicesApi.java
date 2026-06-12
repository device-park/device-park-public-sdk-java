package com.devicepark.sdk.management.devices;

import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.AbstractApiService;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.devices.Device;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;


public final class DevicesApi extends AbstractApiService {

    private final DeviceParkHttpClient deviceParkHttpClient;

    public DevicesApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Default sayfalama/sıralama değerleri ile cihazları listeler.
     */
    public PageDto<Device> list() throws IOException {
        String response = deviceParkHttpClient.get("/management/api/v1/public/devices");
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Device>>() {
        });
    }
}
