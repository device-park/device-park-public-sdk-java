package io.testinium.devicepark.management.pools;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.Sorting;
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

        String response = deviceParkHttpClient.get("/management/api/v1/public/pools", qs);
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

        String response = deviceParkHttpClient.get("/management/api/v1/public/pools", qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Pool>>() {
        });
    }

}
