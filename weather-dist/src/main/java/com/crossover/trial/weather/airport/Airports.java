package com.crossover.trial.weather.airport;

import java.util.stream.Stream;

public interface Airports {
    Stream<AirportData> streamAll();
    AirportData find(String iataCode);
    void delete(AirportData airport);
    AirportData addAirport(String iataCode, double latitude, double longitude);
    void init();
}
