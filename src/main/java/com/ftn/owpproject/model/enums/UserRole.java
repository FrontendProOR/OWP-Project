package com.ftn.owpproject.model.enums;

public enum UserRole {
	MANAGER("Manager"),
    BUYER("Buyer");
	private final String userRole;
	UserRole(String userRole) {
		this.userRole = userRole;
	}
	public String getUserRole() {
		return userRole;
	}
	
}
