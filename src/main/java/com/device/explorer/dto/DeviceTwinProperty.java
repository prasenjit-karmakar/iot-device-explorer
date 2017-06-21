package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceTwinProperty {
    private final String propertyName;
    private final String propertyValue;

    @JsonCreator
    public DeviceTwinProperty(@JsonProperty("propertyName") String propertyName, @JsonProperty("propertyValue") String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }
}
