package com.device.explorer.resource;

import com.device.explorer.dto.DeviceDto;
import com.device.explorer.dto.DeviceRequest;
import com.device.explorer.dto.DeviceTwinProperty;
import com.device.explorer.dto.DeviceUpdateRequest;
import com.device.explorer.service.DeviceService;
import com.google.inject.Inject;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * * @Author Prasenjit Karmakar
 */
@Path("/api/v0/iothub/devices")
public class DeviceExplorerController {
    private final DeviceService deviceService;

    @Inject
    public DeviceExplorerController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @POST
    @Path("/{deviceName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceDto registerDevice(@PathParam("deviceName") String deviceName, @Valid DeviceRequest request) throws IOException, NoSuchAlgorithmException {
        return deviceService.registerDevice(deviceName, request);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeviceDto> getAllDevices() throws IOException, IotHubException {
        return deviceService.getAllDevices();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceDto getDeviceById(@PathParam("id") String id) throws Exception {
        return deviceService.getDeviceById(id);
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updateDeviceById(@PathParam("id") String id, @Valid DeviceUpdateRequest request) throws Exception {
        deviceService.updateDevice(id, request);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeviceDto> searchDevice(@QueryParam("name") String name) throws Exception {
        return deviceService.searchDevice(name);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteDeviceById(@PathParam("id") String id) throws IOException, IotHubException {
        deviceService.deleteDeviceById(id);
    }

    @POST
    @Path("/deviceTwin/{deviceId}/{accessKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DeviceTwinProperty> setDeviceTwin(@PathParam("deviceId") String deviceId, @PathParam("accessKey") String accessKey,
                                                  List<DeviceTwinProperty> twinParams) throws Exception {
        return deviceService.setDeviceTwin(twinParams, accessKey, deviceId);
    }

    @DELETE
    @Path("/deviceTwin/{deviceId}/{accessKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteDeviceTwin(@PathParam("deviceId") String deviceId, @PathParam("accessKey") String accessKey) throws Exception {
        deviceService.deleteDeviceTwin(accessKey, deviceId);
    }

}
