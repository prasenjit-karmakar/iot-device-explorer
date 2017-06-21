package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceUpdateRequest {
    private final Boolean enable;

    @JsonCreator
    public DeviceUpdateRequest(@JsonProperty("enable") Boolean enable) {
        this.enable = enable;
    }

    public boolean getEnable() {
        return enable;
    }
}
