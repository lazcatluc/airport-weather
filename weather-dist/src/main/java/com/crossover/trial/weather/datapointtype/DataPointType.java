package com.crossover.trial.weather.datapointtype;

import com.crossover.trial.weather.AtmosphericInformation;
import com.crossover.trial.weather.DataPoint;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType {
	
    WIND (new Wind()),
    TEMPERATURE (new Temperature()),
    HUMIDTY(new Humidity()),
    PRESSURE(new Pressure()),
    CLOUDCOVER(new CloudCover()),
    PRECIPITATION(new Precipitation());
    
    private final AtmosphericInformationUpdater updater;
	
	private DataPointType(AtmosphericInformationUpdater updater) {
		this.updater = updater;
	}

	public void update(AtmosphericInformation ai, DataPoint dp) {
		updater.updateAtmosphericInformation(ai, dp);
	}
}
