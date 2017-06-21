package com.device.explorer.util;

/**
 * @author Prasenjit Karmakar
 *
 */
public enum ConnectionParamEnum {

    HOSTNAME("HostName"), SHAREDACCESSKEYNAME("SharedAccessKeyName"), SHAREDACCESSKEY("SharedAccessKey"),
    DEVICEID("DeviceId");

    private final String paramName;

    ConnectionParamEnum(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
