package com.devicepark.sdk.auth.credentials;

/**
 * Device Park SDK için kullanıcı kimlik bilgilerini (client-id / client-secret)
 * temsil eden ortak arayüz.
 *
 * <p>AWS SDK'daki {@code AwsCredentials} arayüzünün muadilidir.</p>
 */
public interface DeviceParkCredentials {

    /**
     * @return SDK kullanıcısına verilen client id
     */
    String clientId();

    /**
     * @return SDK kullanıcısına verilen client secret
     */
    String clientSecret();
}

