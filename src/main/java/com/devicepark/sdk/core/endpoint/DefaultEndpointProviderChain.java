package com.devicepark.sdk.core.endpoint;

import com.devicepark.sdk.core.exception.SdkClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Birden fazla {@link EndpointProvider}'ı sırayla deneyen chain.
 *
 * <p>Default chain sırası: {@link EnvironmentEndpointProvider} (sysprop önce, sonra env).
 * Production'da çoğunlukla {@link StaticEndpointProvider} ile birlikte
 * builder üzerinden özelleştirilir.</p>
 */
public final class DefaultEndpointProviderChain implements EndpointProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEndpointProviderChain.class);

    private final List<EndpointProvider> providers;

    private DefaultEndpointProviderChain(List<EndpointProvider> providers) {
        this.providers = Collections.unmodifiableList(new ArrayList<>(providers));
    }

    public static DefaultEndpointProviderChain create() {
        return new DefaultEndpointProviderChain(Collections.singletonList(EnvironmentEndpointProvider.create()));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Endpoint resolveEndpoint() {
        List<String> errors = new ArrayList<>();
        for (EndpointProvider provider : providers) {
            try {
                Endpoint endpoint = provider.resolveEndpoint();
                LOG.debug("Endpoint çözüldü: {} -> {}",
                        provider.getClass().getSimpleName(), endpoint);
                return endpoint;
            } catch (RuntimeException e) {
                LOG.debug("Endpoint provider başarısız [{}]: {}",
                        provider.getClass().getSimpleName(), e.getMessage());
                errors.add(provider.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
        throw new SdkClientException(
                "Hiçbir endpoint provider başarılı olamadı. Denemeler: " + errors);
    }

    public static final class Builder {
        private final List<EndpointProvider> providers = new ArrayList<>();

        public Builder add(EndpointProvider provider) {
            providers.add(provider);
            return this;
        }

        public DefaultEndpointProviderChain build() {
            if (providers.isEmpty()) {
                throw new IllegalStateException("Chain en az bir provider içermeli");
            }
            return new DefaultEndpointProviderChain(providers);
        }
    }
}

