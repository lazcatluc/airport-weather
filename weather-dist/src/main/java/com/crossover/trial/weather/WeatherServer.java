package com.crossover.trial.weather;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.*;

/**
 * This main method will be use by the automated functional grader. You
 * shouldn't move this class or remove the main method. You may change the
 * implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

    private static final String BASE_URL = "http://localhost:9090/";
    private static volatile boolean serverStarted = false;
    private static boolean shutdownRequested = false;

    private WeatherServer() {

    }

    public static void main(String[] args) {
        try {
            System.out.println("Starting Weather App local testing server: " + BASE_URL);

            final ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.register(RestWeatherCollectorEndpoint.class);
            resourceConfig.register(RestWeatherQueryEndpoint.class);

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), resourceConfig, false);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdownNow()));

            HttpServerProbe probe = new HttpServerProbe.Adapter() {
                @Override
                public void onRequestReceiveEvent(HttpServerFilter filter, Connection connection, Request request) {
                    System.out.println(request.getRequestURI());
                }
            };
            server.getServerConfiguration().getMonitoringConfig().getWebServerConfig().addProbes(probe);

            // the autograder waits for this output before running automated
            // tests, please don't remove it
            server.start();
            System.out.println(format("Weather Server started.%n url=%s%n", BASE_URL));
            serverStarted = true;

            // blocks until the process is terminated
            synchronized (WeatherServer.class) {
                while (!shutdownRequested) {
                    WeatherServer.class.wait(5000);
                }
            }
            server.shutdown();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static boolean isServerStarted() {
        return serverStarted;
    }

    public static synchronized void requestShutdown() {
        shutdownRequested = true;
        WeatherServer.class.notifyAll();
    }
}
