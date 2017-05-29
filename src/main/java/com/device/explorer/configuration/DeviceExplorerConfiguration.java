package com.device.explorer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 * @Author Prasenjit Karmakar
 */
public class DeviceExplorerConfiguration extends Configuration {

    @NotEmpty
    private String defaultName;

    @NotEmpty
    private String iotHubconnectionString;

    @Min(10)
    private int maxShowDeviceCount;

    public String getIotHubconnectionString() {
        return iotHubconnectionString;
    }

    public void setIotHubconnectionString(String iotHubconnectionString) {
        this.iotHubconnectionString = iotHubconnectionString;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public int getMaxShowDeviceCount() {
        return maxShowDeviceCount;
    }

    public void setMaxShowDeviceCount(int maxShowDeviceCount) {
        this.maxShowDeviceCount = maxShowDeviceCount;
    }
}
