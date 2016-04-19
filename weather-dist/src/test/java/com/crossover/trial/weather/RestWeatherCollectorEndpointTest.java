package com.crossover.trial.weather;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RestWeatherCollectorEndpointTest {
	@Mock
	private AtmosphericInformation ai;
	
	private DataPointType pointType;
	
	@Mock
	private DataPoint dp;
	
	private RestWeatherCollectorEndpoint restWeatherCollectorEndpoint;
	
	@Before
	public void setUp() {
		restWeatherCollectorEndpoint = new RestWeatherCollectorEndpoint();
		MockitoAnnotations.initMocks(this);
		pointType = DataPointType.WIND;
	}
	
	private void updateAtmosphericInformation() {
		restWeatherCollectorEndpoint.updateAtmosphericInformation(ai, pointType.name(), dp);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateWindWhenDataPointMeanIsNegative() throws Exception {
		when(dp.getMean()).thenReturn(-1D);
		
		updateAtmosphericInformation();
	}
	
	@Test
	public void updatesTemperatureWhenItIsZeroDegrees() throws Exception {
		pointType = DataPointType.TEMPERATURE;
		when(dp.getMean()).thenReturn(0d);
		
		updateAtmosphericInformation();
		
		verify(ai, times(1)).setTemperature(dp);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateTemperatureWhenBelowMinus50() throws Exception {
		pointType = DataPointType.TEMPERATURE;
		when(dp.getMean()).thenReturn(-51d);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateTemperatureWhenAbove99() throws Exception {
		pointType = DataPointType.TEMPERATURE;
		when(dp.getMean()).thenReturn(100d);
		
		updateAtmosphericInformation();
	}
	
	@Test
	public void updatesHumidityWhenItIsZero() throws Exception {
		pointType = DataPointType.HUMIDTY;
		when(dp.getMean()).thenReturn(0d);
		
		updateAtmosphericInformation();
		
		verify(ai, times(1)).setHumidity(dp);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateHumidityWhenNegative() throws Exception {
		pointType = DataPointType.HUMIDTY;
		when(dp.getMean()).thenReturn(-1d);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateHumidityWhenAbove99() throws Exception {
		pointType = DataPointType.HUMIDTY;
		when(dp.getMean()).thenReturn(100d);
		
		updateAtmosphericInformation();
	}
	
	@Test
	public void updatesPressureWhenItIs650() throws Exception {
		pointType = DataPointType.PRESSURE;
		when(dp.getMean()).thenReturn(650d);
		
		updateAtmosphericInformation();
		
		verify(ai, times(1)).setPressure(dp);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdatePressureWhenBelow650() throws Exception {
		pointType = DataPointType.PRESSURE;
		when(dp.getMean()).thenReturn(649d);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdatePressureWhenAbove799() throws Exception {
		pointType = DataPointType.PRESSURE;
		when(dp.getMean()).thenReturn(800d);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateCloudCoverWhenNegative() throws Exception {
		pointType = DataPointType.CLOUDCOVER;
		when(dp.getMean()).thenReturn(-1D);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdateCloudCoverWhenAbove99() throws Exception {
		pointType = DataPointType.CLOUDCOVER;
		when(dp.getMean()).thenReturn(100d);
		
		updateAtmosphericInformation();
	}
	
	@Test
	public void updatesPrecipitationWhenItIs0() throws Exception {
		pointType = DataPointType.PRECIPITATION;
		when(dp.getMean()).thenReturn(0d);
		
		updateAtmosphericInformation();
		
		verify(ai, times(1)).setPrecipitation(dp);
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdatePressureWhenNegative() throws Exception {
		pointType = DataPointType.PRECIPITATION;
		when(dp.getMean()).thenReturn(-1d);
		
		updateAtmosphericInformation();
	}
	
	@Test(expected = IllegalStateException.class)
	public void doesntUpdatePrecipitationWhenAbove99() throws Exception {
		pointType = DataPointType.PRECIPITATION;
		when(dp.getMean()).thenReturn(100d);
		
		updateAtmosphericInformation();
	}
}
