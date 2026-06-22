package com.devicepark.sdk.model.devices;

import com.devicepark.sdk.model.common.SearchOperation;

/**
 * Cihaz listeleme için tek bir filter kaydı.
 *
 * <p>Genellikle doğrudan kurulmaz; {@link ListDevicesRequest.Builder#addFilter}
 * üzerinden eklenir.</p>
 *
 * @since 1.0.0
 */
public class DeviceFilterRequest {

    private DeviceFilter key;
    private Object value;
    private SearchOperation operation;

    public DeviceFilter getKey() {
        return key;
    }

    public void setKey(DeviceFilter key) {
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

    public static DeviceFilterRequest of(DeviceFilter key, Object value, SearchOperation operation) {
        DeviceFilterRequest request = new DeviceFilterRequest();
        request.setKey(key);
        request.setValue(value);
        request.setOperation(operation);
        return request;
    }
}

