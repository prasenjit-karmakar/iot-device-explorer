package com.device.explorer.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Prasenjit Karmakar
 */
public class IotHubConfiguration {
    @NotEmpty
    private final String hostName;
    @NotEmpty
    private final String sharedAccessKeyName;
    @NotEmpty
    private final String sharedAccessKey;

    public IotHubConfiguration(@JsonProperty("hostName") String hostName,
                               @JsonProperty("sharedAccessKeyName") String sharedAccessKeyName,
                               @JsonProperty("sharedAccessKey") String sharedAccessKey) {
        this.hostName = hostName;
        this.sharedAccessKeyName = sharedAccessKeyName;
        this.sharedAccessKey = sharedAccessKey;
    }


    public String getHostName() {
        return hostName;
    }

    public String getSharedAccessKeyName() {
        return sharedAccessKeyName;
    }

    public String getSharedAccessKey() {
        return sharedAccessKey;
    }
}
