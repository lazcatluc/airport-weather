package com.crossover.trial.weather;

import com.google.gson.Gson;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.crossover.trial.weather.RestWeatherCollectorEndpoint.addAirport;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    /** earth radius in KM */
    public static final double EARTH_RADIUS = 6372.8;

    /** shared gson json to object factory */
    public static final Gson GSON = new Gson();

    /** all known airports */
    protected static final List<AirportData> AIRPORT_DATA = new ArrayList<>();

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */
    public static final Map<AirportData, Integer> REQUEST_FREQUENCY = new HashMap<AirportData, Integer>();

    public static final Map<Double, Integer> RADIUS_FREQUENCIES = new HashMap<Double, Integer>();

    static {
        init();
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

        retval.put("datasize", computeDatasize());
        retval.put("iata_freq", computeFrequecy());
        retval.put("radius_freq", computeRadiusFrequency());

        return GSON.toJson(retval);
    }

    protected int[] computeRadiusFrequency() {
        int radiusFrequencySize = maximumRadiusFrequency() + 1;

        int[] histogram = new int[radiusFrequencySize];
        RADIUS_FREQUENCIES.entrySet().forEach(entry -> increaseHistogramEntry(histogram, entry));

        return histogram;
    }

    protected void increaseHistogramEntry(int[] histogram, Entry<Double, Integer> entry) {
        histogram[entry.getKey().intValue() % 10] += entry.getValue();
    }

    protected int maximumRadiusFrequency() {
        return RADIUS_FREQUENCIES.keySet().stream().max(Double::compare).orElse(1000.0).intValue();
    }

    protected Map<String, Double> computeFrequecy() {
        return AIRPORT_DATA.stream().collect(Collectors.toMap(AirportData::getIata,
                data -> (double) REQUEST_FREQUENCY.getOrDefault(data, 0) / REQUEST_FREQUENCY.size()));
    }

    protected int computeDatasize() {
        return (int) AIRPORT_DATA.stream().map(AirportData::getAtmosphericInformation)
                .filter(AtmosphericInformation::hasInformation).filter(AtmosphericInformation::isUpdatedInTheLastDay)
                .count();
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
        updateRequestFrequency(iata, radius);

        AirportData ad = findAirportData(iata);
        List<AtmosphericInformation> retval = Double.doubleToRawLongBits(radius) == 0L
                ? Collections.singletonList(ad.getAtmosphericInformation())
                : AIRPORT_DATA.stream().filter(airportData -> calculateDistance(ad, airportData) <= radius)
                        .map(AirportData::getAtmosphericInformation).filter(AtmosphericInformation::hasInformation)
                        .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(retval).build();
    }

    /**
     * Records information about how often requests are made
     *
     * @param iata
     *            an iata code
     * @param radius
     *            query radius
     */
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = findAirportData(iata);
        REQUEST_FREQUENCY.merge(airportData, 1, Integer::sum);
        RADIUS_FREQUENCIES.merge(radius, 1, Integer::sum);
    }

    /**
     * Given an iataCode find the airport data
     *
     * @param iataCode
     *            as a string
     * @return airport data or null if not found
     */
    public static AirportData findAirportData(String iataCode) {
        return AIRPORT_DATA.stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst().orElse(null);
    }

    /**
     * Haversine distance between two airports.
     *
     * @param ad1
     *            airport 1
     * @param ad2
     *            airport 2
     * @return the distance in KM
     */
    public double calculateDistance(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.latitude - ad1.latitude);
        double deltaLon = Math.toRadians(ad2.longitude - ad1.longitude);
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(ad1.latitude) * Math.cos(ad2.latitude);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    /**
     * A dummy init method that loads hard coded data
     */
    public static void init() {
        AIRPORT_DATA.clear();
        REQUEST_FREQUENCY.clear();
        RADIUS_FREQUENCIES.clear();

        addAirport("BOS", 42.364347, -71.005181);
        addAirport("EWR", 40.6925, -74.168667);
        addAirport("JFK", 40.639751, -73.778925);
        addAirport("LGA", 40.777245, -73.872608);
        addAirport("MMU", 40.79935, -74.4148747);
    }

}
