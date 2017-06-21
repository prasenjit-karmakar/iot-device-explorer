package com.device.explorer.server;

import com.device.explorer.configuration.DeviceExplorerConfiguration;
import com.device.explorer.configuration.IotHubConfiguration;
import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;

/**
 * @author Prasenjit Karmakar
 */
public class AppModule extends AbstractModule {
    private final DeviceExplorerConfiguration configuration;
    private final Environment environment;

    public AppModule(DeviceExplorerConfiguration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(IotHubConfiguration.class).toInstance(configuration.getIotHub());
        bind(DeviceExplorerConfiguration.class).toInstance(configuration);
    }
}
