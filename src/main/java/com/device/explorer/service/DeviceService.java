package com.device.explorer.service;

import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.configuration.IotHubConfiguration;
import com.device.explorer.dto.DeviceDto;
import com.device.explorer.dto.DeviceRequest;
import com.device.explorer.dto.DeviceTwinProperty;
import com.device.explorer.dto.DeviceUpdateRequest;
import com.device.explorer.util.ConnectionParamEnum;
import com.device.explorer.util.Constants;
import com.google.inject.Inject;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.DeviceStatus;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.auth.SymmetricKey;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.devicetwin.Pair;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Prasenjit Karmakar
 */
public class DeviceService {
    private final RegistryManager registryManager;
    private final DeviceExplorerConfiguration deviceExplorerConfiguration;
    private final DeviceTwinService deviceTwinService;

    @Inject
    public DeviceService(DeviceExplorerConfiguration deviceExplorerConfiguration, DeviceTwinService deviceTwinService) throws Exception {
        this.registryManager = RegistryManager.createFromConnectionString(getIotHubConnectionString(deviceExplorerConfiguration.getIotHub()));
        this.deviceExplorerConfiguration = deviceExplorerConfiguration;
        this.deviceTwinService = deviceTwinService;
    }

    public DeviceDto registerDevice(String deviceName, DeviceRequest request) throws IOException, NoSuchAlgorithmException {
        SymmetricKey key = null;
        if (request != null && !StringUtils.isBlank(request.getAccessKey())) {
            //String encodedAccessKey = new String(Base64.encodeBase64(request.getAccessKey().getBytes()));
            key = new SymmetricKey();
            key.setPrimaryKey(request.getAccessKey());
            key.setSecondaryKey(request.getAccessKey());
        }
        Device device = Device.createFromId(deviceName, null, key);
        try {
            device = registryManager.addDevice(device);
        } catch (IotHubException iote) {
            throw new WebApplicationException("Sorry, request cannot be processed currently", HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
        return new DeviceDto(device.getDeviceId(), device.getGenerationId(), device.getStatus().name(),
                device.getStatusUpdatedTime(), device.getConnectionState().name(), device.getConnectionStateUpdatedTime(), device.getLastActivityTime(),
                device.getCloudToDeviceMessageCount(), null, device.getPrimaryKey());
    }

    public List<DeviceDto> getAllDevices() throws IOException, IotHubException {
        final List<DeviceDto> allDevices = registryManager.getDevices(deviceExplorerConfiguration.getMaxShowDeviceCount()).stream().map(d -> new DeviceDto(d.getDeviceId(),
                d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime(),
                d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), null, d.getPrimaryKey())).collect(Collectors.toList());
        return allDevices;
    }

    public DeviceDto getDeviceById(String id) throws Exception {
        DeviceTwin twin = DeviceTwin.createFromConnectionString(getIotHubConnectionString(deviceExplorerConfiguration.getIotHub()));
        DeviceTwinDevice twinDevice = new DeviceTwinDevice(id);
        twin.getTwin(twinDevice);
        Set<Pair> pairs = twinDevice.getReportedProperties();
        List<DeviceTwinProperty> deviceTwin = new ArrayList<>();
        for (Pair pair : pairs) {
            deviceTwin.add(new DeviceTwinProperty(pair.getKey(), String.valueOf(pair.getValue())));
        }
        Device d = registryManager.getDevice(id);
        return new DeviceDto(d.getDeviceId(), d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime()
                , d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), deviceTwin, d.getPrimaryKey());

    }

    public void deleteDeviceById(@PathParam("id") String id) throws IOException, IotHubException {
        registryManager.removeDevice(id);
    }

    public List<DeviceTwinProperty> setDeviceTwin(List<DeviceTwinProperty> twinParams, String accessKey, String deviceId) throws Exception {
        return deviceTwinService.createDeviceTwin(twinParams, accessKey, deviceId);
    }

    public List<DeviceDto> searchDevice(String name) throws IOException, IotHubException {
        List<DeviceDto> searchedDevices = registryManager.getDevices(deviceExplorerConfiguration.getMaxShowDeviceCount()).stream().filter(d -> d.getDeviceId().startsWith(name)).map(d -> new DeviceDto(d.getDeviceId(),
                d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime(),
                d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), null, d.getPrimaryKey())).collect(Collectors.toList());
        return searchedDevices;
    }

    public static String getIotHubConnectionString(IotHubConfiguration configuration) {
        return new StringBuilder(ConnectionParamEnum.HOSTNAME.getParamName()).append(Constants.EQUAL_TO)
                .append(configuration.getHostName()).append(Constants.DELIMITER)
                .append(ConnectionParamEnum.SHAREDACCESSKEYNAME.getParamName()).append(Constants.EQUAL_TO)
                .append(configuration.getSharedAccessKeyName()).append(Constants.DELIMITER)
                .append(ConnectionParamEnum.SHAREDACCESSKEY.getParamName()).append(Constants.EQUAL_TO)
                .append(configuration.getSharedAccessKey()).toString();
    }

    public void deleteDeviceTwin(String accessKey, String deviceId) {
        deviceTwinService.deleteDeviceTwin(accessKey, deviceId);
    }

    public void updateDevice(String deviceId, DeviceUpdateRequest request) throws IOException, IotHubException {
        Device device = registryManager.getDevice(deviceId);
        if (request.getEnable())
            device.setStatus(DeviceStatus.Enabled);
        else
            device.setStatus(DeviceStatus.Disabled);
        registryManager.updateDevice(device);
    }

}
