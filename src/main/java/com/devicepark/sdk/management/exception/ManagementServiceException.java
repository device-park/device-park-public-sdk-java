package com.devicepark.sdk.management.exception;

import com.devicepark.sdk.core.exception.SdkServiceException;

/**
 * Management API'sinin servis tarafından dönen 4xx/5xx hatalarını temsil eder
 * (auth hataları hariç — onlar {@link com.devicepark.sdk.auth.exception.AuthenticationException}'a maplenir).
 */
public class ManagementServiceException extends SdkServiceException {

    public ManagementServiceException(int statusCode, String message) {
        super(message, statusCode, null, null);
    }
}

