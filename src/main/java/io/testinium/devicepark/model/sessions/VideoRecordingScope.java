package io.testinium.devicepark.model.sessions;

/**
 * Controls whether DevicePark manages screen recording for the full session,
 * or leaves start/stop to Appium plugins for the Appium session only.
 *
 * <p>When omitted on session start, the API defaults to {@link #FULL_SESSION}.</p>
 *
 * @since 1.0.0
 */
public enum VideoRecordingScope {

    /**
     * DevicePark starts and stops screen recording for the entire DevicePark session.
     */
    FULL_SESSION,

    /**
     * Appium plugins start and stop screen recording; DevicePark does not manage the recording lifecycle.
     */
    APPIUM_SESSION
}
