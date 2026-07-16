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
  <groupId>io.testinium.devicepark</groupId>
    <artifactId>device-park-public-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Quick Start

```java
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.authentication.credentials.Credentials;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.devices.Device;
import io.testinium.devicepark.model.devices.ListDevicesRequest;

// 1. Build the client
DeviceParkApiClient client = DeviceParkApiClient.builder()
        .url("https://dev-devicepark.testinium.io")
        .credentials(Credentials.of("your-client-id", "your-client-secret"))
        .build();

        // 2. List devices (using default pagination)
        PageDto<Device> result = client.devices().list(ListDevicesRequest.builder().build());

System.out.

        println("Total devices : "+result.totalElements());
        System.out.

        println("Total pages   : "+result.totalPages());
        System.out.

        println("Devices       : "+result.data());

// 3. Close the client (release resources)
        client.

        close();
```

> **Tip:** `DeviceParkApiClient` implements `java.io.Closeable`, so you can use it with `try-with-resources`.

```java
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.authentication.credentials.Credentials;
import io.testinium.devicepark.model.common.PageDto;
import io.testinium.devicepark.model.devices.Device;

try(DeviceParkApiClient client = DeviceParkApiClient.builder()
        .url("https://dev-devicepark.testinium.io")
        .credentials(Credentials.of("your-client-id", "your-client-secret"))
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
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.authentication.credentials.Credentials;

DeviceParkApiClient client = DeviceParkApiClient.builder()
        .url("https://dev-devicepark.testinium.io")
        .credentials(Credentials.of("your-client-id", "your-client-secret"))
        .build();
```

### 2. Environment Variables

Define the following environment variables before starting your application:

```bash
export DEVICEPARK_CLIENT_ID=your-client-id
export DEVICEPARK_CLIENT_SECRET=your-client-secret
```


---

## Client Configuration

All configuration options available on `DeviceParkApiClient.builder()`:

| Method                      | Description                     | Default |
|-----------------------------|---------------------------------|---------|
| `.url(String)`              | API base URL                    | —       |
| `.credentials(Credentials)` | OAuth2 Credentials object       | —       |
| `.timeout(int)`             | HTTP request timeout in seconds | `60`    |

```java
import io.testinium.devicepark.authentication.credentials.Credentials;

DeviceParkApiClient client = DeviceParkApiClient.builder()
        .url("https://dev-devicepark.testinium.io")
        .credentials(Credentials.of("your-client-id", "your-client-secret"))
        .timeout(30)
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
import io.testinium.devicepark.model.common.SortDirection;
import io.testinium.devicepark.model.devices.ListDevicesRequest;

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

| Exception                  | Description                                                        |
|----------------------------|--------------------------------------------------------------------|
| `IllegalArgumentException` | Thrown when client is built without providing required credentials |

```java
import io.testinium.devicepark.DeviceParkApiClient;
import io.testinium.devicepark.authentication.credentials.Credentials;

try(DeviceParkApiClient client = DeviceParkApiClient.builder()
        .url("https://dev-devicepark.testinium.io")
        .credentials(Credentials.of("your-client-id", "your-client-secret"))
        .build()) {

    PageDto<Device> result = client.devices().list();
    result.

data().

forEach(System.out::println);

}catch(
IllegalArgumentException e){
        System.err.

println("Configuration error: "+e.getMessage());
} catch (Exception e) {
    System.err.println("Unexpected error: " + e.getMessage());
}
```

---

## License

This project is licensed under the [MIT License](LICENSE).
