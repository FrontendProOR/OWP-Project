package com.ftn.owpproject.model.enums;

public enum TransportationType {
	AIRPLANE("Airplane"),
    BUS("Bus"),
    PERSONAL("Personal transportation");
	private final String transportationType;
	TransportationType(String transportationType) {
		this.transportationType = transportationType;
	}
	public String getTransportationType() {
		return transportationType;
	}
    
}
