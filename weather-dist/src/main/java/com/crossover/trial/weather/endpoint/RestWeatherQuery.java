package com.crossover.trial.weather.endpoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.crossover.trial.weather.WeatherConfiguration;
import com.crossover.trial.weather.airport.AirportData;
import com.crossover.trial.weather.airport.Airports;
import com.crossover.trial.weather.airport.Distance;
import com.crossover.trial.weather.atmosphere.AtmosphericInformation;
import com.crossover.trial.weather.statistics.Statistics;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQuery implements WeatherQuery {

    /** shared gson json to object factory */
    public static final Gson GSON = new Gson();

    private final Airports airports;
    private final Statistics statistics;
    private final Distance distance;
    
    public RestWeatherQuery() {
        airports = WeatherConfiguration.AIRPORTS;
        statistics = WeatherConfiguration.STATISTICS;
        distance = WeatherConfiguration.DISTANCE_CALCULATOR;
    }

    /**
     * Retrieve service health including total size of valid data points and
     * request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();

        retval.put("datasize", statistics.computeDatasize());
        retval.put("iata_freq", statistics.requestFrequecies());
        retval.put("radius_freq", statistics.radiusHistogram());

        return GSON.toJson(retval);
    }

   

    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the
     * requested airport information and return a list of matching atmosphere
     * information.
     *
     * @param iata
     *            the iataCode
     * @param radiusString
     *            the radius in km
     *
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iata, String radiusString) {
        double radius = StringUtils.trimToEmpty(radiusString).isEmpty() ? 0 : Double.valueOf(radiusString);
        statistics.updateRequestFrequency(iata, radius);

        AirportData centerAirport = airports.find(iata);
        List<AtmosphericInformation> retval = Double.doubleToRawLongBits(radius) == 0L
                ? Collections.singletonList(centerAirport.getAtmosphericInformation())
                : airports.streamAll().filter(airportData -> distance.between(centerAirport, airportData) <= radius)
                        .map(AirportData::getAtmosphericInformation)
                        .filter(AtmosphericInformation::hasInformation)
                        .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(retval).build();
    }



}
