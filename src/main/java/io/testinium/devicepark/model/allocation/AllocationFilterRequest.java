package io.testinium.devicepark.model.allocation;

import io.testinium.devicepark.model.common.SearchOperation;

/**
 * Single filter record for allocation listing.
 *
 * <p>Typically not constructed directly; added via {@link AllocationSearchRequest.Builder#addFilter}.</p>
 *
 * @since 1.0.0
 */
public class AllocationFilterRequest {

    private AllocationFilter key;
    private Object value;
    private SearchOperation operation;

    public AllocationFilter getKey() {
        return key;
    }

    public void setKey(AllocationFilter key) {
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

    public static AllocationFilterRequest of(AllocationFilter key, Object value, SearchOperation operation) {
        AllocationFilterRequest request = new AllocationFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}

