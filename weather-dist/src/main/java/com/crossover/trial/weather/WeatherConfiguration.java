package com.crossover.trial.weather;

import com.crossover.trial.weather.airport.Airports;
import com.crossover.trial.weather.airport.AirportsInMemory;
import com.crossover.trial.weather.airport.Distance;
import com.crossover.trial.weather.airport.Haversine;
import com.crossover.trial.weather.statistics.Statistics;
import com.crossover.trial.weather.statistics.StatisticsInMemory;

public class WeatherConfiguration {
    public static final String BASE_URL = "http://localhost:9090/";
    public static final Airports AIRPORTS = new AirportsInMemory();
    public static final Statistics STATISTICS = new StatisticsInMemory(AIRPORTS);
    public static final Distance DISTANCE_CALCULATOR = new Haversine();

    private static volatile boolean serverStarted;
    private static boolean shutdownRequested;

    private WeatherConfiguration() {
        
    }
    
    public static synchronized void init() {
        AIRPORTS.init();
        STATISTICS.init();
        serverStarted = false;
        shutdownRequested = false;
    }

    public static void setServerStarted(boolean serverStarted) {
        WeatherConfiguration.serverStarted = serverStarted;
    }
  
    public static boolean isServerStarted() {
        return serverStarted;
    }

    public static synchronized void requestShutdown() {
        shutdownRequested = true;
        WeatherConfiguration.class.notifyAll();
    }    
    
    public static synchronized void waitForShutdownRequest() throws InterruptedException {
        while (!shutdownRequested) {
            WeatherConfiguration.class.wait(5000);
        }
    }
}
