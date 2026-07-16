package io.testinium.devicepark.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.authentication.credentials.Credentials;
import io.testinium.devicepark.authentication.token.AccessToken;
import io.testinium.devicepark.core.AbstractApiService;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

/**
 * Apache HttpClient 5 based, OAuth2-integrated HTTP client.
 *
 * <p>This is the transport (transport) layer shared by all SDK API services.
 * End users do not use this class directly; related API services
 * (devices, sessions, etc.) are consumed via {@link
 * DeviceParkApiClient}.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>OAuth2 client-credentials flow token acquisition and caching
 *       (automatic refresh with 60s safety margin)</li>
 *   <li>Adding {@code Authorization} header to each request</li>
 *   <li>JSON, multipart, {@code application/octet-stream} stream
 *       and byte upload support</li>
 *   <li>Throwing non-2xx HTTP responses as {@link IOException}</li>
 * </ul>
 *
 * <h2>Thread-Safety</h2>
 * <p>Apache HttpClient 5 is thread-safe. Token cache updates are protected
 * with {@code synchronized}.</p>
 *
 * <p>This class is an internal part of the SDK.</p>
 *
 * @since 1.0.0
 */
public class DeviceParkHttpClient extends AbstractApiService implements Closeable {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String baseUrl;
    private final CloseableHttpClient httpClient;
    private final Credentials credentials;
    private AccessToken cachedToken;

    /**
     * Creates a new {@code DeviceParkHttpClient}.
     *
     * @param baseUrl        Device Park server base URL
     * @param timeoutSeconds response/connection-request timeout in seconds; if {@code null}, defaults to 20 seconds
     * @param credentials    OAuth2 credentials (required)
     */
    public DeviceParkHttpClient(String baseUrl, Integer timeoutSeconds, Credentials credentials) {
        this.baseUrl = baseUrl;
        this.credentials = credentials;
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(timeoutSeconds != null ? timeoutSeconds : 20))
                .setConnectionRequestTimeout(Timeout.ofSeconds(timeoutSeconds != null ? timeoutSeconds : 20))
                .build();

        this.httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * Query parametresi ve özel header olmadan {@code GET} isteği yapar.
     *
     * @param path endpoint path'i (base URL'e göre)
     * @return ham response gövdesi (JSON veya text)
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String get(String path) {
        try {
            HttpGet request = new HttpGet(buildUrl(path));
            addHeaders(request, null);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    /**
     * Query parametreli {@code GET} isteği yapar.
     *
     * @param path        endpoint path'i
     * @param queryParams URL-encode edilecek query parametreleri ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String get(String path, Map<String, ?> queryParams) {
        try {
            HttpGet request = new HttpGet(buildUri(baseUrl, path, queryParams));
            addHeaders(request, null);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    /**
     * Query parametreli ve özel header'lı {@code GET} isteği yapar.
     *
     * @param path        endpoint path'i
     * @param queryParams URL-encode edilecek query parametreleri ({@code null} olabilir)
     * @param headers     ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String get(String path, Map<String, ?> queryParams, Map<String, String> headers) {
        try {
            HttpGet request = new HttpGet(buildUri(baseUrl, path, queryParams));
            addHeaders(request, headers);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET request for path: " + path, e);
        }
    }

    /**
     * JSON gövdeli {@code POST} isteği yapar.
     *
     * @param path    endpoint path'i
     * @param body    JSON-serialize edilmiş istek gövdesi ({@code null} ise body yollanmaz)
     * @param headers ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String post(String path, String body, Map<String, String> headers) {
        try {
            HttpPost request = new HttpPost(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute POST request for path: " + path, e);
        }
    }

    /**
     * JSON gövdeli {@code PUT} isteği yapar.
     *
     * @param path    endpoint path'i
     * @param body    JSON-serialize edilmiş istek gövdesi ({@code null} ise body yollanmaz)
     * @param headers ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String put(String path, String body, Map<String, String> headers) {
        try {
            HttpPut request = new HttpPut(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute PUT request for path: " + path, e);
        }
    }

    /**
     * {@code DELETE} isteği yapar.
     *
     * @param path    endpoint path'i
     * @param headers ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi (genelde boş)
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String delete(String path, Map<String, String> headers) {
        try {
            HttpDelete request = new HttpDelete(buildUrl(path));
            addHeaders(request, headers);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute DELETE request for path: " + path, e);
        }
    }

    /**
     * {@code multipart/form-data} dosya yüklemesi yapar.
     *
     * @param path         endpoint path'i
     * @param file         yüklenecek dosya
     * @param filePartName form alanı adı; {@code null} ise {@code "file"} kullanılır
     * @param formFields   ek metin form alanları ({@code null} olabilir)
     * @param headers      ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String postMultipart(String path, File file, String filePartName, Map<String, String> formFields,
                                Map<String, String> headers) {
        try {
            HttpPost request = new HttpPost(buildUrl(path));
            addHeaders(request, headers);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            String partName = filePartName != null ? filePartName : "file";
            builder.addBinaryBody(partName, file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            if (formFields != null) {
                formFields.forEach((k, v) -> {
                    if (v != null) {
                        builder.addTextBody(k, v, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8));
                    }
                });
            }
            HttpEntity entity = builder.build();
            request.setEntity(entity);
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute multipart POST request for path: " + path, e);
        }
    }

    /**
     * {@code application/octet-stream} olarak ham {@link InputStream} yükler.
     *
     * <p>Stream'in kapatılması çağıran tarafın sorumluluğundadır.</p>
     *
     * @param path    endpoint path'i
     * @param body    yüklenecek stream ({@code null} ise body yollanmaz)
     * @param headers ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String postStream(String path, InputStream body, Map<String, String> headers) {
        try {
            HttpPost request = new HttpPost(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new InputStreamEntity(body, -1, ContentType.APPLICATION_OCTET_STREAM));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute octet-stream POST request for path: " + path, e);
        }
    }

    /**
     * {@code application/octet-stream} olarak ham byte dizisi yükler.
     *
     * @param path    endpoint path'i
     * @param body    yüklenecek byte dizisi ({@code null} ise body yollanmaz)
     * @param headers ek HTTP header'ları ({@code null} olabilir)
     * @return ham response gövdesi
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public String postBytes(String path, byte[] body, Map<String, String> headers) {
        try {
            HttpPost request = new HttpPost(buildUrl(path));
            addHeaders(request, headers);
            if (body != null) {
                request.setEntity(new ByteArrayEntity(body, ContentType.APPLICATION_OCTET_STREAM));
            }
            return execute(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute octet-stream POST request for path: " + path, e);
        }
    }

    /**
     * Binary içerik (örn. log dosyası, ekran kaydı) için {@code GET} isteği yapar.
     *
     * @param path endpoint path'i
     * @return response gövdesinin ham byte temsili
     * @throws RuntimeException ağ hatası veya 2xx olmayan yanıt durumunda
     */
    public byte[] getBytes(String path) {
        try {
            HttpGet request = new HttpGet(buildUrl(path));
            addHeaders(request, null);
            return executeBytes(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute GET (bytes) request for path: " + path, e);
        }
    }

    private byte[] executeBytes(ClassicHttpRequest request) throws IOException {
        return httpClient.execute(request, response -> {
            int status = response.getCode();
            byte[] body = response.getEntity() != null ? EntityUtils.toByteArray(response.getEntity()) : new byte[0];
            if (status >= 200 && status < 300) {
                return body;
            } else {
                throw new IOException("HTTP Request Failed with status: " + status
                        + " body: " + new String(body, StandardCharsets.UTF_8));
            }
        });
    }

    private String execute(ClassicHttpRequest request) throws IOException {
        return httpClient.execute(request, response -> {
            int status = response.getCode();
            String responseBody = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : null;
            if (status >= 200 && status < 300) {
                return responseBody;
            } else {
                throw new IOException("HTTP Request Failed with status: " + status + " body: " + responseBody);
            }
        });
    }

    private String buildUrl(String path) {
        if (path == null) {
            return baseUrl;
        }
        if (baseUrl.endsWith("/") && path.startsWith("/")) {
            return baseUrl + path.substring(1);
        } else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            return baseUrl + "/" + path;
        }
        return baseUrl + path;
    }

    private synchronized AccessToken getValidAccessToken() throws IOException {
        if (cachedToken == null || !cachedToken.isValid(java.time.Duration.ofSeconds(60), java.time.Instant.now())) {
            cachedToken = fetchAccessToken();
        }
        return cachedToken;
    }

    private AccessToken fetchAccessToken() throws IOException {
        HttpPost request = new HttpPost(this.baseUrl + "/uaa/oauth2/token");
        String basicAuth = Base64.getEncoder().encodeToString(
                (credentials.getClientId() + ":" + credentials.getClientSecret()).getBytes(StandardCharsets.UTF_8));
        request.addHeader("Authorization", "Basic " + basicAuth);
        String formParams = "grant_type=client_credentials&scope=openid";
        request.setEntity(new StringEntity(formParams, ContentType.APPLICATION_FORM_URLENCODED));
        String responseBody = execute(request);
        return MAPPER.readValue(responseBody, AccessToken.class);
    }

    private void addHeaders(ClassicHttpRequest request, Map<String, String> headers) throws IOException {
        request.addHeader("Authorization", getValidAccessToken().authorizationHeader());
        Optional.ofNullable(headers).ifPresent(map -> map.forEach(request::addHeader));
    }

    /**
     * Altta yatan Apache HTTP istemcisini kapatır ve bağlantı havuzunu serbest
     * bırakır.
     *
     * @throws IOException kapama sırasında G/Ç hatası oluşursa
     */
    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
