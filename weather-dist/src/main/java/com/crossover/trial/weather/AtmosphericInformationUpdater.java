package com.crossover.trial.weather;

import java.util.function.BiConsumer;

/**
 * update atmospheric information with the given data point for the given point
 * type
 *
 * @param ai
 *            the atmospheric information object to update
 * @param pointType
 *            the data point type as a string
 * @param dp
 *            the actual data point
 */
public class AtmosphericInformationUpdater {

    private final double minimumMean;
    private final double maximumMean;
    private final BiConsumer<AtmosphericInformation, DataPoint> updater;

    public AtmosphericInformationUpdater(Builder builder) {
        minimumMean = builder.minimumMean;
        maximumMean = builder.maximumMean;
        updater = builder.updater;
    }

    public void updateAtmosphericInformation(AtmosphericInformation ai, DataPoint dp) {
        enforceMeanIsInRange(dp);
        updater.accept(ai, dp);
        ai.setLastUpdateTime(System.currentTimeMillis());
    }

    private void enforceMeanIsInRange(DataPoint dp) {
        if (!isMeanInRange(dp.getMean())) {
            throw new IllegalStateException("couldn't update atmospheric data");
        }
    }

    private boolean isMeanInRange(double mean) {
        return mean >= minimumMean && mean <= maximumMean;
    }

    public static class Builder {
        private double minimumMean = 0d;
        private double maximumMean = 99.99d;
        private BiConsumer<AtmosphericInformation, DataPoint> updater = AtmosphericInformation::setCloudCover;

        public Builder withMeanFrom(double minimumMean) {
            this.minimumMean = minimumMean;
            return this;
        }

        public Builder withMeanUpTo(double maximumMean) {
            this.maximumMean = maximumMean;
            return this;
        }

        public Builder updating(BiConsumer<AtmosphericInformation, DataPoint> updater) {
            this.updater = updater;
            return this;
        }

        public AtmosphericInformationUpdater build() {
            return new AtmosphericInformationUpdater(this);
        }
    }
}