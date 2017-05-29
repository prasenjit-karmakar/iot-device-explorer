package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * @Author Prasenjit Karmakar
 */
public class DeviceDto {
    @NotEmpty
    private final String deviceId;
    @NotEmpty
    private final String generationId;
    @NotEmpty
    private final String status;
    @NotEmpty
    private final String statusUpdatedTime;
    @NotEmpty
    private final String connectionState;
    @NotEmpty
    private final String connectionStateUpdatedTime;
    @NotEmpty
    private final String lastActivityTime;
    @NotEmpty
    private final long cloudToDeviceMessageCount;
    @Valid
    private final DeviceTwinInfo deviceTwin;

    @JsonCreator
    public DeviceDto(@JsonProperty("deviceId") String deviceId, @JsonProperty("generationId") String generationId, @JsonProperty("status") String status,
                     @JsonProperty("statusUpdatedTime") String statusUpdatedTime, @JsonProperty("connectionState") String connectionState,
                     @JsonProperty("connectionStateUpdatedTime") String connectionStateUpdatedTime, @JsonProperty("lastActivityTime") String lastActivityTime,
                     @JsonProperty("cloudToDeviceMessageCount") long cloudToDeviceMessageCount, @JsonProperty("deviceTwin") DeviceTwinInfo deviceTwinInfo) {
        this.deviceId = deviceId;
        this.generationId = generationId;
        this.status = status;
        this.statusUpdatedTime = statusUpdatedTime;
        this.connectionState = connectionState;
        this.connectionStateUpdatedTime = connectionStateUpdatedTime;
        this.lastActivityTime = lastActivityTime;
        this.cloudToDeviceMessageCount = cloudToDeviceMessageCount;
        this.deviceTwin = deviceTwinInfo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getGenerationId() {
        return generationId;
    }

    public String getStatus() {
        return status;
    }


    public String getStatusUpdatedTime() {
        return statusUpdatedTime;
    }

    public String getConnectionState() {
        return connectionState;
    }

    public String getConnectionStateUpdatedTime() {
        return connectionStateUpdatedTime;
    }

    public String getLastActivityTime() {
        return lastActivityTime;
    }

    public long getCloudToDeviceMessageCount() {
        return cloudToDeviceMessageCount;
    }

    public DeviceTwinInfo getDeviceTwin() {
        return deviceTwin;
    }
}
