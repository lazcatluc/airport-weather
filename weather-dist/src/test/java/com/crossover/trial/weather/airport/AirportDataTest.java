package com.crossover.trial.weather.airport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AirportDataTest {

    @Test
    public void airportDataIsNotEqualToNull() {
        assertThat(new AirportData().equals(null)).isFalse();
    }

    @Test
    public void airportDataIsEqualToOneWithTheSameIataCode() {
        AirportData airportData1 = new AirportData();
        AirportData airportData2 = new AirportData();
        airportData1.setIata("iata");
        airportData2.setIata("iata");
        
        assertThat(airportData1).isEqualTo(airportData2);
    }
}
