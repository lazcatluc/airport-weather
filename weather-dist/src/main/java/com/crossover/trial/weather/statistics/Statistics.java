package com.crossover.trial.weather.statistics;

import java.util.Map;

public interface Statistics {
    int computeDatasize();
    int[] radiusHistogram();
    Map<String, Double> requestFrequecies();
    void init();
    void updateRequestFrequency(String iata, Double radius);
}
