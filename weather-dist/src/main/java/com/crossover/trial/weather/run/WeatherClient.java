package com.crossover.trial.weather.run;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherConfiguration;
import com.crossover.trial.weather.atmosphere.DataPoint;

/**
 * A reference implementation for the weather client. Consumers of the REST API
 * can look at WeatherClient to understand API semantics. This existing client
 * populates the REST endpoint with dummy data useful for testing.
 *
 * @author code test administrator
 */
public class WeatherClient {

    private static final Logger LOGGER = Logger.getLogger(WeatherClient.class.getName());

    /** end point for read queries */
    private final WebTarget query;

    /** end point to supply updates */
    private final WebTarget collect;

    /** don't use System.out directly for writing */
    private final PrintStream printStream;

    public WeatherClient(PrintStream printStream) {
        query = ClientBuilder.newClient().target(WeatherConfiguration.BASE_URL + "query");
        collect = ClientBuilder.newClient().target(WeatherConfiguration.BASE_URL + "collect");
        this.printStream = printStream;
    }

    public void pingCollect() {
        WebTarget path = collect.path("/ping");
        Response response = path.request().get();
        printStream.print("collect.ping: " + response.readEntity(String.class) + "\n");
    }

    public void query(String iata) {
        WebTarget path = query.path("/weather/" + iata + "/0");
        Response response = path.request().get();
        printStream.println("query." + iata + ".0: " + response.readEntity(String.class));
    }

    public void pingQuery() {
        WebTarget path = query.path("/ping");
        Response response = path.request().get();
        printStream.println("query.ping: " + response.readEntity(String.class));
    }

    public void delete(String iata) {
        Response response = collect.path("/airport/"+iata).request().delete();
        printStream.println("delete: " + response.getStatus());
    }
    
    public void collectAirports() {
        Response response = collect.path("/airports").request().get();
        printStream.println("collect.airports: " + response.readEntity(String.class));
    }
    
    public void populate(String pointType, int first, int last, int mean, int median, int count) {
        WebTarget path = collect.path("/weather/BOS/" + pointType);
        DataPoint dp = new DataPoint.Builder().withFirst(first).withLast(last).withMean(mean).withMedian(median)
                .withCount(count).build();
        path.request().post(Entity.entity(dp, MediaType.APPLICATION_JSON));
    }

    public void exit() {
        try {
            collect.path("/exit").request().get();
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, e, e::getMessage);
        }
    }

    public static void main(String[] args) {
        WeatherClient wc = new WeatherClient(System.out);
        wc.pingCollect();
        wc.populate("wind", 0, 10, 6, 4, 20);

        wc.query("BOS");
        wc.query("JFK");
        wc.query("EWR");
        wc.query("LGA");
        wc.query("MMU");

        wc.pingQuery();
        wc.collectAirports();
        wc.delete("BOS");
        wc.collectAirports();
        wc.exit();
        System.out.print("complete");
    }
}
