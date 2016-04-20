package com.crossover.trial.weather.run;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.HttpServerProbe;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import com.crossover.trial.weather.WeatherConfiguration;
import com.crossover.trial.weather.endpoint.Resources;

/**
 * This main method will be use by the automated functional grader. You
 * shouldn't move this class or remove the main method. You may change the
 * implementation, but we encourage caution.
 *
 * @author code test administrator
 */
public class WeatherServer {

    private WeatherServer() {

    }

    public static void main(String[] args) {
        try {
            System.out.println("Starting Weather App local testing server: " + WeatherConfiguration.BASE_URL);

            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(WeatherConfiguration.BASE_URL),
                    Resources.newResourceConfig(), false);
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
            System.out.println(format("Weather Server started.%n url=%s%n", WeatherConfiguration.BASE_URL));
            WeatherConfiguration.setServerStarted(true);

            // blocks until the process is terminated
            WeatherConfiguration.waitForShutdownRequest();
            server.shutdown();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 
}
