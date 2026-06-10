package com.devicepark.sdk.management.internal;

import com.devicepark.sdk.core.http.SdkHttpResponse;

import java.util.Map;

/**
 * Shared HTTP executor for Management APIs.
 */
public interface ManagementRequestExecutor {

    SdkHttpResponse get(String path, Map<String, ?> queryParams);
}

