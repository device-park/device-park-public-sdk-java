package io.testinium.devicepark.management.pools;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.model.pools.CreatePoolRequest;
import io.testinium.devicepark.model.pools.ListPoolsRequest;
import io.testinium.devicepark.model.pools.Pool;
import io.testinium.devicepark.model.pools.PoolFilter;
import io.testinium.devicepark.model.pools.PoolFilterRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * API service for device pool management operations.
 *
 * <p>Pools are logically grouped device collections; they can be targeted
 * by {@code devicePoolId} during allocation.</p>
 *
 * <p>Obtain instances of this service via {@link
 * DeviceParkApiClient#pools()}.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /management/api/v1/public/pools}</p>
 *
 * @since 1.0.0
 */
public final class PoolsApi {

    private static final String POOLS_PATH = "/management/api/v1/public/pools";

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Creates a new {@code PoolsApi}.
     *
     * @param deviceParkHttpClient the shared HTTP client
     */
    public PoolsApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Lists device pools in a paginated manner.
     *
     * @param request pagination/sorting parameters; if {@code null},
     *                default values are used
     * @return a single page of {@link Pool} list
     */
    public PageDto<Pool> list(ListPoolsRequest request) {
        ListPoolsRequest req = request != null ? request : ListPoolsRequest.builder().build();
        Sorting s = req.getSorting();
        List<PoolFilterRequest> filters = req.getFilters();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);


        if (filters != null) {
            int filterIndex = 0;
            for (PoolFilterRequest filter : filters) {
                if (filter == null) {
                    continue;
                }
                qs.put("filters[" + filterIndex + "].key", filter.getKey());
                qs.put("filters[" + filterIndex + "].value", filter.getValue());
                qs.put("filters[" + filterIndex + "].operation", filter.getOperation());
                filterIndex++;
            }
        }

        String response = deviceParkHttpClient.get(POOLS_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Pool>>() {
        });
    }


    /**
     * Lists only default device pools in a paginated manner.
     *
     * @param request pagination/sorting parameters; if {@code null},
     *                default values are used
     * @return a single page of default {@link Pool} list (where {@code isDefault} is {@code true})
     */
    public PageDto<Pool> listByDefaultPool(ListPoolsRequest request) {

        ListPoolsRequest req = request != null ? request : ListPoolsRequest.builder().build();
        Sorting s = req.getSorting();

        List<PoolFilterRequest> filters = new ArrayList<>();
        PoolFilterRequest poolFilterRequest = new PoolFilterRequest();
        poolFilterRequest.setKey(PoolFilter.IS_DEFAULT);
        poolFilterRequest.setOperation(SearchOperation.EQUAL);
        poolFilterRequest.setValue(true);
        filters.add(poolFilterRequest);

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        int filterIndex = 0;
        for (PoolFilterRequest filter : filters) {
            qs.put("filters[" + filterIndex + "].key", filter.getKey());
            qs.put("filters[" + filterIndex + "].value", filter.getValue());
            qs.put("filters[" + filterIndex + "].operation", filter.getOperation());
            filterIndex++;
        }

        String response = deviceParkHttpClient.get(POOLS_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Pool>>() {
        });
    }

    /**
     * Creates a new device pool.
     *
     * @param request pool creation criteria (required); {@code name} must be unique within the account
     * @return the created {@link Pool}
     * @throws IllegalArgumentException if {@code request} is {@code null} or {@code name} is blank
     */
    public Pool create(CreatePoolRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        String body = JsonMapper.toJson(request);
        String response = deviceParkHttpClient.post(POOLS_PATH, body, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Pool>() {
        });
    }

    /**
     * Deletes the device pool with the given ID.
     *
     * <p>Devices in the pool are not deleted; they return to available inventory.</p>
     *
     * @param poolId the pool ID to delete
     * @throws IllegalArgumentException if {@code poolId} is null or empty
     */
    public void delete(String poolId) {
        if (poolId == null || poolId.trim().isEmpty()) {
            throw new IllegalArgumentException("poolId cannot be null or empty");
        }
        deviceParkHttpClient.delete(POOLS_PATH + "/" + poolId, null);
    }

    /**
     * Adds devices to the given pool by serial number.
     *
     * @param poolId  the target pool ID
     * @param serials device serial numbers to add
     * @return serial numbers of devices in the pool after the add operation
     * @throws IllegalArgumentException if {@code poolId} is blank or {@code serials} is null/empty
     */
    public List<String> addDevices(String poolId, List<String> serials) {
        validatePoolIdAndSerials(poolId, serials);
        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("serials", serials);
        String response = deviceParkHttpClient.post(POOLS_PATH + "/" + poolId + "/devices", null, qs, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<List<String>>() {
        });
    }

    /**
     * Removes devices from the given pool by serial number.
     *
     * <p>Devices are unbound from the pool; physical devices are not deleted.</p>
     *
     * @param poolId  the target pool ID
     * @param serials device serial numbers to remove
     * @return serial numbers remaining in the pool after the remove operation
     * @throws IllegalArgumentException if {@code poolId} is blank or {@code serials} is null/empty
     */
    public List<String> removeDevices(String poolId, List<String> serials) {
        validatePoolIdAndSerials(poolId, serials);
        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("serials", serials);
        String response = deviceParkHttpClient.delete(POOLS_PATH + "/" + poolId + "/devices", qs, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<List<String>>() {
        });
    }

    private static void validatePoolIdAndSerials(String poolId, List<String> serials) {
        if (poolId == null || poolId.trim().isEmpty()) {
            throw new IllegalArgumentException("poolId cannot be null or empty");
        }
        if (serials == null || serials.isEmpty()) {
            throw new IllegalArgumentException("serials cannot be null or empty");
        }
    }

}
