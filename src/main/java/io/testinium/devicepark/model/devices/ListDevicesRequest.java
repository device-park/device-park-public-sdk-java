package io.testinium.devicepark.model.devices;

import io.testinium.devicepark.model.common.SearchOperation;
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code GET /management/api/v1/public/devices} isteği için parametre objesi.
 *
 * <p>Server tarafındaki {@code DevicePaginationRequest} ile birebir eşleşir.
 * Spring controller {@code @ModelAttribute} ile bind eder, bu yüzden
 * setter'lı POJO yapısında tutulur.</p>
 *
 * <pre>
 * var req = ListDevicesRequest.builder()
 *         .page(0).size(50)
 *         .sortBy("ID").direction(SortDirection.DESC)
 *         .build();
 * </pre>
 */
public class ListDevicesRequest {

    private List<DeviceFilterRequest> filters = new ArrayList<>();
    private Sorting sorting = new Sorting();

    public List<DeviceFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public void setFilters(List<DeviceFilterRequest> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
    }

    public Sorting getSorting() { return sorting; }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting != null ? sorting : new Sorting();
    }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final ListDevicesRequest req = new ListDevicesRequest();

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

        public Builder addFilter(DeviceFilter key, Object value, SearchOperation operation) {
            req.filters.add(DeviceFilterRequest.of(key, value, operation));
            return this;
        }

        public Builder filters(List<DeviceFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        public ListDevicesRequest build() { return req; }
    }
}
