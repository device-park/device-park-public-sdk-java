package io.testinium.devicepark.model.sessions.screenRecord;

import io.testinium.devicepark.model.common.SearchOperation;

/**
 * Single filter record for screen-record listing.
 *
 * <p>Typically not constructed directly; added via {@link ScreenRecordPaginationRequest.Builder#addFilter}.</p>
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

