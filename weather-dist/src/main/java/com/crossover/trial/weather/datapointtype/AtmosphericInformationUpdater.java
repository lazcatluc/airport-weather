package com.crossover.trial.weather.datapointtype;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;

/**
 * update atmospheric information with the given data point for the given point type
 *
 * @param ai the atmospheric information object to update
 * @param pointType the data point type as a string
 * @param dp the actual data point
 */
public abstract class AtmosphericInformationUpdater {
	protected abstract double getMinimumMean();
	protected abstract double getMaximumMean();
	protected abstract void update(AtmosphericInformation ai, DataPoint dp);
	public final void updateAtmosphericInformation(AtmosphericInformation ai, DataPoint dp) {
		enforceMeanIsInRange(dp);
		update(ai, dp);
		ai.setLastUpdateTime(System.currentTimeMillis());
	}
	
	private void enforceMeanIsInRange(DataPoint dp) {
		if (!isMeanInRange(dp.getMean())) {
			throw new IllegalStateException("couldn't update atmospheric data");
		}
	}
	
	private boolean isMeanInRange(double mean) {
		return mean >= getMinimumMean() && mean <= getMaximumMean();
	}

}