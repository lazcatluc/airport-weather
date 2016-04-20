package com.crossover.trial.weather.airport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.junit.Test;

public class HaversineTest {

    @Test
    public void computesAGoodAproximationOfTheRealDistance() {
        AirportData start = new AirportData();
        start.setLatitude(44.638);
        start.setLongitude(-63.587);
        
        AirportData finish = new AirportData();
        finish.setLatitude(44.644);
        finish.setLongitude(-63.597);
        
        double expectedDistance = 1.035;
        
        assertThat(new Haversine().between(start, finish)).isCloseTo(expectedDistance, offset(0.0001));
    }

}
