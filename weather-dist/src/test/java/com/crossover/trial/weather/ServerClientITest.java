package com.crossover.trial.weather;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.crossover.trial.weather.WeatherClient;
import com.crossover.trial.weather.WeatherServer;
import com.crossover.trial.weather.configuration.WeatherConfiguration;

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
			"query.BOS.0: [{\"temperature\":null,\"wind\":{\"mean\":6.0,\"first\":0,\"second\":4,\"third\":10,\"count\":20},\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/JFK/0\n"+
			"query.JFK.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/EWR/0\n"+
			"query.EWR.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/LGA/0\n"+
			"query.LGA.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/weather/MMU/0\n"+
			"query.MMU.0: [{\"temperature\":null,\"wind\":null,\"humidity\":null,\"precipitation\":null,\"pressure\":null,\"cloudCover\":null}]\n"+
			"/query/ping\n"+
			"query.ping: {\"iata_freq\":{\"EWR\":0.2,\"MMU\":0.2,\"LGA\":0.2,\"BOS\":0.2,\"JFK\":0.2},\"radius_freq\":[5],\"datasize\":1}\n"+
            "/collect/airports\n"+
            "collect.airports: [\"EWR\",\"MMU\",\"BOS\",\"LGA\",\"JFK\"]\n"+
            "/collect/airport/BOS\n"+
            "delete: 200\n"+
            "/collect/airports\n"+
			"collect.airports: [\"EWR\",\"MMU\",\"LGA\",\"JFK\"]\n"+
			"/collect/exit\n"+
			"complete";	

	@Test
	public void clientCallsServer() throws InterruptedException {
	    WeatherConfiguration.init();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		Thread server = new Thread(() -> WeatherServer.main(new String[]{}));
		server.start();
		while (!WeatherConfiguration.isServerStarted()) {
			Thread.sleep(1000);
		}
		Thread client = new Thread(() -> WeatherClient.main(new String[]{}));
		client.start();
		server.join();
		client.join();

		assertThat(baos.toString().replaceAll("\r?\n","\n")).isEqualTo(EXPECTED_OUTPUT);
	}

}
