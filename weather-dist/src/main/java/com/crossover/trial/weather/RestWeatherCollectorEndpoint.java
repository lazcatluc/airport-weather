package com.crossover.trial.weather;

import static com.crossover.trial.weather.RestWeatherQueryEndpoint.AIRPORT_DATA;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.ATMOPSHERIC_INFORMATION;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.findAirportData;
import static com.crossover.trial.weather.RestWeatherQueryEndpoint.getAirportDataIdx;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.datapointtype.DataPointType;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport
 * weather collection sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
	
	public static final Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

	/** shared gson json to object factory */
	public static final Gson GSON = new Gson();

	@Override
	public Response ping() {
		return Response.status(Response.Status.OK).entity("ready").build();
	}

	@Override
	public Response updateWeather(@PathParam("iata") String iataCode, @PathParam("pointType") String pointType,
			String datapointJson) {
		
		addDataPoint(iataCode, pointType, GSON.fromJson(datapointJson, DataPoint.class));
		
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response getAirports() {
		Set<String> retval = new HashSet<>();
		for (AirportData ad : AIRPORT_DATA) {
			retval.add(ad.getIata());
		}
		return Response.status(Response.Status.OK).entity(retval).build();
	}

	@Override
	public Response getAirport(@PathParam("iata") String iata) {
		AirportData ad = findAirportData(iata);
		return Response.status(Response.Status.OK).entity(ad).build();
	}

	@Override
	public Response addAirport(@PathParam("iata") String iata, @PathParam("lat") String latString,
			@PathParam("long") String longString) {
		addAirport(iata, Double.valueOf(latString), Double.valueOf(longString));
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response deleteAirport(@PathParam("iata") String iata) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	@Override
	public Response exit() {
		WeatherServer.requestShutdown();
		return Response.noContent().build();
	}
	//
	// Internal support methods
	//

	/**
	 * Update the airports weather data with the collected data.
	 *
	 * @param iataCode
	 *            the 3 letter IATA code
	 * @param pointType
	 *            the point type {@link DataPointType}
	 * @param dp
	 *            a datapoint object holding pointType data
	 *
	 * @throws WeatherException
	 *             if the update can not be completed
	 */
	public void addDataPoint(String iataCode, String pointType, DataPoint dp) {
		int airportDataIdx = getAirportDataIdx(iataCode);
		AtmosphericInformation ai = ATMOPSHERIC_INFORMATION.get(airportDataIdx);
		updateAtmosphericInformation(ai, pointType, dp);
	}

	public void updateAtmosphericInformation(AtmosphericInformation ai, String pointType, DataPoint dp) {
		DataPointType.valueOf(pointType.toUpperCase()).update(ai, dp);
	}

	/**
	 * Add a new known airport to our list.
	 *
	 * @param iataCode
	 *            3 letter code
	 * @param latitude
	 *            in degrees
	 * @param longitude
	 *            in degrees
	 *
	 * @return the added airport
	 */
	public static AirportData addAirport(String iataCode, double latitude, double longitude) {
		AirportData ad = new AirportData();
		AIRPORT_DATA.add(ad);

		AtmosphericInformation ai = new AtmosphericInformation();
		ATMOPSHERIC_INFORMATION.add(ai);
		ad.setIata(iataCode);
		ad.setLatitude(latitude);
		ad.setLatitude(longitude);
		return ad;
	}

}
