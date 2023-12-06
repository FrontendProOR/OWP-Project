package com.ftn.owpproject.model.enums;

public enum UserRole {
	PASSENGER("Passenger"),
    ORGANIZER("Organizer"),
    ADMINISTRATOR("Administrator"), 
    USER("User");
	private final String userRole;
	UserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserRole() {
		return userRole;
	}
	
}
