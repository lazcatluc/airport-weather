package com.crossover.trial.weather.airport;

import java.util.stream.Stream;

public interface Airports {
    Stream<AirportData> streamAll();
    AirportData find(String iataCode);
    void delete(String iata);
    AirportData addAirport(String iataCode, double latitude, double longitude);
    void init();
}
