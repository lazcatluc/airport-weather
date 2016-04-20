package com.crossover.trial.weather.airport;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import java.util.function.ToDoubleFunction;

public class Haversine implements Distance {

    /** earth radius in KM */
    public static final double EARTH_RADIUS_KILOMETERS = 6371;    

    /**
     * Haversine distance between two airports.
     *
     * @param from
     *            airport 1
     * @param to
     *            airport 2
     * @return the distance in KM
     */
    @Override
    public double between(AirportData from, AirportData to) {
        return 2 * EARTH_RADIUS_KILOMETERS * asin(sqrt(
                haversine(this::latitudeRadians, from, to) + 
                haversine(this::longitudeRadians, from, to) * cos(latitudeRadians(from)) * cos(latitudeRadians(to))));
    }
    
    protected double latitudeRadians(AirportData airportData) {
        return toRadians(airportData.getLatitude());
    }

    protected double longitudeRadians(AirportData airportData) {
        return toRadians(airportData.getLongitude());
    }
    
    protected double haversine(ToDoubleFunction<AirportData> func, AirportData from, AirportData to) {
        double delta = func.applyAsDouble(to) - func.applyAsDouble(from);
        return pow(sin(delta / 2), 2);
    }

}
