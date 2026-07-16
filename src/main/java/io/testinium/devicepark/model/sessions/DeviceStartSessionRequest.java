package io.testinium.devicepark.model.sessions;

/**
 * Request model sent to start a new Appium session.
 *
 * <p>Fields other than {@code allocationId} are optional; however, it is
 * recommended to populate fields like {@code userId}, {@code userEmail},
 * {@code companyId}, {@code companyName} to communicate user/company context completely.</p>
 *
 * <h2>Example</h2>
 * <pre>
 * DeviceStartSessionRequest req = DeviceStartSessionRequest.builder()
 *         .allocationId(allocation.allocationId())
 *         .videoRecording(true)
 *         .userEmail("user@example.com")
 *         .appiumVersion("2.5.0")
 *         .build();
 * </pre>
 *
 * @since 1.0.0
 */
public class DeviceStartSessionRequest {

    private String allocationId;
    private String companyPoolId;
    private String sessionId;
    private Boolean videoRecording = false;
    private Long userId;
    private String userEmail;
    private Long companyId;
    private String companyName;
    private String customVideoRecordingPath;
    private String appiumVersion;

    public String getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(String allocationId) {
        this.allocationId = allocationId;
    }

    public String getCompanyPoolId() {
        return companyPoolId;
    }

    public void setCompanyPoolId(String companyPoolId) {
        this.companyPoolId = companyPoolId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getVideoRecording() {
        return videoRecording;
    }

    public void setVideoRecording(Boolean videoRecording) {
        this.videoRecording = videoRecording != null ? videoRecording : false;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomVideoRecordingPath() {
        return customVideoRecordingPath;
    }

    public void setCustomVideoRecordingPath(String customVideoRecordingPath) {
        this.customVideoRecordingPath = customVideoRecordingPath;
    }

    public String getAppiumVersion() {
        return appiumVersion;
    }

    public void setAppiumVersion(String appiumVersion) {
        this.appiumVersion = appiumVersion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final DeviceStartSessionRequest req = new DeviceStartSessionRequest();

        public Builder allocationId(String allocationId) {
            req.setAllocationId(allocationId);
            return this;
        }

        public Builder companyPoolId(String companyPoolId) {
            req.setCompanyPoolId(companyPoolId);
            return this;
        }

        public Builder sessionId(String sessionId) {
            req.setSessionId(sessionId);
            return this;
        }

        public Builder videoRecording(Boolean videoRecording) {
            req.setVideoRecording(videoRecording);
            return this;
        }

        public Builder userId(Long userId) {
            req.setUserId(userId);
            return this;
        }

        public Builder userEmail(String userEmail) {
            req.setUserEmail(userEmail);
            return this;
        }

        public Builder companyId(Long companyId) {
            req.setCompanyId(companyId);
            return this;
        }

        public Builder companyName(String companyName) {
            req.setCompanyName(companyName);
            return this;
        }

        public Builder customVideoRecordingPath(String customVideoRecordingPath) {
            req.setCustomVideoRecordingPath(customVideoRecordingPath);
            return this;
        }

        public Builder appiumVersion(String appiumVersion) {
            req.setAppiumVersion(appiumVersion);
            return this;
        }

        public DeviceStartSessionRequest build() {
            return req;
        }
    }
}

