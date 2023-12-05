package com.ftn.owpproject.model.enums;

public enum TypeOfAccommodation {
	APARTMENT("Apartment"),
    HOTEL_ROOM_ONLY("Hotel room only"),
    HOTEL_BED_AND_BREAKFAST("Hotel bed and breakfast"),
    HALF_BOARD("Half board");
	private final String typeOfAccomodation;
	TypeOfAccommodation(String typeOfAccomodation) {
		this.typeOfAccomodation = typeOfAccomodation;
	}
	public String getTypeOfAccomodation() {
		return typeOfAccomodation;
	}

	
	
}
