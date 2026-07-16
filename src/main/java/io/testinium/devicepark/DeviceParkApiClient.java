package io.testinium.devicepark;

import io.testinium.devicepark.allocation.AllocationApi;
import io.testinium.devicepark.applications.ApplicationApi;
import io.testinium.devicepark.authentication.credentials.Credentials;
import io.testinium.devicepark.client.DeviceParkHttpClient;
import io.testinium.devicepark.core.util.Suppliers;
import io.testinium.devicepark.management.devices.DevicesApi;
import io.testinium.devicepark.management.pools.PoolsApi;
import io.testinium.devicepark.sessions.SessionApi;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * Main entry point for the Device Park Public SDK.
 *
 * <p>{@code DeviceParkApiClient} bundles modular API clients that provide
 * type-safe access to all public REST APIs of the Device Park platform
 * (devices, pools, allocations, sessions, applications) under a single interface.
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Automatic token management via OAuth2 client-credentials flow (cached and refreshed)</li>
 *   <li>Lazy and memoized API services per module</li>
 *   <li>Apache HttpClient 5 based HTTP layer with configurable timeouts</li>
 *   <li>Jackson with snake_case and camelCase JSON support, {@code java.time} types</li>
 *   <li>{@link Closeable} interface for safe resource cleanup</li>
 * </ul>
 *
 * <h2>Quick Start</h2>
 * <pre>
 * Credentials credentials = Credentials.of("client-id", "client-secret");
 *
 * try (DeviceParkApiClient client = DeviceParkApiClient.builder()
 *         .url("https://device-park.example.com")
 *         .credentials(credentials)
 *         .timeout(60)
 *         .build()) {
 *
 *     PageDto&lt;Device&gt; devices = client.devices().list(
 *             ListDevicesRequest.builder().page(0).size(20).build());
 *
 *     devices.data().forEach(System.out::println);
 * }
 * </pre>
 *
 * <h2>Thread-Safety</h2>
 * <p>Instances of this class are thread-safe; the same instance can be shared
 * across multiple threads. Typically, a single {@code DeviceParkApiClient}
 * instance per application lifetime is sufficient.</p>
 *
 * <h2>Resource Management</h2>
 * <p>The class implements the {@link Closeable} interface. Use try-with-resources
 * is strongly recommended.</p>
 *
 * @see DeviceParkApiClientBuilder
 * @see Credentials
 * @since 1.0.0
 */
public class DeviceParkApiClient implements Closeable {

    /**
     * The underlying HTTP client shared by all API services.
     */
    protected final DeviceParkHttpClient httpClient;

    /**
     * Lazy/memoized {@link DevicesApi} supplier.
     */
    protected final Supplier<DevicesApi> devices;

    /**
     * Lazy/memoized {@link PoolsApi} supplier.
     */
    protected final Supplier<PoolsApi> pools;

    /**
     * Lazy/memoized {@link AllocationApi} supplier.
     */
    protected final Supplier<AllocationApi> allocations;

    /**
     * Lazy/memoized {@link SessionApi} supplier.
     */
    protected final Supplier<SessionApi> sessions;

    /**
     * Lazy/memoized {@link ApplicationApi} supplier.
     */
    protected final Supplier<ApplicationApi> applications;

    /**
     * Creates a new {@code DeviceParkApiClient}.
     *
     * <p>Prefer using the {@link #builder()} factory over direct construction;
     * the builder validates configuration and throws {@link IllegalArgumentException}
     * if invalid.</p>
     *
     * @param uri         Base URL of the Device Park server (e.g., {@code https://dp.example.com})
     * @param timeout     Timeout for HTTP requests in seconds; if {@code null}, defaults are used
     * @param credentials OAuth2 client-credentials (required)
     */
    public DeviceParkApiClient(String uri, Integer timeout, Credentials credentials) {
        this.httpClient = new DeviceParkHttpClient(uri, timeout, credentials);
        this.devices = Suppliers.memoize(() -> new DevicesApi(this.httpClient));
        this.pools = Suppliers.memoize(() -> new PoolsApi(this.httpClient));
        this.allocations = Suppliers.memoize(() -> new AllocationApi(this.httpClient));
        this.sessions = Suppliers.memoize(() -> new SessionApi(this.httpClient));
        this.applications = Suppliers.memoize(() -> new ApplicationApi(this.httpClient));
    }

    /**
     * Returns a new {@link DeviceParkApiClientBuilder} for fluent configuration.
     *
     * @return a new builder instance
     */
    public static DeviceParkApiClientBuilder builder() {
        return new DeviceParkApiClientBuilder();
    }

    /**
     * Provides access to the Device Management (Devices) API.
     *
     * @return shared (memoized) {@link DevicesApi} instance
     */
    public DevicesApi devices() {
        return devices.get();
    }

    /**
     * Provides access to the Device Pool (Pools) API.
     *
     * @return shared (memoized) {@link PoolsApi} instance
     */
    public PoolsApi pools() {
        return pools.get();
    }

    /**
     * Provides access to the Device Allocation (Allocations) API.
     *
     * @return shared (memoized) {@link AllocationApi} instance
     */
    public AllocationApi allocations() {
        return allocations.get();
    }

    /**
     * Provides access to the Test Session (Sessions) API.
     *
     * @return shared (memoized) {@link SessionApi} instance
     */
    public SessionApi sessions() {
        return sessions.get();
    }

    /**
     * Provides access to Application (APK/IPA) Management (Applications) API.
     *
     * @return shared (memoized) {@link ApplicationApi} instance
     */
    public ApplicationApi applications() {
        return applications.get();
    }

    /**
     * Closes the underlying HTTP client and connection pool.
     *
     * @throws IOException if an I/O error occurs while closing the HTTP client
     */
    @Override
    public void close() throws IOException {
        this.httpClient.close();
    }
}
