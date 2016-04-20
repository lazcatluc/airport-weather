package com.crossover.trial.weather.airport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.WeatherConfiguration;
import com.crossover.trial.weather.WeatherServer;

public class AirportLoaderITest {
    
    private Thread server;
    
    @Before
    public void setUp() throws InterruptedException {
        WeatherConfiguration.init();
        server = new Thread(() -> WeatherServer.main(new String[]{}));
        server.start();
        while (!WeatherConfiguration.isServerStarted()) {
            Thread.sleep(1000);
        }
    }
    
    @After
    public void tearDown() throws InterruptedException {
        WeatherConfiguration.requestShutdown();
        server.join();
    }
    
    @Test
    public void uploadsAirportsFromFile() throws Exception {
        AirportLoader.main(new String[] {ClassLoader.getSystemClassLoader()
                .getResource("airports.dat").getPath()});
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsExceptionForWrongFormatFile() throws Exception {
        AirportLoader.main(new String[] {ClassLoader.getSystemClassLoader()
                .getResource("wrong-format.dat").getPath()});
    }
}
