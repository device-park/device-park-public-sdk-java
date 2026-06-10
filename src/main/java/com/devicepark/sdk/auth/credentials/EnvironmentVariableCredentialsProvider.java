package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.exception.SdkClientException;
import com.devicepark.sdk.core.util.Validate;

/**
 * Credentials'ı işletim sistemi environment variable'larından okur.
 *
 * <ul>
 *   <li>{@code DEVICEPARK_CLIENT_ID}</li>
 *   <li>{@code DEVICEPARK_CLIENT_SECRET}</li>
 * </ul>
 */
public final class EnvironmentVariableCredentialsProvider implements DeviceParkCredentialsProvider {

    public static final String CLIENT_ID_ENV_VAR = "DEVICEPARK_CLIENT_ID";
    public static final String CLIENT_SECRET_ENV_VAR = "DEVICEPARK_CLIENT_SECRET";

    private final EnvReader envReader;

    public EnvironmentVariableCredentialsProvider() {
        this(System::getenv);
    }

    // Test edilebilirlik için
    EnvironmentVariableCredentialsProvider(EnvReader envReader) {
        this.envReader = envReader;
    }

    public static EnvironmentVariableCredentialsProvider create() {
        return new EnvironmentVariableCredentialsProvider();
    }

    @Override
    public DeviceParkCredentials resolveCredentials() {
        String clientId = envReader.get(CLIENT_ID_ENV_VAR);
        String clientSecret = envReader.get(CLIENT_SECRET_ENV_VAR);

        if (Validate.isBlank(clientId) || Validate.isBlank(clientSecret)) {
            throw new SdkClientException(
                    "Environment variable'lardan Device Park credentials çözülemedi. "
                            + "Lütfen " + CLIENT_ID_ENV_VAR + " ve " + CLIENT_SECRET_ENV_VAR
                            + " değişkenlerini tanımlayın.");
        }
        return BasicCredentials.create(clientId, clientSecret);
    }

    @FunctionalInterface
    interface EnvReader {
        String get(String name);
    }
}

