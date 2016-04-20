package com.crossover.trial.weather.airport;

public class Haversine implements Distance {

    /** earth radius in KM */
    public static final double EARTH_RADIUS = 6372.8;    

    /**
     * Haversine distance between two airports.
     *
     * @param ad1
     *            airport 1
     * @param ad2
     *            airport 2
     * @return the distance in KM
     */
    @Override
    public double between(AirportData ad1, AirportData ad2) {
        double deltaLat = Math.toRadians(ad2.getLatitude() - ad1.getLatitude());
        double deltaLon = Math.toRadians(ad2.getLongitude() - ad1.getLongitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(ad1.getLatitude()) * Math.cos(ad2.getLatitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

}
