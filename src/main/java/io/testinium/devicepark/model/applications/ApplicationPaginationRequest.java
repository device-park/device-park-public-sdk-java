package io.testinium.devicepark.model.applications;

import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Request model carrying pagination, sorting, and filter parameters
 * for {@code GET /storage/api/v1/public/applications} request.
 *
 * <h2>Example</h2>
 * <pre>
 * ApplicationPaginationRequest req = ApplicationPaginationRequest.builder()
 *         .page(0).size(20)
 *         .addFilter(ApplicationFilter.VERSION, "1.0.0", SearchOperation.EQUAL)
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class ApplicationPaginationRequest {

    private List<ApplicationFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    /**
     * @return the immutable filter list
     */
    public List<ApplicationFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * @param filters filter list; cleared if {@code null}
     */
    public void setFilters(List<ApplicationFilterRequest> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
    }

    /**
     * @return pagination/sorting parameters
     */
    public Sorting getSorting() {
        return sorting;
    }

    /**
     * @param sorting pagination/sorting; if {@code null}, defaults are used
     */
    public void setSorting(Sorting sorting) {
        this.sorting = sorting != null ? sorting : new Sorting();
    }

    /**
     * @return a new {@link Builder} for fluent usage
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Fluent builder for {@link ApplicationPaginationRequest}.
     */
    public static final class Builder {
        private final ApplicationPaginationRequest req = new ApplicationPaginationRequest();

        /**
         * @param page 0-based page index @return the same builder
         */
        public Builder page(int page) {
            req.sorting.setPage(page);
            return this;
        }

        /**
         * @param size number of elements per page @return the same builder
         */
        public Builder size(int size) {
            req.sorting.setSize(size);
            return this;
        }

        /**
         * @param sortBy sort field @return the same builder
         */
        public Builder sortBy(String sortBy) {
            req.sorting.setSortBy(sortBy);
            return this;
        }

        /**
         * @param direction sort direction @return the same builder
         */
        public Builder direction(SortDirection direction) {
            req.sorting.setDirection(direction);
            return this;
        }

        /**
         * Adds a new filter.
         *
         * @param key       filter field
         * @param value     value to be compared
         * @param operation comparison operator
         * @return the same builder
         */
        public Builder addFilter(ApplicationFilter key, Object value, SearchOperation operation) {
            req.filters.add(ApplicationFilterRequest.of(key, value, operation));
            return this;
        }

        /**
         * @param filters sets all filters together @return the same builder
         */
        public Builder filters(List<ApplicationFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        /**
         * @return the filled request model
         */
        public ApplicationPaginationRequest build() {
            return req;
        }
    }
}
