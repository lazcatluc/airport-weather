package com.crossover.trial.weather.airport;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.*;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /** end point for read queries */
    private final WebTarget query;

    /** end point to supply updates */
    private final WebTarget collect;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        query = client.target("http://localhost:8080/query");
        collect = client.target("http://localhost:8080/collect");
    }

    public void upload(File file) throws IOException {
        try (BufferedReader reader = newReader(file)) {
            String l;
            while ((l = reader.readLine()) != null) {
                break;
            }
        }
    }

    protected BufferedReader newReader(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }

    public static void main(String[] args) throws IOException {
        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            throw new FileNotFoundException(airportDataFile + " is not a valid input");
        }

        new AirportLoader().upload(airportDataFile);
    }
}
