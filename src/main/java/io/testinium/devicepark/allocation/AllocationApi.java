package io.testinium.devicepark.allocation;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.allocation.Allocation;
import io.testinium.devicepark.model.allocation.AllocationSearchRequest;
import io.testinium.devicepark.model.allocation.DeviceAllocationRequest;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.sessions.SessionApi;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API service providing device allocation (allocation) operations.
 *
 * <p>An allocation is the reservation of a device for testing/session purposes.
 * Typical flow: {@link #create(DeviceAllocationRequest) create} → when device is ready,
 * use returned {@link Allocation#allocationId()} with {@link
 * SessionApi#start start session} → delete when done via {@link #delete(String) delete}.</p>
 *
 * <p>Obtain instances of this service via {@link
 * DeviceParkApiClient#allocations()}.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /allocation/api/v2/public/allocations}</p>
 *
 * @since 1.0.0
 */
public final class AllocationApi {

    private static final String ALLOCATION_PATH = "/allocation/api/v2/public/allocations";

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Creates a new {@code AllocationApi}.
     *
     * <p>Usually not called directly; use {@link
     * DeviceParkApiClient#allocations()}.</p>
     *
     * @param deviceParkHttpClient the shared HTTP client
     */
    public AllocationApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Lists allocations of the authorized client in a paginated manner.
     *
     * @param request pagination/sorting parameters; if {@code null},
     *                default values ({@code page=0, size=20, sortBy=ID,
     *                direction=DESC}) are used
     * @return a single page of {@link Allocation} list
     */
    public PageDto<Allocation> list(AllocationSearchRequest request) {
        AllocationSearchRequest req = request != null ? request : AllocationSearchRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get(ALLOCATION_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Allocation>>() {
        });
    }

    /**
     * Creates a new device allocation (allocation).
     *
     * <p>If a matching device is available, it is allocated immediately; otherwise,
     * it is queued and queue position information is returned via {@link Allocation#position()}.</p>
     *
     * @param request target device/pool criteria and priority (required)
     * @return the created allocation record
     * @throws IllegalArgumentException if {@code request} is {@code null}
     */
    public Allocation create(DeviceAllocationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        String body = JsonMapper.toJson(request);
        String response = deviceParkHttpClient.post(ALLOCATION_PATH, body, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Allocation>() {
        });
    }

    /**
     * Cancels/releases the allocation with the given ID.
     *
     * @param allocationId the allocation ID to be cancelled
     */
    public void delete(String allocationId) {
        deviceParkHttpClient.delete(ALLOCATION_PATH + "/" + allocationId, null);
    }
}
