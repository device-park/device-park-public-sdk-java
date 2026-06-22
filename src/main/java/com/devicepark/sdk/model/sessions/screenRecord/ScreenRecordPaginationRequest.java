package com.devicepark.sdk.model.sessions.screenRecord;

import com.devicepark.sdk.model.common.SearchOperation;
import com.devicepark.sdk.model.common.SortDirection;
import com.devicepark.sdk.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code GET /storage/api/v1/public/sessions/{sessionId}/screen-records}
 * isteği için sayfalama, sıralama ve filter parametrelerini taşıyan istek
 * modeli.
 *
 * <h2>Öörnek</h2>
 * <pre>
 * ScreenRecordPaginationRequest req = ScreenRecordPaginationRequest.builder()
 *         .page(0).size(20)
 *         .addFilter(ScreenRecordFilter.CREATED_AT, "2026-01-01", SearchOperation.GREATER_THAN)
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class ScreenRecordPaginationRequest {

    private List<ScreenRecordFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    public List<ScreenRecordFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<ScreenRecordFilterRequest> filters) {
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
        private final ScreenRecordPaginationRequest req = new ScreenRecordPaginationRequest();

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

        public Builder addFilter(ScreenRecordFilter key, Object value, SearchOperation operation) {
            req.filters.add(ScreenRecordFilterRequest.of(key, value, operation));
            return this;
        }

        public Builder filters(List<ScreenRecordFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        public ScreenRecordPaginationRequest build() {
            return req;
        }
    }
}

