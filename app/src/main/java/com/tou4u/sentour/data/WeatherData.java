package com.tou4u.sentour.data;

public class WeatherData {

	private String skyName;
	private String skyCode;
	private String temperature;
	
	public WeatherData(String skyName, String skyCode, String temperature) {
		this.skyName = skyName;
		this.skyCode = skyCode;
		this.temperature = temperature;
	}

	public String getSkyName() {
		return skyName;
	}

	public String getSkyCode() {
		return skyCode;
	}

	public String getTemperature() {
		return temperature;
	}
	
	
}
