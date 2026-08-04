package io.testinium.devicepark.management.devices;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.management.pools.PoolsApi;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.model.devices.Device;
import io.testinium.devicepark.model.devices.DeviceFilter;
import io.testinium.devicepark.model.devices.DeviceFilterRequest;
import io.testinium.devicepark.model.devices.ListDevicesRequest;
import io.testinium.devicepark.model.pools.Pool;

import java.util.LinkedHashMap;
import java.util.List;
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
 * <p>{@code /management/api/v2/public/devices}</p>
 *
 * @since 1.0.0
 */
public final class DevicesApi {

    private static final String DEVICES_PATH = "/management/api/v2/public/devices";

    private final DeviceParkHttpClient deviceParkHttpClient;
    private final PoolsApi poolsApi;

    /**
     * Creates a new {@code DevicesApi}.
     *
     * @param deviceParkHttpClient the shared HTTP client
     * @param poolsApi             pools API used to resolve the default pool
     */
    public DevicesApi(DeviceParkHttpClient deviceParkHttpClient, PoolsApi poolsApi) {
        this.deviceParkHttpClient = deviceParkHttpClient;
        this.poolsApi = poolsApi;
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
        List<DeviceFilterRequest> filters = req.getFilters();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        if (filters != null) {
            int filterIndex = 0;
            for (DeviceFilterRequest filter : filters) {
                if (filter == null) {
                    continue;
                }
                qs.put("filters[" + filterIndex + "].key", filter.getKey());
                qs.put("filters[" + filterIndex + "].value", filter.getValue());
                qs.put("filters[" + filterIndex + "].operation", filter.getOperation());
                filterIndex++;
            }
        }

        String response = deviceParkHttpClient.get(DEVICES_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Device>>() {
        });
    }

    /**
     * Lists devices that belong to the client's default pool.
     *
     * <p>Resolves the default pool via {@link PoolsApi#getDefaultPool()} and
     * applies a {@link DeviceFilter#POOL_ID} filter. Any existing {@code POOL_ID}
     * filter in {@code request} is ignored so the default pool always wins.</p>
     *
     * @param request pagination/sorting/extra filters; if {@code null},
     *                default values are used
     * @return a single page of {@link Device} list from the default pool
     */
    public PageDto<Device> listByDefaultPool(ListDevicesRequest request) {
        ListDevicesRequest req = request != null ? request : ListDevicesRequest.builder().build();
        Sorting s = req.getSorting();
        Pool pool = poolsApi.getDefaultPool();

        ListDevicesRequest.Builder builder = ListDevicesRequest.builder()
                .page(s.getPage())
                .size(s.getSize())
                .sortBy(s.getSortBy())
                .direction(s.getDirection())
                .addFilter(DeviceFilter.POOL_ID, pool.id(), SearchOperation.EQUAL);

        List<DeviceFilterRequest> filters = req.getFilters();
        if (filters != null) {
            for (DeviceFilterRequest filter : filters) {
                if (filter == null || DeviceFilter.POOL_ID.equals(filter.getKey())) {
                    continue;
                }
                builder.addFilter(filter.getKey(), filter.getValue(), filter.getOperation());
            }
        }

        return list(builder.build());
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
