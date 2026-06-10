package com.devicepark.sdk.core.endpoint;

/**
 * Bir {@link Endpoint} örneği üreten kaynak.
 *
 * <p>AWS SDK'daki {@code EndpointProvider} muadilidir. Implementasyonlar
 * endpoint'i kod, environment variable, system property veya remote discovery
 * gibi kaynaklardan üretebilir.</p>
 */
@FunctionalInterface
public interface EndpointProvider {

    /**
     * @return kullanılacak gateway endpoint'i
     * @throws com.devicepark.sdk.core.exception.SdkClientException endpoint çözülemezse
     */
    Endpoint resolveEndpoint();
}

