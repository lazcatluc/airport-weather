package com.crossover.trial.weather.endpoint;

import org.glassfish.jersey.server.ResourceConfig;

public class Resources {
    
    private Resources() {
        
    }
    
    public static ResourceConfig newResourceConfig() {
        final ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(RestWeatherCollector.class);
        resourceConfig.register(RestWeatherQuery.class);
        return resourceConfig;
    }
    
}
