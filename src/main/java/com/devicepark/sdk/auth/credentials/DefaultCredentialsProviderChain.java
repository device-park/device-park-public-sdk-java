package com.devicepark.sdk.auth.credentials;

import com.devicepark.sdk.core.exception.SdkClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Birden fazla {@link DeviceParkCredentialsProvider}'ı sırayla deneyen chain.
 *
 * <p>İlk başarılı provider'ın sonucu döner; hepsi başarısız olursa toplu
 * {@link com.devicepark.sdk.core.exception.SdkClientException} fırlatır.</p>
 *
 * <p>Default chain sırası:</p>
 * <ol>
 *   <li>{@link SystemPropertyCredentialsProvider} ({@code devicepark.clientId} / {@code devicepark.clientSecret})</li>
 *   <li>{@link EnvironmentVariableCredentialsProvider} ({@code DEVICEPARK_CLIENT_ID} / {@code DEVICEPARK_CLIENT_SECRET})</li>
 *   <li>{@link ProfileCredentialsProvider} ({@code ~/.devicepark/credentials})</li>
 * </ol>
 */
public final class DefaultCredentialsProviderChain implements DeviceParkCredentialsProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCredentialsProviderChain.class);

    private final List<DeviceParkCredentialsProvider> providers;

    private DefaultCredentialsProviderChain(List<DeviceParkCredentialsProvider> providers) {
        this.providers = Collections.unmodifiableList(new ArrayList<>(providers));
    }

    public static DefaultCredentialsProviderChain create() {
        return new DefaultCredentialsProviderChain(Arrays.asList(
                SystemPropertyCredentialsProvider.create(),
                EnvironmentVariableCredentialsProvider.create(),
                ProfileCredentialsProvider.create()
        ));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public DeviceParkCredentials resolveCredentials() {
        List<String> errors = new ArrayList<>();
        for (DeviceParkCredentialsProvider provider : providers) {
            try {
                DeviceParkCredentials creds = provider.resolveCredentials();
                LOG.debug("Credentials çözüldü: {}", provider.getClass().getSimpleName());
                return creds;
            } catch (RuntimeException e) {
                LOG.debug("Provider başarısız [{}]: {}",
                        provider.getClass().getSimpleName(), e.getMessage());
                errors.add(provider.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
        throw new SdkClientException(
                "Hiçbir credentials provider başarılı olamadı. Denemeler: " + errors);
    }

    public static final class Builder {
        private final List<DeviceParkCredentialsProvider> providers = new ArrayList<>();

        public Builder add(DeviceParkCredentialsProvider provider) {
            providers.add(provider);
            return this;
        }

        public DefaultCredentialsProviderChain build() {
            if (providers.isEmpty()) {
                throw new IllegalStateException("Chain en az bir provider içermeli");
            }
            return new DefaultCredentialsProviderChain(providers);
        }
    }
}

