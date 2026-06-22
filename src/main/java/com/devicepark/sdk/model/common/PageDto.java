package com.devicepark.sdk.model.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Sayfalanmış API yanıtları için generic wrapper.
 *
 * <p>Server tarafındaki {@code PageDto<T>} ile birebir eşleşir.</p>
 *
 * <pre>
 * {
 *   "size": 20,
 *   "page": 0,
 *   "totalPages": 1,
 *   "totalElements": 1,
 *   "data": [ ... ]
 * }
 * </pre>
 */
public final class PageDto<T> {

    private final int size;
    private final int page;
    private final int totalPages;
    private final long totalElements;
    private final List<T> data;

    @JsonCreator
    public PageDto(
            @JsonProperty("size") int size,
            @JsonProperty("page") int page,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("totalElements") long totalElements,
            @JsonProperty("data") List<T> data) {
        this.size = size;
        this.page = page;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.data = data != null
                ? Collections.unmodifiableList(new ArrayList<T>(data))
                : Collections.<T>emptyList();
    }

    public int size() { return size; }
    public int page() { return page; }
    public int totalPages() { return totalPages; }
    public long totalElements() { return totalElements; }
    public List<T> data() { return data; }

    /** Bu sayfa son sayfa mı? */
    public boolean isLast() {
        return page >= totalPages - 1;
    }

    /** Bu sayfa boş mu? */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageDto)) return false;
        PageDto<?> that = (PageDto<?>) o;
        return size == that.size && page == that.page && totalPages == that.totalPages
                && totalElements == that.totalElements && data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, page, totalPages, totalElements, data);
    }

    @Override
    public String toString() {
        return "PageDto(page=" + page + "/" + totalPages
                + ", size=" + size + ", totalElements=" + totalElements
                + ", data.size=" + data.size() + ")";
    }
}

