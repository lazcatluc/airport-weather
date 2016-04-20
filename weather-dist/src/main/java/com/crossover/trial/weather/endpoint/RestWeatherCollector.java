package com.crossover.trial.weather.endpoint;

import java.util.stream.Collectors;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.WeatherConfiguration;
import com.crossover.trial.weather.airport.AirportData;
import com.crossover.trial.weather.airport.Airports;
import com.crossover.trial.weather.atmosphere.AtmosphericInformation;
import com.crossover.trial.weather.atmosphere.DataPoint;
import com.crossover.trial.weather.atmosphere.DataPointType;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollector implements WeatherCollector {

    /** shared gson json to object factory */
    public static final Gson GSON = new Gson();

    private final Airports airports;

    public RestWeatherCollector() {
        this.airports = WeatherConfiguration.AIRPORTS;
    }

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
        updateAtmosphericInformation(airports.find(iataCode).getAtmosphericInformation(), pointType,
                GSON.fromJson(datapointJson, DataPoint.class));

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response getAirports() {
        return Response.status(Response.Status.OK)
                .entity(airports.streamAll().map(AirportData::getIata).collect(Collectors.toSet())).build();
    }

    @Override
    public Response getAirport(String iata) {
        return Response.status(Response.Status.OK).entity(airports.find(iata)).build();
    }

    @Override
    public Response addAirport(String iata, String latString, String longString) {
        airports.addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response deleteAirport(String iata) {
        airports.delete(iata);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response exit() {
        WeatherConfiguration.requestShutdown();
        return Response.noContent().build();
    }

    public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) {
        DataPointType.valueOf(pointType.toUpperCase()).update(ai, dp);
    }

}
