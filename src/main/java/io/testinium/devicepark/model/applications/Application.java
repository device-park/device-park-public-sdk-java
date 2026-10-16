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
 * <p>Metadata fields are extracted from the uploaded artifact and are
 * platform specific: {@link #bundleId()}, {@link #title()},
 * {@link #ipaVersion()}, {@link #enterprise()} and {@link #minimumOsVersion()}
 * are only populated for IPA uploads, {@link #minSdkVersion()} only for APK
 * uploads. Fields that cannot be read from the artifact stay {@code null}.</p>
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
    private final String bundleId;
    private final String title;
    private final String ipaVersion;
    private final Boolean enterprise;
    private final String minimumOsVersion;
    private final Integer minSdkVersion;

    /**
     * JSON deserileştirme için kullanılan constructor.
     *
     * @param revision         revizyon numarası (artan)
     * @param sizeInBytes      dosya boyutu (byte)
     * @param version          uygulama versiyonu (en fazla 50 karakter)
     * @param fileKey          uygulamanın benzersiz anahtarı
     * @param filePath         sunucudaki saklama yolu
     * @param downloadUrl      önceden imzalanmış indirme URL'i
     * @param createdAt        yüklenme zamanı
     * @param bundleId         IPA bundle identifier (yalnızca IPA)
     * @param title            uygulama adı (yalnızca IPA)
     * @param ipaVersion       IPA içinden okunan versiyon (yalnızca IPA)
     * @param enterprise       enterprise imzalı IPA ise {@code true} (yalnızca IPA)
     * @param minimumOsVersion desteklenen en düşük iOS sürümü (yalnızca IPA)
     * @param minSdkVersion    desteklenen en düşük Android API seviyesi (yalnızca APK)
     */
    @JsonCreator
    public Application(
            @JsonProperty("revision") Long revision,
            @JsonProperty("size_in_bytes") Long sizeInBytes,
            @JsonProperty("version") String version,
            @JsonProperty("fileKey") String fileKey,
            @JsonProperty("filePath") String filePath,
            @JsonProperty("downloadUrl") String downloadUrl,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("bundleId") String bundleId,
            @JsonProperty("title") String title,
            @JsonProperty("ipaVersion") String ipaVersion,
            @JsonProperty("enterprise") Boolean enterprise,
            @JsonProperty("minimumOsVersion") String minimumOsVersion,
            @JsonProperty("minSdkVersion") Integer minSdkVersion) {
        this.revision = revision;
        this.sizeInBytes = sizeInBytes;
        this.version = version;
        this.fileKey = fileKey;
        this.filePath = filePath;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
        this.bundleId = bundleId;
        this.title = title;
        this.ipaVersion = ipaVersion;
        this.enterprise = enterprise;
        this.minimumOsVersion = minimumOsVersion;
        this.minSdkVersion = minSdkVersion;
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

    /**
     * @return IPA bundle identifier'ı (ör. {@code com.example.app}); IPA dışı
     * yüklemelerde {@code null}
     */
    public String bundleId() {
        return bundleId;
    }

    /**
     * @return IPA Info.plist içinden okunan uygulama adı; IPA dışı yüklemelerde
     * {@code null}
     */
    public String title() {
        return title;
    }

    /**
     * @return IPA Info.plist içinden okunan versiyon; yükleme sırasında verilen
     * {@link #version()} değerinden farklı olabilir, IPA dışı yüklemelerde
     * {@code null}
     */
    public String ipaVersion() {
        return ipaVersion;
    }

    /**
     * @return IPA enterprise provisioning profili ile imzalandıysa {@code true};
     * IPA dışı yüklemelerde {@code null}
     */
    public Boolean enterprise() {
        return enterprise;
    }

    /**
     * @return IPA'nın desteklediği en düşük iOS sürümü (ör. {@code "13.0"});
     * IPA dışı yüklemelerde {@code null}
     */
    public String minimumOsVersion() {
        return minimumOsVersion;
    }

    /**
     * @return APK'nın desteklediği en düşük Android API seviyesi (ör. {@code 24});
     * APK dışı yüklemelerde {@code null}
     */
    public Integer minSdkVersion() {
        return minSdkVersion;
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
                + ", createdAt=" + createdAt + ", bundleId=" + bundleId
                + ", title=" + title + ", ipaVersion=" + ipaVersion
                + ", enterprise=" + enterprise + ", minimumOsVersion=" + minimumOsVersion
                + ", minSdkVersion=" + minSdkVersion + ")";
    }
}
