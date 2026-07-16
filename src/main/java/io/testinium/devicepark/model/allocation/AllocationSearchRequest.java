package io.testinium.devicepark.model.allocation;

import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request model carrying pagination, sorting, and filter parameters
 * for {@code GET /allocation/api/v2/public/allocations} request.
 *
 * <h2>Example</h2>
 * <pre>
 * AllocationSearchRequest req = AllocationSearchRequest.builder()
 *         .page(0).size(20)
 *         .addFilter(AllocationFilter.ALLOCATION, "abc-123", SearchOperation.EQUAL)
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class AllocationSearchRequest {

    private List<AllocationFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    public List<AllocationFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<AllocationFilterRequest> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
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
        private final AllocationSearchRequest req = new AllocationSearchRequest();

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

        public Builder addFilter(AllocationFilter key, Object value, SearchOperation operation) {
            req.filters.add(AllocationFilterRequest.of(key, value, operation));
            return this;
        }

        public Builder filters(List<AllocationFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        public AllocationSearchRequest build() {
            return req;
        }
    }
}

