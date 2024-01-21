package com.ftn.owpproject.model.enums;

public enum TypeOfAccommodation {
	APARTMENT("Apartment"),
    HOTEL("Hotel");
    
	private final String typeOfAccomodation;
	TypeOfAccommodation(String typeOfAccomodation) {
		this.typeOfAccomodation = typeOfAccomodation;
	}
	public String getTypeOfAccomodation() {
		return typeOfAccomodation;
	}

	
	
}
