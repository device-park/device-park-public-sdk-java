package com.devicepark.sdk.allocation;

import com.devicepark.sdk.client.DeviceParkHttpClient;
import com.devicepark.sdk.core.json.JsonMapper;
import com.devicepark.sdk.model.allocation.Allocation;
import com.devicepark.sdk.model.allocation.AllocationSearchRequest;
import com.devicepark.sdk.model.allocation.DeviceAllocationRequest;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.common.Sorting;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cihaz tahsisi (allocation) işlemlerini sunan API servisi.
 *
 * <p>Allocation, bir cihazın test/oturum amacıyla rezerve edilmesidir.
 * Tipik akış: {@link #create(DeviceAllocationRequest) create} → cihaz hazır
 * olduğunda dönen {@link Allocation#allocationId()} ile {@link
 * com.devicepark.sdk.sessions.SessionApi#start session başlat} → iş bitince
 * {@link #delete(String) delete}.</p>
 *
 * <p>Bu servis instance'larını {@link
 * com.devicepark.sdk.DeviceParkApiClient#allocations()} ile elde edin.</p>
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
     * Yeni bir {@code AllocationApi} oluşturur.
     *
     * <p>Genellikle doğrudan çağrılmaz; {@link
     * com.devicepark.sdk.DeviceParkApiClient#allocations()} kullanın.</p>
     *
     * @param deviceParkHttpClient paylaşılan HTTP istemcisi
     */
    public AllocationApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Yetkili istemcinin allocation'larını sayfalı olarak listeler.
     *
     * @param request sayfalama/sıralama parametreleri; {@code null} verilirse
     *                varsayılan değerler ({@code page=0, size=20, sortBy=ID,
     *                direction=DESC}) kullanılır
     * @return tek sayfalık {@link Allocation} listesi
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
     * Yeni bir cihaz tahsisi (allocation) oluşturur.
     *
     * <p>Eğer eşleşen cihaz boştaysa hemen tahsis edilir, aksi halde kuyruğa
     * alınır ve {@link Allocation#position()} ile bekleme sırası bilgisi döner.</p>
     *
     * @param request hedef cihaz/pool kriterleri ve öncelik (zorunlu)
     * @return oluşturulan tahsis kaydı
     * @throws IllegalArgumentException {@code request} {@code null} ise
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
     * Verilen ID'ye sahip allocation'ı iptal eder/serbest bırakır.
     *
     * @param allocationId iptal edilecek tahsis kimliği
     */
    public void delete(String allocationId) {
        deviceParkHttpClient.delete(ALLOCATION_PATH + "/" + allocationId, null);
    }
}
