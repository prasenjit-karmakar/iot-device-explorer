package com.device.explorer.server;

import com.device.explorer.configuration.CORSResponseFilter;
import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.resource.DeviceExplorerController;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * * @Author Prasenjit Karmakar
 */
abstract class DeviceExplorerApplication extends Application<DeviceExplorerConfiguration> {
    public void run(DeviceExplorerConfiguration deviceExplorerConfiguration, Environment environment) throws Exception {
        Injector injector = createInjector(deviceExplorerConfiguration, environment);
        environment.jersey().register(CORSResponseFilter.class);
        environment.jersey().register(injector.getInstance(DeviceExplorerController.class));
    }


    abstract Injector createInjector(final DeviceExplorerConfiguration configuration, final Environment environment);

}
