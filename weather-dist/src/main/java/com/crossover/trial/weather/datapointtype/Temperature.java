package com.crossover.trial.weather.datapointtype;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;

class Temperature extends AtmosphericInformationUpdater {

	private static final long serialVersionUID = 6184674034702748891L;

	@Override
	protected double getMinimumMean() {
		return -50;
	}

	@Override
	protected double getMaximumMean() {
		return 99;
	}

	@Override
	protected void update(AtmosphericInformation ai, DataPoint dp) {
		ai.setTemperature(dp);
	}
	
}