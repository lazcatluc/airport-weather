package com.crossover.trial.weather;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class ServerClientITest {
	
	private static final String EXPECTED_OUTPUT = 
			"Starting Weather App local testing server: http://localhost:9090/\n"+
			"Weather Server started.\n"+
			" url=http://localhost:9090/\n"+
			"\n"+
			"/collect/ping\n"+
			"collect.ping: ready\n"+
			"/collect/weather/BOS/wind\n"+
			"/query/weather/BOS/0\n"+
			"query.BOS.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/JFK/0\n"+
			"query.JFK.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/EWR/0\n"+
			"query.EWR.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/LGA/0\n"+
			"query.LGA.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/MMU/0\n"+
			"query.MMU.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/ping\n"+
			"query.ping: {\"iata_freq\":{\"EWR\":0.2,\"MMU\":0.2,\"BOS\":0.2,\"LGA\":0.2,\"JFK\":0.2},\"radius_freq\":[0],\"datasize\":0}\n"+
			"/collect/exit\n"+
			"complete";	

	@Test
	public void clientCallsServer() throws InterruptedException {
		RestWeatherQueryEndpoint.init();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Thread server = new Thread(() -> WeatherServer.main(new String[]{}));
		server.start();
		while (!WeatherServer.isServerStarted()) {
			Thread.sleep(1000);
		}
		Thread client = new Thread(() -> WeatherClient.main(new String[]{}));
		client.start();
		server.join();
		client.join();

		assertThat(baos.toString()).isEqualTo(EXPECTED_OUTPUT);
	}

}
