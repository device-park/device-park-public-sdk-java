package io.testinium.devicepark.model.applications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable model representing an uploaded mobile application.
 *
 * <p>Maps one-to-one to the server-side {@code ApplicationResponse}.
 * Multiple {@link #revision()}s can exist under the same {@code fileKey};
 * each revision carries a different {@link #version()}.</p>
 *
 * <p>Equality is computed via {@link #fileKey()}.</p>
 *
 * @since 1.0.0
 */
public final class Application {

    private final Long revision;
    private final Long sizeInBytes;
    private final String version;
    private final String fileKey;
    private final String filePath;
    private final String downloadUrl;
    private final LocalDateTime createdAt;

    /**
     * JSON deserileştirme için kullanılan constructor.
     *
     * @param revision    revizyon numarası (artan)
     * @param sizeInBytes dosya boyutu (byte)
     * @param version     uygulama versiyonu (en fazla 50 karakter)
     * @param fileKey     uygulamanın benzersiz anahtarı
     * @param filePath    sunucudaki saklama yolu
     * @param downloadUrl önceden imzalanmış indirme URL'i
     * @param createdAt   yüklenme zamanı
     */
    @JsonCreator
    public Application(
            @JsonProperty("revision") Long revision,
            @JsonProperty("size_in_bytes") Long sizeInBytes,
            @JsonProperty("version") String version,
            @JsonProperty("fileKey") String fileKey,
            @JsonProperty("filePath") String filePath,
            @JsonProperty("downloadUrl") String downloadUrl,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.revision = revision;
        this.sizeInBytes = sizeInBytes;
        this.version = version;
        this.fileKey = fileKey;
        this.filePath = filePath;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
    }

    /**
     * @return revizyon numarası
     */
    public Long revision() {
        return revision;
    }

    /**
     * @return dosya boyutu (byte)
     */
    @JsonProperty("size_in_bytes")
    public Long sizeInBytes() {
        return sizeInBytes;
    }

    /**
     * @return uygulama versiyonu
     */
    public String version() {
        return version;
    }

    /**
     * @return uygulamanın benzersiz anahtarı
     */
    public String fileKey() {
        return fileKey;
    }

    /**
     * @return sunucudaki saklama yolu
     */
    public String filePath() {
        return filePath;
    }

    /**
     * @return önceden imzalanmış indirme URL'i (zaman aşımına uğrayabilir)
     */
    public String downloadUrl() {
        return downloadUrl;
    }

    /**
     * @return yüklenme zamanı
     */
    public LocalDateTime createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application)) return false;
        Application that = (Application) o;
        return Objects.equals(fileKey, that.fileKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileKey);
    }

    @Override
    public String toString() {
        return "ApplicationResponse(fileKey=" + fileKey + ", filePath=" + filePath
                + ", version=" + version + ", revision=" + revision
                + ", sizeInBytes=" + sizeInBytes + ", downloadUrl=" + downloadUrl
                + ", createdAt=" + createdAt + ")";
    }
}
