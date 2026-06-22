package com.devicepark.sdk.model.sessions;

import com.devicepark.sdk.model.common.SearchOperation;

/**
 * Session listeleme için tek bir filter kaydı.
 *
 * <p>Genellikle doğrudan kurulmaz; {@link DeviceSessionRequest.Builder#addFilter}
 * üzerinden eklenir.</p>
 *
 * @since 1.0.0
 */
public class DeviceSessionFilterRequest {

    private SessionFilter key;
    private Object value;
    private SearchOperation operation;

    public SessionFilter getKey() {
        return key;
    }

    public void setKey(SessionFilter key) {
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

    public static DeviceSessionFilterRequest of(SessionFilter key, Object value, SearchOperation operation) {
        DeviceSessionFilterRequest request = new DeviceSessionFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}

