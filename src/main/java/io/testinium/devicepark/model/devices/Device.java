package io.testinium.devicepark.model.devices;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Device model returned from the Device Park Management API (external customer view).
 *
 * <p>Maps one-to-one to the server-side {@code ExternalDeviceResponse}.</p>
 */
public final class Device {

    private final Long id;
    private final String serial;
    private final String marketName;
    private final String model;
    private final String manufacturer;
    private final String platform;
    private final String platformVersion;
    private final String version;
    private final String state;
    private final Boolean isSimulator;
    private final Boolean isPublic;

    @JsonCreator
    public Device(
            @JsonProperty("id") Long id,
            @JsonProperty("serial") String serial,
            @JsonProperty("marketName") String marketName,
            @JsonProperty("model") String model,
            @JsonProperty("manufacturer") String manufacturer,
            @JsonProperty("platform") String platform,
            @JsonProperty("platformVersion") String platformVersion,
            @JsonProperty("version") String version,
            @JsonProperty("state") String state,
            @JsonProperty("isSimulator") Boolean isSimulator,
            @JsonProperty("isPublic") Boolean isPublic) {
        this.id = id;
        this.serial = serial;
        this.marketName = marketName;
        this.model = model;
        this.manufacturer = manufacturer;
        this.platform = platform;
        this.platformVersion = platformVersion;
        this.version = version;
        this.state = state;
        this.isSimulator = isSimulator;
        this.isPublic = isPublic;
    }

    public Long id() { return id; }
    public String serial() { return serial; }
    public String marketName() { return marketName; }
    public String model() { return model; }
    public String manufacturer() { return manufacturer; }
    public String platform() { return platform; }
    public String platformVersion() { return platformVersion; }
    public String version() { return version; }
    public String state() { return state; }

    public Boolean isSimulator() {
        return isSimulator;
    }

    public Boolean isPublic() {
        return isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device that = (Device) o;
        return Objects.equals(id, that.id) && Objects.equals(serial, that.serial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serial);
    }

    @Override
    public String toString() {
        return "Device(id=" + id + ", serial=" + serial
                + ", model=" + model + ", platform=" + platform + " " + platformVersion
                + ", state=" + state + ", isSimulator=" + isSimulator + ", isPublic=" + isPublic + ")";
    }
}

