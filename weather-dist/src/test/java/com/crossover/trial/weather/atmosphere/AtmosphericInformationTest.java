package com.crossover.trial.weather.atmosphere;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class AtmosphericInformationTest {

    @Test
    public void isNotUpdatedInTheLastDayWhenUpdatedIn1970() {
        AtmosphericInformation ai = new AtmosphericInformation();
        ai.setLastUpdateTime(0);
        
        assertThat(ai.isUpdatedInTheLastDay()).isFalse();
    }

}
