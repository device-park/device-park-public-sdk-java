package com.devicepark.sdk.model.applications;

import com.devicepark.sdk.model.common.SearchOperation;
import com.devicepark.sdk.model.common.SortDirection;
import com.devicepark.sdk.model.common.Sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code GET /storage/api/v1/public/applications} isteği için sayfalama,
 * sıralama ve filter parametrelerini taşıyan istek modeli.
 *
 * <h2>Öörnek</h2>
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
     * @return değiştirilemez filter listesi
     */
    public List<ApplicationFilterRequest> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * @param filters filter listesi; {@code null} ise temizlenir
     */
    public void setFilters(List<ApplicationFilterRequest> filters) {
        this.filters = filters != null ? new ArrayList<>(filters) : new ArrayList<>();
    }

    /**
     * @return sayfalama/sıralama parametreleri
     */
    public Sorting getSorting() {
        return sorting;
    }

    /**
     * @param sorting sayfalama/sıralama; {@code null} ise varsayılan kullanılır
     */
    public void setSorting(Sorting sorting) {
        this.sorting = sorting != null ? sorting : new Sorting();
    }

    /**
     * @return akıcı kullanım için yeni {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@link ApplicationPaginationRequest} için akıcı (fluent) builder.
     */
    public static final class Builder {
        private final ApplicationPaginationRequest req = new ApplicationPaginationRequest();

        /**
         * @param page 0-tabanlı sayfa indeksi @return aynı builder
         */
        public Builder page(int page) {
            req.sorting.setPage(page);
            return this;
        }

        /**
         * @param size sayfa başına eleman sayısı @return aynı builder
         */
        public Builder size(int size) {
            req.sorting.setSize(size);
            return this;
        }

        /**
         * @param sortBy sıralama alanı @return aynı builder
         */
        public Builder sortBy(String sortBy) {
            req.sorting.setSortBy(sortBy);
            return this;
        }

        /**
         * @param direction sıralama yönü @return aynı builder
         */
        public Builder direction(SortDirection direction) {
            req.sorting.setDirection(direction);
            return this;
        }

        /**
         * Yeni bir filter ekler.
         *
         * @param key       filter alanı
         * @param value     karşılaştırılacak değer
         * @param operation karşılaştırma operatörü
         * @return aynı builder
         */
        public Builder addFilter(ApplicationFilter key, Object value, SearchOperation operation) {
            req.filters.add(ApplicationFilterRequest.of(key, value, operation));
            return this;
        }

        /**
         * @param filters tüm filter'ları topluca ayarlar @return aynı builder
         */
        public Builder filters(List<ApplicationFilterRequest> filters) {
            req.setFilters(filters);
            return this;
        }

        /**
         * @return doldurulmuş istek modeli
         */
        public ApplicationPaginationRequest build() {
            return req;
        }
    }
}
