package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.exception.SdkClientException;
import com.devicepark.sdk.core.util.Validate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Credentials'ı kullanıcının ev dizinindeki bir profil dosyasından okur.
 *
 * <p>Varsayılan konum: {@code ~/.devicepark/credentials} (override için
 * {@code DEVICEPARK_CREDENTIALS_FILE} env var veya {@code devicepark.credentialsFile}
 * system property kullanılabilir).</p>
 *
 * <p>Dosya formatı (INI benzeri):</p>
 * <pre>
 * [default]
 * devicepark_client_id     = AKIA...
 * devicepark_client_secret = wJalr...
 *
 * [staging]
 * devicepark_client_id     = ...
 * devicepark_client_secret = ...
 * </pre>
 *
 * <p>Kullanılan profil adı varsayılan olarak {@code default}'tur. Override için
 * {@code DEVICEPARK_PROFILE} env var veya {@code devicepark.profile} system property
 * kullanılabilir.</p>
 */
public final class ProfileCredentialsProvider implements DeviceParkCredentialsProvider {

    public static final String DEFAULT_PROFILE_NAME = "default";

    public static final String PROFILE_NAME_ENV_VAR = "DEVICEPARK_PROFILE";
    public static final String PROFILE_NAME_SYS_PROP = "devicepark.profile";

    public static final String CREDENTIALS_FILE_ENV_VAR = "DEVICEPARK_CREDENTIALS_FILE";
    public static final String CREDENTIALS_FILE_SYS_PROP = "devicepark.credentialsFile";

    public static final String CLIENT_ID_KEY = "devicepark_client_id";
    public static final String CLIENT_SECRET_KEY = "devicepark_client_secret";

    private final Path credentialsFile;
    private final String profileName;

    private ProfileCredentialsProvider(Path credentialsFile, String profileName) {
        this.credentialsFile = credentialsFile;
        this.profileName = profileName;
    }

    public static ProfileCredentialsProvider create() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public DeviceParkCredentials resolveCredentials() {
        if (!Files.exists(credentialsFile)) {
            throw new SdkClientException(
                    "Device Park credentials dosyası bulunamadı: " + credentialsFile);
        }

        Map<String, Map<String, String>> profiles;
        try {
            profiles = parse(Files.readAllLines(credentialsFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new SdkClientException("Credentials dosyası okunamadı: " + credentialsFile, e);
        }

        Map<String, String> entries = profiles.get(profileName);
        if (entries == null) {
            throw new SdkClientException("Profil bulunamadı: '" + profileName
                    + "' (dosya: " + credentialsFile + ")");
        }

        String clientId = entries.get(CLIENT_ID_KEY);
        String clientSecret = entries.get(CLIENT_SECRET_KEY);
        if (Validate.isBlank(clientId) || Validate.isBlank(clientSecret)) {
            throw new SdkClientException("Profil '" + profileName + "' '" + CLIENT_ID_KEY
                    + "' ve '" + CLIENT_SECRET_KEY + "' alanlarını içermeli.");
        }
        return BasicCredentials.create(clientId, clientSecret);
    }

    /** Çok küçük bir INI parser. Yorumlar ('#' veya ';') ve boş satırlar yok sayılır. */
    static Map<String, Map<String, String>> parse(List<String> lines) {
        Map<String, Map<String, String>> profiles = new LinkedHashMap<>();
        Map<String, String> current = null;

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                continue;
            }
            if (line.startsWith("[") && line.endsWith("]")) {
                String name = line.substring(1, line.length() - 1).trim();
                current = profiles.computeIfAbsent(name, k -> new HashMap<>());
                continue;
            }
            int eq = line.indexOf('=');
            if (eq <= 0 || current == null) {
                continue; // section dışı veya geçersiz satırı sessizce atla
            }
            String key = line.substring(0, eq).trim();
            String value = line.substring(eq + 1).trim();
            current.put(key, value);
        }
        return profiles;
    }

    public static final class Builder {
        private Path credentialsFile;
        private String profileName;

        public Builder credentialsFile(Path path) {
            this.credentialsFile = path;
            return this;
        }

        public Builder profileName(String name) {
            this.profileName = name;
            return this;
        }

        public ProfileCredentialsProvider build() {
            Path path = credentialsFile != null ? credentialsFile : resolveDefaultPath();
            String name = profileName != null ? profileName : resolveDefaultProfile();
            return new ProfileCredentialsProvider(path, name);
        }

        private static Path resolveDefaultPath() {
            String fromSys = System.getProperty(CREDENTIALS_FILE_SYS_PROP);
            if (!Validate.isBlank(fromSys)) return Paths.get(fromSys);
            String fromEnv = System.getenv(CREDENTIALS_FILE_ENV_VAR);
            if (!Validate.isBlank(fromEnv)) return Paths.get(fromEnv);
            return Paths.get(System.getProperty("user.home"), ".devicepark", "credentials");
        }

        private static String resolveDefaultProfile() {
            String fromSys = System.getProperty(PROFILE_NAME_SYS_PROP);
            if (!Validate.isBlank(fromSys)) return fromSys;
            String fromEnv = System.getenv(PROFILE_NAME_ENV_VAR);
            if (!Validate.isBlank(fromEnv)) return fromEnv;
            return DEFAULT_PROFILE_NAME;
        }
    }
}

