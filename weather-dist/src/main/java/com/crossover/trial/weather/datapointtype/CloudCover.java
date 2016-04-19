package com.crossover.trial.weather.datapointtype;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;

class CloudCover extends AtmosphericInformationUpdater {

	private static final long serialVersionUID = 6862355523262540792L;

	@Override
	protected double getMinimumMean() {
		return 0;
	}

	@Override
	protected double getMaximumMean() {
		return 99;
	}

	@Override
	protected void update(AtmosphericInformation ai, DataPoint dp) {
		ai.setCloudCover(dp);
	}
	
}