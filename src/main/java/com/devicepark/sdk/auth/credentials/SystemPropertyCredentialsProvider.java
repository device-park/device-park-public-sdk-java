package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.exception.SdkClientException;
import com.devicepark.sdk.core.util.Validate;

/**
 * Credentials'ı JVM system property'lerinden okur.
 *
 * <ul>
 *   <li>{@code devicepark.clientId}</li>
 *   <li>{@code devicepark.clientSecret}</li>
 * </ul>
 */
public final class SystemPropertyCredentialsProvider implements DeviceParkCredentialsProvider {

    public static final String CLIENT_ID_PROPERTY = "devicepark.clientId";
    public static final String CLIENT_SECRET_PROPERTY = "devicepark.clientSecret";

    private final SysPropReader reader;

    public SystemPropertyCredentialsProvider() {
        this(System::getProperty);
    }

    SystemPropertyCredentialsProvider(SysPropReader reader) {
        this.reader = reader;
    }

    public static SystemPropertyCredentialsProvider create() {
        return new SystemPropertyCredentialsProvider();
    }

    @Override
    public DeviceParkCredentials resolveCredentials() {
        String clientId = reader.get(CLIENT_ID_PROPERTY);
        String clientSecret = reader.get(CLIENT_SECRET_PROPERTY);

        if (Validate.isBlank(clientId) || Validate.isBlank(clientSecret)) {
            throw new SdkClientException(
                    "System property'lerden Device Park credentials çözülemedi. Lütfen '"
                            + CLIENT_ID_PROPERTY + "' ve '" + CLIENT_SECRET_PROPERTY
                            + "' system property'lerini tanımlayın.");
        }
        return BasicCredentials.create(clientId, clientSecret);
    }

    @FunctionalInterface
    interface SysPropReader {
        String get(String name);
    }
}

