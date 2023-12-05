package com.ftn.owpproject.model.enums;

public enum TransportationType {
	AIRPLANE("Airplane"),
    BUS("Bus"),
    SHIP("Ship");
	private final String transportationType;
	TransportationType(String transportationType) {
		this.transportationType = transportationType;
	}
	public String getTransportationType() {
		return transportationType;
	}
    
}
