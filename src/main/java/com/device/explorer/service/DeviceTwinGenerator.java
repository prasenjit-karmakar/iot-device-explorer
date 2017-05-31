
package com.device.explorer.service;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Device;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Prasenjit Karmakar
 */

public class DeviceTwinGenerator {
    private enum MANUFACTURER {ABC, XYZ}

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    protected static class DeviceTwinStatusCallBack implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to device twin operation with status " + status.name());
        }
    }

    public void createDeviceTwin(String connString) throws IOException, URISyntaxException {
        System.out.println("Starting...Beginning set up");
        IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
        System.out.println("Successfully read input parameters.");
        System.out.format("Using communication protocol %s.\n",
                protocol.name());

        DeviceClient client = new DeviceClient(connString, protocol);

        if (client == null) {
            System.out.println("Could not create an IoT Hub client.");
            return;
        }

        System.out.println("Successfully created an IoT Hub client.");

        Device homeKit = new Device() {
            @Override
            public void PropertyCall(String propertyKey, Object propertyValue, Object context) {
                System.out.println(propertyKey + " changed to " + propertyValue);
            }
        };

        try {
            client.open();

            System.out.println("Opened connection to IoT Hub.");

            client.startDeviceTwin(new DeviceTwinStatusCallBack(), null, homeKit, null);

            System.out.println("Starting to device Twin...");

            homeKit.setDesiredPropertyCallback(new Property("manufacturer", null), homeKit, null);
            homeKit.setDesiredPropertyCallback(new Property("modelNumber", null), homeKit, null);
            homeKit.setDesiredPropertyCallback(new Property("lastServiceDate", null), homeKit, null);
            homeKit.setDesiredPropertyCallback(new Property("nextServiceDate", null), homeKit, null);

            homeKit.setReportedProp(new Property("manufacturer", MANUFACTURER.ABC.name()));
            homeKit.setReportedProp(new Property("modelNumber", "412C-EF"));
            homeKit.setReportedProp(new Property("lastServiceDate", sdf.format(new Date())));
            homeKit.setReportedProp(new Property("nextServiceDate", sdf.format(new Date())));
            client.sendReportedProperties(homeKit.getReportedProp());
            System.out.println("Updating reported properties..");
            client.subscribeToDesiredProperties(homeKit.getDesiredProp());
            System.out.println("Waiting for Desired properties");
        } catch (Exception e) {
            System.out.println("On exception, shutting down \n" + " Cause: " + e.getCause() + " \n" + e.getMessage());
            homeKit.clean();
            client.close();
            System.out.println("Shutting down...");
        }

        homeKit.clean();
        client.close();
        System.out.println("Shutting down...");
    }
}
