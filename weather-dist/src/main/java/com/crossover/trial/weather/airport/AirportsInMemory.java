package com.crossover.trial.weather.airport;

import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jetty.util.ConcurrentHashSet;

import com.crossover.trial.weather.atmosphere.AtmosphericInformation;

public class AirportsInMemory implements Airports {

    /** all known airports */
    private static final Set<AirportData> AIRPORT_DATA = new ConcurrentHashSet<>();
    
    @Override
    public Stream<AirportData> streamAll() {
        return AIRPORT_DATA.stream();
    }

    @Override
    public AirportData find(String iataCode) {
        return AIRPORT_DATA.stream().filter(ap -> ap.getIata().equals(iataCode)).findFirst().orElse(null);
    }

    @Override
    public void delete(AirportData airport) {
        AIRPORT_DATA.remove(airport);
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
        AIRPORT_DATA.add(ad);
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
