package com.devicepark.sdk.model.applications;

import com.devicepark.sdk.model.common.SearchOperation;

/**
 * Application listeleme için tek bir filter kaydı.
 *
 * <p>Genellikle doğrudan kurulmaz; {@link ApplicationPaginationRequest.Builder#addFilter}
 * üzerinden eklenir.</p>
 *
 * @since 1.0.0
 */
public class ApplicationFilterRequest {

    private ApplicationFilter key;
    private Object value;
    private SearchOperation operation;

    /**
     * @return filter alanı
     */
    public ApplicationFilter getKey() {
        return key;
    }

    /**
     * @param key filter alanı
     */
    public void setKey(ApplicationFilter key) {
        this.key = key;
    }

    /**
     * @return karşılaştırılacak değer
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value karşılaştırılacak değer
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return karşılaştırma operatörü
     */
    public SearchOperation getOperation() {
        return operation;
    }

    /**
     * @param operation karşılaştırma operatörü
     */
    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    /**
     * Akıcı (fluent) fabrika.
     *
     * @param key       filter alanı
     * @param value     karşılaştırılacak değer
     * @param operation karşılaştırma operatörü
     * @return doldurulmuş yeni {@code ApplicationFilterRequest}
     */
    public static ApplicationFilterRequest of(ApplicationFilter key, Object value, SearchOperation operation) {
        ApplicationFilterRequest request = new ApplicationFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}
