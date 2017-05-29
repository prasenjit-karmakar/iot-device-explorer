package com.device.explorer;

import com.device.explorer.configuration.CORSResponseFilter;
import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.resource.DeviceExplorerController;
import com.device.explorer.service.DeviceTwinSample;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * * @Author Prasenjit Karmakar
 */
public class DeviceExplorerApplication extends Application<DeviceExplorerConfiguration> {
    public void run(DeviceExplorerConfiguration deviceExplorerConfiguration, Environment environment) throws Exception {
        environment.jersey().register(CORSResponseFilter.class);
        final DeviceExplorerController resource = new DeviceExplorerController(
                deviceExplorerConfiguration,
                new DeviceTwinSample());
        environment.jersey().register(resource);
    }

    public static void main(String[] args) throws Exception {
        new DeviceExplorerApplication().run(args);
    }
}
