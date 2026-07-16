package io.testinium.devicepark.model.pools;

import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code GET /api/v1/public/pools} istegi icin parametre objesi.
 */
public class ListPoolsRequest {

    private List<PoolFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    public List<PoolFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<PoolFilterRequest> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<PoolFilterRequest>();
    }

    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting != null ? sorting : new Sorting();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final ListPoolsRequest req = new ListPoolsRequest();

        public Builder page(int page) {
            req.sorting.setPage(page);
            return this;
        }

        public Builder size(int size) {
            req.sorting.setSize(size);
            return this;
        }

        public Builder sortBy(String sortBy) {
            req.sorting.setSortBy(sortBy);
            return this;
        }

        public Builder direction(SortDirection direction) {
            req.sorting.setDirection(direction);
            return this;
        }

        public Builder addFilter(PoolFilter key, Object value, SearchOperation operation) {
            req.filters.add(PoolFilterRequest.of(key, value, operation));
            return this;
        }

        public Builder filters(List<PoolFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        public ListPoolsRequest build() {
            return req;
        }
    }
}

