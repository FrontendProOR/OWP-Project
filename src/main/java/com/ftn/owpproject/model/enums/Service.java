package com.ftn.owpproject.model.enums;

public enum Service {
    WIFI("Wi-Fi"),
    BATHROOM("Bathroom"),
    TV("TV"),
    AIR_CONDITIONING("Air conditioning");

	private final String serviceName;
	Service(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceName() {
		return serviceName;
	}
    
    
}

    

