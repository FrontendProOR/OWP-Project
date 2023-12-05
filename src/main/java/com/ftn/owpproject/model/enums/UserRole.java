package com.ftn.owpproject.model.enums;

public enum UserRole {
	PASSENGER("Passenger"),
    ORGANIZER("Organizer"),
    ADMINISTRATOR("Administrator");
	private final String userRole;
	UserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserRole() {
		return userRole;
	}
	
}
