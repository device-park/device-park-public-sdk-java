package com.devicepark.sdk.model.sessions.screenRecord;

import com.devicepark.sdk.model.common.SearchOperation;

/**
 * Screen-record listeleme için tek bir filter kaydı.
 *
 * <p>Genellikle doğrudan kurulmaz; {@link ScreenRecordPaginationRequest.Builder#addFilter}
 * üzerinden eklenir.</p>
 *
 * @since 1.0.0
 */
public class ScreenRecordFilterRequest {

    private ScreenRecordFilter key;
    private Object value;
    private SearchOperation operation;

    public ScreenRecordFilter getKey() {
        return key;
    }

    public void setKey(ScreenRecordFilter key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(SearchOperation operation) {
        this.operation = operation;
    }

    public static ScreenRecordFilterRequest of(ScreenRecordFilter key, Object value, SearchOperation operation) {
        ScreenRecordFilterRequest request = new ScreenRecordFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}

