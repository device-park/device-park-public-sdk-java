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
 * API service for device management (devices) operations.
 *
 * <p>Used to list devices visible to the authorized client and fetch details
 * by serial number.</p>
 *
 * <p>Obtain instances of this service via {@link
 * DeviceParkApiClient#devices()}.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /management/api/v1/public/devices}</p>
 *
 * @since 1.0.0
 */
public final class DevicesApi {

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Creates a new {@code DevicesApi}.
     *
     * @param deviceParkHttpClient the shared HTTP client
     */
    public DevicesApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Lists devices in a paginated manner.
     *
     * @param request pagination/sorting parameters; if {@code null},
     *                default values are used
     * @return a single page of {@link Device} list
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
     * Fetches details of a single device by serial number.
     *
     * @param serial the device's unique serial number
     * @return device details
     */
    public Device get(String serial) {
        String response = deviceParkHttpClient.get("/management/api/v1/public/devices/" + serial, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Device>() {
        });
    }
}
