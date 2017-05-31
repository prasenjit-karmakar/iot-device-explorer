package com.device.explorer.resource;

import com.device.explorer.dto.DeviceConnectionString;
import com.device.explorer.dto.DeviceDto;
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
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceDto registerDevice(@PathParam("deviceName") String deviceName) throws IOException, NoSuchAlgorithmException {
        return deviceService.registerDevice(deviceName);
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

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteDeviceById(@PathParam("id") String id) throws IOException, IotHubException {
        deviceService.deleteDeviceById(id);
    }

    @POST
    @Path("/deviceTwin")
    @Produces(MediaType.APPLICATION_JSON)
    public String setDeviceTwin(@Valid DeviceConnectionString deviceConnectionString) throws Exception {
        deviceService.setDeviceTwin(deviceConnectionString);
        return "Success";
    }

}
