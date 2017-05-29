package com.device.explorer.resource;

import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.dto.DeviceConnectionString;
import com.device.explorer.dto.DeviceDto;
import com.device.explorer.dto.DeviceTwinInfo;
import com.device.explorer.service.DeviceTwinSample;
import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwin;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceTwinDevice;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * * @Author Prasenjit Karmakar
 */
@Path("/api/v0/iothub/devices")
public class DeviceExplorerController {
    private final RegistryManager registryManager;
    private final DeviceExplorerConfiguration deviceExplorerConfiguration;
    private final DeviceTwinSample deviceTwinSample;

    private static final int METHOD_SUCCESS = 200;
    private static final int METHOD_NOT_DEFINED = 404;
    private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
    private DeviceClient client;

    public DeviceExplorerController(DeviceExplorerConfiguration deviceExplorerConfiguration, DeviceTwinSample deviceTwinSample) throws Exception {
        this.deviceExplorerConfiguration = deviceExplorerConfiguration;
        registryManager = RegistryManager.createFromConnectionString(deviceExplorerConfiguration.getIotHubconnectionString());
        this.deviceTwinSample = deviceTwinSample;
    }

    @POST
    @Path("/{deviceName}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceDto registerDevice(@PathParam("deviceName") String deviceName) throws IOException, NoSuchAlgorithmException {
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeviceDto> getAllDevices() throws IOException, IotHubException {
        final List<DeviceDto> allDevices = registryManager.getDevices(deviceExplorerConfiguration.getMaxShowDeviceCount()).stream().map(d -> new DeviceDto(d.getDeviceId(),
                d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime(),
                d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), null)).collect(Collectors.toList());
        return allDevices;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceDto getDeviceById(@PathParam("id") String id) throws Exception {
        DeviceTwin twin = DeviceTwin.createFromConnectionString(deviceExplorerConfiguration.getIotHubconnectionString());
        DeviceTwinDevice twinDevice = new DeviceTwinDevice(id);
        twin.getTwin(twinDevice);
        String properties = twinDevice.reportedPropertiesToString().replace("Reported Properties", "");
        DeviceTwinInfo deviceTwinInfo = new Gson().fromJson(properties, DeviceTwinInfo.class);
        Device d = registryManager.getDevice(id);
        return new DeviceDto(d.getDeviceId(), d.getGenerationId(), d.getStatus().name(), d.getStatusUpdatedTime(), d.getConnectionState().name(), d.getConnectionStateUpdatedTime()
                , d.getLastActivityTime(), d.getCloudToDeviceMessageCount(), deviceTwinInfo);

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteDeviceById(@PathParam("id") String id) throws IOException, IotHubException {
        registryManager.removeDevice(id);
    }

    @POST
    @Path("/deviceTwin")
    @Produces(MediaType.APPLICATION_JSON)
    public String setDeviceTwin(@Valid DeviceConnectionString deviceConnectionString) throws Exception {
        deviceTwinSample.createDeviceTwin(deviceConnectionString.getConnString());
        return "Success";
    }

}
