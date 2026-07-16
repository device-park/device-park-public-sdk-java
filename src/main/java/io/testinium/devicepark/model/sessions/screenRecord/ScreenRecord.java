package io.testinium.devicepark.model.sessions.screenRecord;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable model representing a screen record (screen recording) taken during a session.
 *
 * <p>Maps one-to-one to the server-side {@code ExternalScreenRecordResponse}. {@link #downloadUrl()}
 * returns a pre-signed URL and may become invalid after a certain period.</p>
 *
 * <p>Equality is computed via {@link #fileKey()}.</p>
 *
 * @since 1.0.0
 */
public final class ScreenRecord {

    private final String fileKey;
    private final String filePath;
    private final String downloadUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Double duration;

    @JsonCreator
    public ScreenRecord(
            @JsonProperty("fileKey") String fileKey,
            @JsonProperty("filePath") String filePath,
            @JsonProperty("downloadUrl") String downloadUrl,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("updatedAt") LocalDateTime updatedAt,
            @JsonProperty("duration") Double duration) {
        this.fileKey = fileKey;
        this.filePath = filePath;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.duration = duration;
    }

    public String fileKey() {
        return fileKey;
    }

    public String filePath() {
        return filePath;
    }

    public String downloadUrl() {
        return downloadUrl;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public Double duration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScreenRecord)) return false;
        ScreenRecord that = (ScreenRecord) o;
        return Objects.equals(fileKey, that.fileKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileKey);
    }

    @Override
    public String toString() {
        return "ScreenRecord(fileKey=" + fileKey + ", filePath=" + filePath
                + ", downloadUrl=" + downloadUrl + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", duration=" + duration + ")";
    }
}

