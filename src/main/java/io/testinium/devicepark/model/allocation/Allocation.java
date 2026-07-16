package io.testinium.devicepark.model.allocation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Device allocation (allocation) response model.
 *
 * <p>Maps one-to-one to the server-side {@code DeviceAllocationResponse}.
 * If the allocation is granted immediately, {@link #deviceSerial()} is populated;
 * otherwise {@link #position()} contains queue position information.</p>
 *
 * <p>This class is immutable. Equality is computed via {@link #allocationId()}.</p>
 *
 * @since 1.0.0
 */
public final class Allocation {

    private final String allocationId;
    private final String deviceSerial;
    private final String requestId;
    private final Integer position;
    private final Instant expiresAt;

    /**
     * JSON deserileştirme için kullanılan constructor.
     *
     * @param allocationId unique allocation ID
     * @param deviceSerial the device's serial number if allocation is granted immediately, otherwise {@code null}
     * @param requestId    request ID (idempotency / tracking)
     * @param position     position in queue (1 = first); {@code null} if allocation is granted immediately
     * @param expiresAt    time when the allocation will automatically expire
     */
    @JsonCreator
    public Allocation(
            @JsonProperty("allocationId") String allocationId,
            @JsonProperty("deviceSerial") String deviceSerial,
            @JsonProperty("requestId") String requestId,
            @JsonProperty("position") Integer position,
            @JsonProperty("expiresAt") Instant expiresAt) {
        this.allocationId = allocationId;
        this.deviceSerial = deviceSerial;
        this.requestId = requestId;
        this.position = position;
        this.expiresAt = expiresAt;
    }

    /**
     * @return unique allocation ID
     */
    public String allocationId() {
        return allocationId;
    }

    /**
     * @return the serial number of the allocated device ({@code null} if queued)
     */
    public String deviceSerial() {
        return deviceSerial;
    }

    /**
     * @return request ID
     */
    public String requestId() {
        return requestId;
    }

    /**
     * @return position in queue; {@code null} if device is allocated immediately
     */
    public Integer position() {
        return position;
    }

    /**
     * @return time when the allocation will automatically expire
     */
    public Instant expiresAt() {
        return expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allocation)) return false;
        Allocation that = (Allocation) o;
        return Objects.equals(allocationId, that.allocationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allocationId);
    }

    @Override
    public String toString() {
        return "DeviceAllocationResponse(allocationId=" + allocationId + ", deviceSerial=" + deviceSerial
                + ", requestId=" + requestId + ", position=" + position + ", expiresAt=" + expiresAt + ")";
    }
}
