package com.crossover.trial.weather.airport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.crossover.trial.weather.atmosphere.AtmosphericInformation;

public class AirportsInMemory implements Airports {

    /** all known airports */
    private static final Map<String, AirportData> AIRPORT_DATA = new ConcurrentHashMap<>();
    
    @Override
    public Stream<AirportData> streamAll() {
        return AIRPORT_DATA.values().stream();
    }

    @Override
    public AirportData find(String iataCode) {
        return AIRPORT_DATA.get(iataCode);
    }

    @Override
    public void delete(String iata) {
        AIRPORT_DATA.remove(iata);
    }
    
    /**
     * Add a new known airport to our list.
     *
     * @param iataCode
     *            3 letter code
     * @param latitude
     *            in degrees
     * @param longitude
     *            in degrees
     *
     * @return the added airport
     */
    @Override
    public AirportData addAirport(String iataCode, double latitude, double longitude) {
        AirportData ad = new AirportData();
        ad.setIata(iataCode);
        ad.setLatitude(latitude);
        ad.setLongitude(longitude);
        ad.setAtmosphericInformation(new AtmosphericInformation());
        AIRPORT_DATA.put(iataCode, ad);
        return ad;
    }    

    @Override
    public void init() {
        AIRPORT_DATA.clear();
        addAirport("BOS", 42.364347, -71.005181);
        addAirport("EWR", 40.6925, -74.168667);
        addAirport("JFK", 40.639751, -73.778925);
        addAirport("LGA", 40.777245, -73.872608);
        addAirport("MMU", 40.79935, -74.4148747);
    }
}
