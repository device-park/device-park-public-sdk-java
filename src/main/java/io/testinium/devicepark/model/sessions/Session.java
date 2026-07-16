package io.testinium.devicepark.model.sessions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Tahsis edilmiş bir cihaz üzerinde gerçekleşen test oturumunu temsil eden
 * değiştirilemez (immutable) model.
 *
 * <p>Server tarafındaki {@code DeviceSessionResponse} ile birebir eşleşir.
 * Eşitlik {@link #sessionId()} üzerinden hesaplanır.</p>
 *
 * @since 1.0.0
 */
public final class Session {

    private final Long id;
    private final String state;
    private final String client;
    private final String sessionId;
    private final String allocationId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime latestInteractionTime;
    private final Long userId;
    private final String userEmail;
    private final Long companyId;
    private final String companyName;
    private final String deviceSerial;
    private final String deviceName;
    private final String deviceModel;
    private final String deviceManufacturer;
    private final String devicePlatform;
    private final String deviceVersion;
    private final Boolean videoRecording;
    private final String videoRecordUrl;
    private final String appiumVersion;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Instant dataAccessEndDate;

    @JsonCreator
    public Session(
            @JsonProperty("id") Long id,
            @JsonProperty("state") String state,
            @JsonProperty("client") String client,
            @JsonProperty("sessionId") String sessionId,
            @JsonProperty("allocationId") String allocationId,
            @JsonProperty("startDate") LocalDateTime startDate,
            @JsonProperty("endDate") LocalDateTime endDate,
            @JsonProperty("latestInteractionTime") LocalDateTime latestInteractionTime,
            @JsonProperty("userId") Long userId,
            @JsonProperty("userEmail") String userEmail,
            @JsonProperty("companyId") Long companyId,
            @JsonProperty("companyName") String companyName,
            @JsonProperty("deviceSerial") String deviceSerial,
            @JsonProperty("deviceName") String deviceName,
            @JsonProperty("deviceModel") String deviceModel,
            @JsonProperty("deviceManufacturer") String deviceManufacturer,
            @JsonProperty("devicePlatform") String devicePlatform,
            @JsonProperty("deviceVersion") String deviceVersion,
            @JsonProperty("videoRecording") Boolean videoRecording,
            @JsonProperty("videoRecordUrl") String videoRecordUrl,
            @JsonProperty("appiumVersion") String appiumVersion,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt,
            @JsonProperty("dataAccessEndDate") Instant dataAccessEndDate) {
        this.id = id;
        this.state = state;
        this.client = client;
        this.sessionId = sessionId;
        this.allocationId = allocationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latestInteractionTime = latestInteractionTime;
        this.userId = userId;
        this.userEmail = userEmail;
        this.companyId = companyId;
        this.companyName = companyName;
        this.deviceSerial = deviceSerial;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.deviceManufacturer = deviceManufacturer;
        this.devicePlatform = devicePlatform;
        this.deviceVersion = deviceVersion;
        this.videoRecording = videoRecording;
        this.videoRecordUrl = videoRecordUrl;
        this.appiumVersion = appiumVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dataAccessEndDate = dataAccessEndDate;
    }

    public Long id() {
        return id;
    }

    public String state() {
        return state;
    }

    public String client() {
        return client;
    }

    public String sessionId() {
        return sessionId;
    }

    public String allocationId() {
        return allocationId;
    }

    public LocalDateTime startDate() {
        return startDate;
    }

    public LocalDateTime endDate() {
        return endDate;
    }

    public LocalDateTime latestInteractionTime() {
        return latestInteractionTime;
    }

    public Long userId() {
        return userId;
    }

    public String userEmail() {
        return userEmail;
    }

    public Long companyId() {
        return companyId;
    }

    public String companyName() {
        return companyName;
    }

    public String deviceSerial() {
        return deviceSerial;
    }

    public String deviceName() {
        return deviceName;
    }

    public String deviceModel() {
        return deviceModel;
    }

    public String deviceManufacturer() {
        return deviceManufacturer;
    }

    public String devicePlatform() {
        return devicePlatform;
    }

    public String deviceVersion() {
        return deviceVersion;
    }

    public Boolean videoRecording() {
        return videoRecording;
    }

    public String videoRecordUrl() {
        return videoRecordUrl;
    }

    public String appiumVersion() {
        return appiumVersion;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public Instant dataAccessEndDate() {
        return dataAccessEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session that = (Session) o;
        return Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "DeviceSessionResponse(sessionId=" + sessionId + ", allocationId=" + allocationId
                + ", state=" + state + ", deviceSerial=" + deviceSerial + ")";
    }
}

