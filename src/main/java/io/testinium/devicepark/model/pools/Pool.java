package io.testinium.devicepark.model.pools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Pool model returned from the API.
 */
public final class Pool {

    private final String id;
    private final String name;

    @JsonCreator
    public Pool(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pool)) return false;
        Pool pool = (Pool) o;
        return Objects.equals(id, pool.id) && Objects.equals(name, pool.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Pool(id=" + id + ", name=" + name + ")";
    }
}

