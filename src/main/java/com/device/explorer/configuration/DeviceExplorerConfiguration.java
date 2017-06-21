package com.device.explorer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @Author Prasenjit Karmakar
 */
public class DeviceExplorerConfiguration extends Configuration {

    @NotEmpty
    private final String defaultName;

    @NotNull
    private final IotHubConfiguration iotHub;


    private final int maxShowDeviceCount;

    public DeviceExplorerConfiguration(@JsonProperty("defaultName") String defaultName,
                                       @JsonProperty("iotHub") IotHubConfiguration iotHub,
                                       @JsonProperty("maxShowDeviceCount") int maxShowDeviceCount) {
        this.defaultName = defaultName;
        this.iotHub = iotHub;
        this.maxShowDeviceCount = maxShowDeviceCount;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public IotHubConfiguration getIotHub() {
        return iotHub;
    }

    public int getMaxShowDeviceCount() {
        return maxShowDeviceCount;
    }
}
