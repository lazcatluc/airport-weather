package com.crossover.trial.weather.airport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AirportDataTest {

    @Test
    public void airportDataIsNotEqualToNull() {
        assertThat(new AirportData().equals(null)).isFalse();
    }

}
