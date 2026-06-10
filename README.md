# Device Park Public SDK — Java

Official Java client library for the **Device Park Management API**.  
A lightweight, dependency-minimal SDK written in pure Java 8+ that lets you manage your device inventory programmatically.

---

## 📋 Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Authentication](#authentication)
- [Client Configuration](#client-configuration)
- [API Reference](#api-reference)
  - [Devices](#devices)
- [Model Reference](#model-reference)
- [Error Handling](#error-handling)
- [License](#license)

---

## Requirements

| Dependency | Version |
|------------|---------|
| Java       | 8+      |
| Maven      | 3.6+    |

---

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.devicepark</groupId>
    <artifactId>device-park-public-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Quick Start

```java
import com.devicepark.sdk.management.DeviceParkManagementClient;
import com.devicepark.sdk.model.common.PageDto;
import com.devicepark.sdk.model.devices.Device;
import com.devicepark.sdk.model.devices.ListDevicesRequest;

// 1. Build the client
DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .endpoint("https://dev-devicepark.testinium.io")
        .credentials("your-client-id", "your-client-secret")
        .build();

// 2. List devices (using default pagination)
PageDto<Device> result = client.devices().list(ListDevicesRequest.builder().build());

System.out.println("Total devices : " + result.totalElements());
System.out.println("Total pages   : " + result.totalPages());
System.out.println("Devices       : " + result.data());

// 3. Close the client (release resources)
client.close();
```

> **Tip:** `DeviceParkManagementClient` implements `java.io.Closeable`, so you can use it with `try-with-resources`.

```java
try (DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .endpoint("https://dev-devicepark.testinium.io")
        .credentials("your-client-id", "your-client-secret")
        .build()) {

    PageDto<Device> result = client.devices().list();
    result.data().forEach(System.out::println);
}
```

---

## Authentication

The SDK supports multiple ways to provide credentials. The priority order is as follows:

### 1. Direct Credentials (Recommended)

```java
DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .credentials("your-client-id", "your-client-secret")
        .build();
```

### 2. Environment Variables

Define the following environment variables before starting your application:

```bash
export DEVICEPARK_CLIENT_ID=your-client-id
export DEVICEPARK_CLIENT_SECRET=your-client-secret
```

```java
import com.devicepark.sdk.auth.credentials.EnvironmentVariableCredentialsProvider;

DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .build();
```

### 3. Credentials Profile File

Default location: `~/.devicepark/credentials`

```ini
[default]
devicepark_client_id     = your-client-id
devicepark_client_secret = your-client-secret

[staging]
devicepark_client_id     = staging-client-id
devicepark_client_secret = staging-client-secret
```

```java
import com.devicepark.sdk.auth.credentials.ProfileCredentialsProvider;

// Default profile
DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();

// Specific profile
DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.builder()
                .profileName("staging")
                .build())
        .build();
```

You can also override the credentials file path and profile via environment variables:

```bash
export DEVICEPARK_CREDENTIALS_FILE=/path/to/credentials
export DEVICEPARK_PROFILE=staging
```

---

## Client Configuration

All configuration options available on `DeviceParkManagementClient.builder()`:

| Method                           | Description                                         | Default                                          |
|----------------------------------|-----------------------------------------------------|--------------------------------------------------|
| `.endpoint(String)`              | API base URL                                        | `https://dev-devicepark.testinium.io/management` |
| `.credentials(String, String)`   | Client ID and Client Secret                         | —                                                |
| `.credentialsProvider(...)`      | Custom `DeviceParkCredentialsProvider` implementation | —                                              |
| `.timeout(int)`                  | HTTP request timeout in seconds                     | `60`                                             |
| `.maxRetries(int)`               | Number of retries on failed requests                | `2`                                              |
| `.addHeader(String, String)`     | Custom HTTP header to be sent with every request    | —                                                |
| `.logging(LogConfig)`            | SDK logging configuration                           | Silent (no log output)                           |
| `.httpClient(ApacheHttp5Client)` | Custom Apache HttpClient 5 instance                 | —                                                |

```java
DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .endpoint("https://dev-devicepark.testinium.io")
        .credentials("your-client-id", "your-client-secret")
        .timeout(30)
        .maxRetries(3)
        .addHeader("X-Custom-Header", "custom-value")
        .build();
```

---

## API Reference

### Devices

#### `client.devices().list()`

Fetches the device list using all default values.

```java
PageDto<Device> result = client.devices().list();
```

#### `client.devices().list(ListDevicesRequest)`

Fetches the device list with custom pagination and sorting parameters.

```java
import com.devicepark.sdk.model.common.SortDirection;
import com.devicepark.sdk.model.devices.ListDevicesRequest;

PageDto<Device> result = client.devices().list(
        ListDevicesRequest.builder()
                .page(0)                        // Page number (0-based)
                .size(50)                        // Number of devices per page
                .sortBy("ID")                    // Sort field
                .direction(SortDirection.DESC)   // Sort direction: ASC or DESC
                .build()
);
```

**Default Values:**

| Parameter   | Default |
|-------------|---------|
| `page`      | `0`     |
| `size`      | `20`    |
| `sortBy`    | `"ID"`  |
| `direction` | `DESC`  |

#### Fetching All Devices with Pagination

```java
int page = 0;
PageDto<Device> current;

do {
    current = client.devices().list(
            ListDevicesRequest.builder()
                    .page(page)
                    .size(100)
                    .build()
    );

    current.data().forEach(device ->
            System.out.println(device.id() + " - " + device.marketName())
    );

    page++;
} while (!current.isLast());
```

---

## Model Reference

### `Device`

The device object returned from the API.

| Field               | Type     | Description                 |
|---------------------|----------|-----------------------------|
| `id()`              | `Long`   | Device ID                   |
| `serial()`          | `String` | Serial number               |
| `marketName()`      | `String` | Commercial device name      |
| `model()`           | `String` | Model name                  |
| `manufacturer()`    | `String` | Manufacturer                |
| `platform()`        | `String` | Platform (Android / iOS)    |
| `platformVersion()` | `String` | Platform version            |
| `version()`         | `String` | Software version            |
| `state()`           | `String` | Current state of the device |

### `PageDto<T>`

Generic wrapper for paginated API responses.

| Method             | Type      | Description                        |
|--------------------|-----------|------------------------------------|
| `data()`           | `List<T>` | Records on the current page        |
| `page()`           | `int`     | Current page number                |
| `size()`           | `int`     | Number of records per page         |
| `totalPages()`     | `int`     | Total number of pages              |
| `totalElements()`  | `long`    | Total number of records            |
| `isLast()`         | `boolean` | Is this the last page?             |
| `isEmpty()`        | `boolean` | Is the current page empty?         |

---

## Error Handling

The SDK reports error conditions using the following exception types:

| Exception             | Description                                                       |
|-----------------------|-------------------------------------------------------------------|
| `SdkClientException`  | Client-side errors (invalid configuration, missing credentials)   |
| `IllegalStateException` | Thrown when a client is built without providing credentials     |

```java
import com.devicepark.sdk.core.exception.SdkClientException;

try (DeviceParkManagementClient client = DeviceParkManagementClient.builder()
        .endpoint("https://dev-devicepark.testinium.io")
        .credentials("your-client-id", "your-client-secret")
        .build()) {

    PageDto<Device> result = client.devices().list();
    // ...

} catch (SdkClientException e) {
    System.err.println("SDK error: " + e.getMessage());
} catch (Exception e) {
    System.err.println("Unexpected error: " + e.getMessage());
}
```

---

## License

This project is licensed under the [MIT License](LICENSE).
