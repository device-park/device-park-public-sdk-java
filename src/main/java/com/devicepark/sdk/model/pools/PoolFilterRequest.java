package com.devicepark.sdk.model.pools;

import com.devicepark.sdk.model.common.SearchOperation;

/**
 * Pool listeleme icin tek filtre kaydi.
 */
public class PoolFilterRequest {

    private PoolFilter key;
    private Object value;
    private SearchOperation operation;

    public PoolFilter getKey() {
        return key;
    }

    public void setKey(PoolFilter key) {
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

    public static PoolFilterRequest of(PoolFilter key, Object value, SearchOperation operation) {
        PoolFilterRequest request = new PoolFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}

