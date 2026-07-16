package io.testinium.devicepark.model.sessions;

import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code GET /session/api/v2/public/sessions} isteği için sayfalama, sıralama
 * ve filter parametrelerini taşıyan istek modeli.
 *
 * <h2>Öörnek</h2>
 * <pre>
 * DeviceSessionRequest req = DeviceSessionRequest.builder()
 *         .page(0).size(20)
 *         .addFilter(SessionFilter.STATE, "FINISHED", SearchOperation.EQUAL)
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class DeviceSessionRequest {

    private List<DeviceSessionFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    public List<DeviceSessionFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<DeviceSessionFilterRequest> filters) {
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
        private final DeviceSessionRequest req = new DeviceSessionRequest();

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

        public Builder addFilter(SessionFilter key, Object value, SearchOperation operation) {
            req.filters.add(DeviceSessionFilterRequest.of(key, value, operation));
            return this;
        }

        public Builder filters(List<DeviceSessionFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        public DeviceSessionRequest build() {
            return req;
        }
    }
}

