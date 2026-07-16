package io.testinium.devicepark.applications;

import com.fasterxml.jackson.core.type.TypeReference;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.json.JsonMapper;
import io.testinium.devicepark.model.applications.Application;
import io.testinium.devicepark.model.applications.ApplicationPaginationRequest;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.common.Sorting;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API service for mobile application (APK/IPA) management.
 *
 * <p>Uploaded applications can be installed on devices during tests. Multiple
 * revisions (versions) can be stored under the same {@code fileKey}.</p>
 *
 * <p>Obtain instances of this service via {@link
 * DeviceParkApiClient#applications()}.</p>
 *
 * <h2>Endpoint Base Path</h2>
 * <p>{@code /storage/api/v1/public/applications}</p>
 *
 * @since 1.0.0
 */
public final class ApplicationApi {

    private static final String APPLICATION_PATH = "/storage/api/v1/public/applications";

    private final DeviceParkHttpClient deviceParkHttpClient;

    /**
     * Creates a new {@code ApplicationApi}.
     *
     * <p>Usually not called directly; use {@link
     * DeviceParkApiClient#applications()}.</p>
     *
     * @param deviceParkHttpClient the shared HTTP client
     */
    public ApplicationApi(DeviceParkHttpClient deviceParkHttpClient) {
        this.deviceParkHttpClient = deviceParkHttpClient;
    }

    /**
     * Lists applications belonging to the authorized client in a paginated manner.
     *
     * @param request pagination/sorting parameters; if {@code null},
     *                default values are used
     * @return a single page of {@link Application} list
     */
    public PageDto<Application> list(ApplicationPaginationRequest request) {
        ApplicationPaginationRequest req = request != null ? request : ApplicationPaginationRequest.builder().build();
        Sorting s = req.getSorting();

        Map<String, Object> qs = new LinkedHashMap<>();
        qs.put("sorting.page", s.getPage());
        qs.put("sorting.size", s.getSize());
        qs.put("sorting.sortBy", s.getSortBy());
        qs.put("sorting.direction", s.getDirection() != null ? s.getDirection().name() : null);

        String response = deviceParkHttpClient.get(APPLICATION_PATH, qs);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<PageDto<Application>>() {
        });
    }

    /**
     * Uploads a file via {@link InputStream} with {@code application/octet-stream}.
     *
     * <p>Closing the stream is the caller's responsibility
     * (try-with-resources is recommended).</p>
     *
     * <h3>Example</h3>
     * <pre>
     * try (InputStream in = Files.newInputStream(Paths.get("/path/app.apk"))) {
     *     Application app = client.applications().upload(in, "app.apk", "1.0.0");
     *     System.out.println(app.fileKey());
     * }
     * </pre>
     *
     * @param fileStream file content (required)
     * @param fileName   file name to be used on the server; sent as {@code file-name} header
     *                   (required)
     * @param version    application version, maximum 50 characters (required)
     * @return metadata of the uploaded application
     * @throws IllegalArgumentException if any parameter is empty/null or
     *                                  {@code version} exceeds 50 characters
     */
    public Application upload(InputStream fileStream, String fileName, String version) {
        return upload(fileStream, fileName, version, false);
    }

    /**
     * Uploads a file via {@link InputStream} with {@code application/octet-stream}.
     *
     * @param fileStream     file content (required)
     * @param fileName       file name to be used on the server
     * @param version        application version
     * @param imageInjection if true, gadget injection is applied
     */
    public Application upload(InputStream fileStream, String fileName, String version, boolean imageInjection) {
        if (fileStream == null) {
            throw new IllegalArgumentException("fileStream cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("fileName cannot be null or empty");
        }
        validateVersion(version);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("file-name", fileName);
        headers.put("version", version);
        headers.put("image-injection", String.valueOf(imageInjection));

        String response = deviceParkHttpClient.postStream(APPLICATION_PATH, fileStream, headers);
        return JsonMapper.fromJson(response.getBytes(), new TypeReference<Application>() {
        });
    }

    /**
     * Deletes the application with the given {@code fileKey} (and all its revisions).
     *
     * @param fileKey the key of the application to be deleted
     * @throws IllegalArgumentException if {@code fileKey} is empty/null
     */
    public void delete(String fileKey) {
        if (fileKey == null || fileKey.trim().isEmpty()) {
            throw new IllegalArgumentException("fileKey cannot be null or empty");
        }
        deviceParkHttpClient.delete(APPLICATION_PATH + "/" + fileKey, null);
    }

    private static void validateVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            throw new IllegalArgumentException("version cannot be null or empty");
        }
        if (version.length() > 50) {
            throw new IllegalArgumentException("version length must be at most 50 characters");
        }
    }
}
