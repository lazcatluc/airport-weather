package com.crossover.trial.weather.datapointtype;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;

class Wind extends AtmosphericInformationUpdater {

	@Override
	protected double getMinimumMean() {
		return 0;
	}

	@Override
	protected double getMaximumMean() {
		return Double.MAX_VALUE;
	}

	@Override
	protected void update(AtmosphericInformation ai, DataPoint dp) {
		ai.setWind(dp);
	}
	
}