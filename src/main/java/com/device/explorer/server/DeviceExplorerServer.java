package com.device.explorer.server;

import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.setup.Environment;

/**
 * Created by Lenovo on 31-05-2017.
 */
public class DeviceExplorerServer extends DeviceExplorerApplication {
    @Override
    Injector createInjector(DeviceExplorerConfiguration configuration, Environment environment) {
        return Guice.createInjector(new AppModule(configuration, environment));
    }

    public static void main(String[] args) throws Exception {
        new DeviceExplorerServer().run(args);
    }
}
