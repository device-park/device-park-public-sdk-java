package io.testinium.devicepark.management.pools;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.model.pools.ListPoolsRequest;
import io.testinium.devicepark.model.pools.Pool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cihaz havuzu (device pool) yönetimi için API servisi.
 *
 * <p>Pool'lar, mantıksal olarak gruplanmış cihaz kümeleridir; allocation
 * sırasında {@code devicePoolId} ile hedeflenebilirler.</p>
 *
 * <p>Bu servis instaönce'larını {@link
 * DeviceParkApiClient#pools()} ile elde edin.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /management/api/v1/public/pools}</p>
 *
 * @since 1.0.0
 */
public final class PoolsApi {

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Yeni bir {@code PoolsApi} oluşturur.
     *
     * @param deviceParkHttpClient paylaşılan HTTP istemcisi
     */
    public PoolsApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Cihaz havuzlarını sayfalı olarak listeler.
     *
     * @param request sayfalama/sıralama parametreleri; {@code null} verilirse
     *                varsayılan değerler kullanılır
     * @return tek sayfalık {@link Pool} listesi
     */
    public PageDto<Pool> list(ListPoolsRequest request) {
        ListPoolsRequest req = request != null ? request : ListPoolsRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get("/management/api/v1/public/pools", qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Pool>>() {
        });
    }
}
