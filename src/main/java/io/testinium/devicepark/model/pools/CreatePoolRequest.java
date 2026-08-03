package io.testinium.devicepark.model.pools;

/**
 * Request body for creating a new device pool.
 *
 * <h2>Example</h2>
 * <pre>
 * CreatePoolRequest req = CreatePoolRequest.builder()
 *         .name("regression-pool")
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class CreatePoolRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final CreatePoolRequest req = new CreatePoolRequest();

        public Builder name(String name) {
            req.name = name;
            return this;
        }

        public CreatePoolRequest build() {
            return req;
        }
    }
}
