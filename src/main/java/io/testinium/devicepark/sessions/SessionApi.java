package io.testinium.devicepark.sessions;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.Sorting;
import io.testinium.devicepark.model.sessions.DeviceSessionRequest;
import io.testinium.devicepark.model.sessions.DeviceStartSessionRequest;
import io.testinium.devicepark.model.sessions.Session;
import io.testinium.devicepark.model.sessions.screenRecord.ScreenRecord;
import io.testinium.devicepark.model.sessions.screenRecord.ScreenRecordPaginationRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test oturumu (session) işlemleri için API servisi.
 *
 * <p>Bir session, tahsis edilmiş bir cihaz üzerinde Appium komutları
 * çalıştırılan etkileşim aralığını temsil eder. Bu servis ile session
 * başlatabilir, durdurabilir, listeleyebilir, Appium loglarını ve ekran
 * kayıtlarını alabilirsiniz.</p>
 *
 * <p>Bu servis instaönce'larını {@link
 * DeviceParkApiClient#sessions()} ile elde edin.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <ul>
 *   <li>Sessions: {@code /session/api/v2/public/sessions}</li>
 *   <li>Screen records: {@code /storage/api/v1/public/sessions/{sessionId}/screen-records}</li>
 * </ul>
 *
 * @since 1.0.0
 */
public final class SessionApi {

    private static final String SESSION_PATH = "/session/api/v2/public/sessions";
    private static final String SCREEN_RECORD_PATH = "/storage/api/v1/public/sessions";

    private final DeviceParkHttpClient deviceParkHttpClient;

    public SessionApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    public PageDto<Session> list(DeviceSessionRequest request) {
        DeviceSessionRequest req = request != null ? request : DeviceSessionRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get(SESSION_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Session>>() {
        });
    }

    public Session start(DeviceStartSessionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        String body = JsonMapper.toJson(request);
        String response = deviceParkHttpClient.post(SESSION_PATH, body, null);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Session>() {
        });
    }

    public void stop(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("sessionId cannot be null or empty");
        }
        deviceParkHttpClient.delete(SESSION_PATH + "/" + sessionId, null);
    }

    public byte[] logs(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("sessionId cannot be null or empty");
        }
        return deviceParkHttpClient.getBytes(SESSION_PATH + "/" + sessionId + "/appium-log");
    }

    public PageDto<ScreenRecord> screenRecords(String sessionId, ScreenRecordPaginationRequest request) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("sessionId cannot be null or empty");
        }
        ScreenRecordPaginationRequest req = request != null ? request : ScreenRecordPaginationRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get(SCREEN_RECORD_PATH + "/" + sessionId + "/screen-records", qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<ScreenRecord>>() {
        });
    }
}
