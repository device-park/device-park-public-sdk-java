package io.testinium.devicepark.model.allocation;

/**
 * Criteria sent in a new device allocation (allocation) request.
 *
 * <p>At least one targeting parameter ({@code serial} or {@code devicePoolId}
 * or a {@code platform}/{@code platformVersion} combination) must be provided.
 * The server allocates the first suitable device matching these criteria.</p>
 *
 * <h2>Example</h2>
 * <pre>
 * DeviceAllocationRequest req = DeviceAllocationRequest.builder()
 *         .platform("Android")
 *         .platformVersion("13")
 *         .priority(2)
 *         .build();
 * </pre>
 *
 * <h2>Priority</h2>
 * <p>Must be between {@code 1} (highest) and {@code 5} (lowest);
 * defaults to {@code 3}.</p>
 *
 * @since 1.0.0
 */
public class DeviceAllocationRequest {

    private String serial;
    private String manufacturer;
    private String model;
    private String platform;
    private String platformVersion;
    private String devicePoolId;
    private Integer priority = 3;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getDevicePoolId() {
        return devicePoolId;
    }

    public void setDevicePoolId(String devicePoolId) {
        this.devicePoolId = devicePoolId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        if (priority == null) {
            this.priority = 3;
            return;
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("priority must be between 1 and 5");
        }
        this.priority = priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final DeviceAllocationRequest req = new DeviceAllocationRequest();

        public Builder serial(String serial) {
            req.setSerial(serial);
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            req.setManufacturer(manufacturer);
            return this;
        }

        public Builder model(String model) {
            req.setModel(model);
            return this;
        }

        public Builder platform(String platform) {
            req.setPlatform(platform);
            return this;
        }

        public Builder platformVersion(String platformVersion) {
            req.setPlatformVersion(platformVersion);
            return this;
        }

        public Builder devicePoolId(String devicePoolId) {
            req.setDevicePoolId(devicePoolId);
            return this;
        }

        public Builder priority(Integer priority) {
            req.setPriority(priority);
            return this;
        }

        public DeviceAllocationRequest build() {
            return req;
        }
    }
}
