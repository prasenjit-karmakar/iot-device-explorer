package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceRequest {

    //@Length(min = 16, max = 64)
    private final String accessKey;

    @JsonCreator
    public DeviceRequest(@JsonProperty("accessKey") String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
