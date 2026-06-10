package com.devicepark.sdk.auth.credentials;

/**
 * Bir {@link DeviceParkCredentials} örneği üreten kaynak.
 *
 * <p>AWS SDK'daki {@code AwsCredentialsProvider} muadilidir. Implementasyonlar
 * credentials'ı her seferinde farklı kaynaklardan (env, system property, profile dosyası,
 * memory) üretebilir.</p>
 */
@FunctionalInterface
public interface DeviceParkCredentialsProvider {

    /**
     * Yapılandırılmış kaynaktan credentials'ı çözer.
     *
     * @return geçerli credentials
     * @throws com.devicepark.sdk.core.exception.SdkClientException kaynaklarda credential bulunamazsa
     */
    DeviceParkCredentials resolveCredentials();
}

