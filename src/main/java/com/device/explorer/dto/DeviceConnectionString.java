package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *@author Prasenjit Karmakar
 */
public class DeviceConnectionString {
    @NotEmpty
    private final String connString;

    @JsonCreator
    public DeviceConnectionString(@JsonProperty("connString") String connString) {
        this.connString = connString;
    }

    public String getConnString() {
        return connString;
    }
}
