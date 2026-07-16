package io.testinium.devicepark.model.sessions;

import io.testinium.devicepark.model.common.SearchOperation;

/**
 * Single filter record for session listing.
 *
 * <p>Typically not constructed directly; added via {@link DeviceSessionRequest.Builder#addFilter}.</p>
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

