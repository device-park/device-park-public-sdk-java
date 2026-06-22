package com.devicepark.sdk.model.allocation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

/**
 * Cihaz tahsisi (allocation) yanıt modeli.
 *
 * <p>Server tarafındaki {@code DeviceAllocationResponse} ile birebir eşleşir.
 * Tahsis hemen verilirse {@link #deviceSerial()} dolu olur; aksi halde
 * {@link #position()} ile bekleme sırası bilgisi gelir.</p>
 *
 * <p>Bu sınıf değiştirilemez (immutable). Eşitlik {@link #allocationId()}
 * üzerinden hesaplanır.</p>
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
     * @param allocationId benzersiz tahsis kimliği
     * @param deviceSerial tahsis hemen verildiyse cihazın seri numarası, aksi halde {@code null}
     * @param requestId    talep kimliği (idempotency / takip)
     * @param position     kuyruktaki sıra (1 = en önde); tahsis hemen verildiyse {@code null}
     * @param expiresAt    tahsisin otomatik düşeceği zaman
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
     * @return benzersiz tahsis kimliği
     */
    public String allocationId() {
        return allocationId;
    }

    /**
     * @return tahsis edilen cihazın seri numarası ({@code null} olabilir, kuyruktaysa)
     */
    public String deviceSerial() {
        return deviceSerial;
    }

    /**
     * @return talep kimliği
     */
    public String requestId() {
        return requestId;
    }

    /**
     * @return kuyruktaki sıra; cihaz hemen tahsis edildiyse {@code null}
     */
    public Integer position() {
        return position;
    }

    /**
     * @return tahsisin otomatik olarak düşeceği zaman
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
