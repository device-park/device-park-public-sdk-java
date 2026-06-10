package com.devicepark.sdk.core.endpoint;

import com.devicepark.sdk.core.exception.SdkClientException;
import com.devicepark.sdk.core.util.Validate;

/**
 * Endpoint'i environment variable veya system property üzerinden çözer.
 *
 * <p>Çözüm önceliği:</p>
 * <ol>
 *   <li>System property: {@code devicepark.endpoint}</li>
 *   <li>Environment variable: {@code DEVICEPARK_ENDPOINT}</li>
 * </ol>
 */
public final class EnvironmentEndpointProvider implements EndpointProvider {

    public static final String ENDPOINT_ENV_VAR = "DEVICEPARK_ENDPOINT";
    public static final String ENDPOINT_SYS_PROP = "devicepark.endpoint";

    private final EnvReader envReader;
    private final SysPropReader sysPropReader;

    public EnvironmentEndpointProvider() {
        this(System::getenv, System::getProperty);
    }

    // Test edilebilirlik için
    EnvironmentEndpointProvider(EnvReader envReader, SysPropReader sysPropReader) {
        this.envReader = envReader;
        this.sysPropReader = sysPropReader;
    }

    public static EnvironmentEndpointProvider create() {
        return new EnvironmentEndpointProvider();
    }

    @Override
    public Endpoint resolveEndpoint() {
        String fromSysProp = sysPropReader.get(ENDPOINT_SYS_PROP);
        if (!Validate.isBlank(fromSysProp)) {
            return Endpoint.of(fromSysProp);
        }
        String fromEnv = envReader.get(ENDPOINT_ENV_VAR);
        if (!Validate.isBlank(fromEnv)) {
            return Endpoint.of(fromEnv);
        }
        throw new SdkClientException(
                "Device Park endpoint çözülemedi. Lütfen '" + ENDPOINT_SYS_PROP + "' system property "
                        + "veya '" + ENDPOINT_ENV_VAR + "' environment variable tanımlayın.");
    }

    @FunctionalInterface
    interface EnvReader {
        String get(String name);
    }

    @FunctionalInterface
    interface SysPropReader {
        String get(String name);
    }
}

