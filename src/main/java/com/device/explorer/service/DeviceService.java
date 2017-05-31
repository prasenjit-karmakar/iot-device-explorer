package com.device.explorer.service;

import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.dto.DeviceConnectionString;
import com.device.explorer.dto.DeviceDto;
import com.device.explorer.dto.DeviceTwinInfo;
import com.google.inject.Inject;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.devicetwin.Pair;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceService {
    private final RegistryManager registryManager;
    private final DeviceExplorerConfiguration deviceExplorerConfiguration;
    private final DeviceTwinGenerator deviceTwinGenerator;

    @Inject
    public DeviceService(DeviceExplorerConfiguration deviceExplorerConfiguration, DeviceTwinGenerator deviceTwinGenerator) throws Exception {
        this.registryManager = RegistryManager.createFromConnectionString(deviceExplorerConfiguration.getIotHubconnectionString());
        this.deviceExplorerConfiguration = deviceExplorerConfiguration;
        this.deviceTwinGenerator = deviceTwinGenerator;
    }

    public DeviceDto registerDevice(String deviceName) throws IOException, NoSuchAlgorithmException {
        Device device = Device.createFromId(deviceName, null, null);
        try {
            device = registryManager.addDevice(device);
        } catch (IotHubException iote) {
            try {
                device = registryManager.getDevice(deviceName);
            } catch (IotHubException iotf) {
                iotf.printStackTrace();
            }
        }
        return new DeviceDto(device.getDeviceId(), device.getGenerationId(), device.getStatus().name(),
                device.getStatusUpdatedTime(), device.getConnectionState().name(), device.getConnectionStateUpdatedTime(), device.getLastActivityTime(),
                device.getCloudToDeviceMessageCount(), null);
    }

    public List<DeviceDto> getAllDevices() throws IOException, IotHubException {
        final List<DeviceDto> allDevices = registryManager.getDevices(deviceExplorerConfiguration.getMaxShowDeviceCount()).stream().map(d -> new DeviceDto(d.getDeviceId(),
                d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime(),
                d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), null)).collect(Collectors.toList());
        return allDevices;
    }

    public DeviceDto getDeviceById(String id) throws Exception {
        DeviceTwin twin = DeviceTwin.createFromConnectionString(deviceExplorerConfiguration.getIotHubconnectionString());
        DeviceTwinDevice twinDevice = new DeviceTwinDevice(id);
        twin.getTwin(twinDevice);
        Set<Pair> pairs = twinDevice.getReportedProperties();
        DeviceTwinInfo deviceTwinInfo = new DeviceTwinInfo();
        for (Pair pair : pairs) {
            if ("nextServiceDate".equals(pair.getKey()))
                deviceTwinInfo.setNextServiceDate((String) pair.getValue());
            if ("manufacturer".equals(pair.getKey()))
                deviceTwinInfo.setManufacturer((String) pair.getValue());
            if ("lastServiceDate".equals(pair.getKey()))
                deviceTwinInfo.setLastServiceDate((String) pair.getValue());
            if ("modelNumber".equals(pair.getKey()))
                deviceTwinInfo.setModelNumber((String) pair.getValue());
        }

        Device d = registryManager.getDevice(id);
        return new DeviceDto(d.getDeviceId(), d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime()
                , d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), deviceTwinInfo);

    }

    public void deleteDeviceById(@PathParam("id") String id) throws IOException, IotHubException {
        registryManager.removeDevice(id);
    }

    public String setDeviceTwin(@Valid DeviceConnectionString deviceConnectionString) throws Exception {
        deviceTwinGenerator.createDeviceTwin(deviceConnectionString.getConnString());
        return "Success";
    }
}
