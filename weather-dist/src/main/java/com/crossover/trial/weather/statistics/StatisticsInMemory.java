package com.crossover.trial.weather.statistics;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.crossover.trial.weather.airport.AirportData;
import com.crossover.trial.weather.airport.Airports;
import com.crossover.trial.weather.atmosphere.AtmosphericInformation;

public class StatisticsInMemory implements Statistics {

    private static final int HISTOGRAM_INTERVAL = 10;

    /**
     * Internal performance counter to better understand most requested
     * information, this map can be improved but for now provides the basis for
     * future performance optimizations. Due to the stateless deployment
     * architecture we don't want to write this to disk, but will pull it off
     * using a REST request and aggregate with other performance metrics
     * {@link #ping()}
     */
    private static final Map<AirportData, Integer> REQUEST_FREQUENCY = new ConcurrentHashMap<AirportData, Integer>();

    private static final Map<Double, Integer> RADIUS_FREQUENCIES = new ConcurrentHashMap<Double, Integer>();
    
    private final Airports airports;
    
    public StatisticsInMemory(Airports airports) {
        this.airports = airports;
    }
    
    @Override
    public int[] radiusHistogram() {
        int radiusFrequencySize = maximumRadiusFrequency() + 1;

        int[] histogram = new int[radiusFrequencySize];
        RADIUS_FREQUENCIES.entrySet().forEach(entry -> increaseHistogramEntry(histogram, entry));

        return histogram;
    }

    protected void increaseHistogramEntry(int[] histogram, Entry<Double, Integer> entry) {
        histogram[entry.getKey().intValue() % HISTOGRAM_INTERVAL] += entry.getValue();
    }

    protected int maximumRadiusFrequency() {
        return RADIUS_FREQUENCIES.keySet().stream().max(Double::compare).orElse(1000.0).intValue();
    }    

    @Override
    public Map<String, Double> requestFrequecies() {
        int size = REQUEST_FREQUENCY.size();
        return airports.streamAll().collect(Collectors.toMap(AirportData::getIata,
                data -> size == 0 ? 0 : REQUEST_FREQUENCY.getOrDefault(data, 0).doubleValue() / size));
    }
    
    @Override
    public int computeDatasize() {
        return (int) airports.streamAll().map(AirportData::getAtmosphericInformation)
                .filter(AtmosphericInformation::hasInformation).filter(AtmosphericInformation::isUpdatedInTheLastDay)
                .count();
    }    
    
    /**
     * Records information about how often requests are made
     *
     * @param iata
     *            an iata code
     * @param radius
     *            query radius
     */
    @Override
    public void updateRequestFrequency(String iata, Double radius) {
        AirportData airportData = airports.find(iata);
        REQUEST_FREQUENCY.merge(airportData, 1, Integer::sum);
        RADIUS_FREQUENCIES.merge(radius, 1, Integer::sum);
    }    

    @Override
    public void init() {
        REQUEST_FREQUENCY.clear();
        RADIUS_FREQUENCIES.clear();
    }
}
