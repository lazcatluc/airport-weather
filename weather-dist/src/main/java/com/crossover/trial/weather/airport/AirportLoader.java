package com.crossover.trial.weather.airport;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import com.crossover.trial.weather.configuration.WeatherConfiguration;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /** end point to supply updates */
    private final WebTarget collect;

    public AirportLoader() {
        collect = ClientBuilder.newClient().target(WeatherConfiguration.BASE_URL + "collect");
    }

    public void upload(File file) throws IOException {
        try (BufferedReader reader = newReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                enforceStatusOk(collect.path(buildAddAirportRequestPath(
                        Arrays.stream(line.split(",")).map(AirportLoader::stripQuotes).toArray(String[]::new)))
                        .request().post(Entity.text("")));
            }
        }
    }

    private void enforceStatusOk(Response response) {
        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("Response status " + response.getStatus());
        }
    }

    protected static String buildAddAirportRequestPath(String[] lineData) {
        String iata = lineData[4];
        if (StringUtils.isEmpty(iata)) {
            iata = "UNKNOWN_"+new DecimalFormat("0000").format(Integer.parseInt(lineData[0]));
        }
        return "/airport/"+iata+"/"+lineData[6]+"/"+lineData[7];
    }

    protected static String stripQuotes(String original) {
        if (original.startsWith("\"") && original.endsWith("\"")) {
            return original.substring(1, original.length() - 1);
        }
        return original;
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
