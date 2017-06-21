
package com.device.explorer.service;

import com.device.explorer.configuration.IotHubConfiguration;
import com.device.explorer.dto.DeviceTwinProperty;
import com.device.explorer.util.ConnectionParamEnum;
import com.device.explorer.util.Constants;
import com.google.inject.Inject;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Prasenjit Karmakar
 */

public class DeviceTwinService {
    private final IotHubConfiguration iotHubConfiguration;

    @Inject
    public DeviceTwinService(IotHubConfiguration iotHubConfiguration) {
        this.iotHubConfiguration = iotHubConfiguration;
    }

    protected static class DeviceTwinStatusCallBack implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to device twin operation with status " + status.name());
        }
    }

    public List<DeviceTwinProperty> createDeviceTwin(List<DeviceTwinProperty> twinParams, String accessKey, String deviceId) throws IOException, URISyntaxException {
        IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
        DeviceClient client = new DeviceClient(getDeviceConnectionString(iotHubConfiguration, accessKey, deviceId), protocol);

        if (client == null) {
            System.out.println("Could not create an IoT Hub client.");
            return null;
        }


        Device homeKit = new Device() {
            @Override
            public void PropertyCall(String propertyKey, Object propertyValue, Object context) {
                System.out.println(propertyKey + " changed to " + propertyValue);
            }
        };

        try {
            client.open();
            client.startDeviceTwin(new DeviceTwinStatusCallBack(), null, homeKit, null);

            for (DeviceTwinProperty param : twinParams) {
                if (!StringUtils.isBlank(param.getPropertyName()) && !StringUtils.isBlank(param.getPropertyValue())) {
                    homeKit.setDesiredPropertyCallback(new Property(param.getPropertyName(), null), homeKit, null);
                    homeKit.setReportedProp(new Property(param.getPropertyName(), param.getPropertyValue()));
                }
            }
            client.sendReportedProperties(homeKit.getReportedProp());
            client.subscribeToDesiredProperties(homeKit.getDesiredProp());
        } catch (Exception e) {
            System.out.println("On exception, shutting down \n" + " Cause: " + e.getCause() + " \n" + e.getMessage());
            homeKit.clean();
            client.close();
            System.out.println("Shutting down...");
        }
        homeKit.clean();
        client.close();
        return twinParams;
    }

    /*
    todo
    Need implementation
     */
    public void deleteDeviceTwin(String accessKey, String deviceId) {
    }

    public static String getDeviceConnectionString(IotHubConfiguration configuration, String sharedAccessKey, String deviceId) {
        return new StringBuilder(ConnectionParamEnum.HOSTNAME.getParamName()).append(Constants.EQUAL_TO)
                .append(configuration.getHostName()).append(Constants.DELIMITER)
                .append(ConnectionParamEnum.DEVICEID.getParamName()).append(Constants.EQUAL_TO)
                .append(deviceId).append(Constants.DELIMITER)
                .append(ConnectionParamEnum.SHAREDACCESSKEY.getParamName()).append(Constants.EQUAL_TO)
                .append(sharedAccessKey).toString();
    }
}
