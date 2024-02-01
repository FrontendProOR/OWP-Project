package com.ftn.owpproject.model;

import java.util.Date;

import com.ftn.owpproject.model.enums.TransportationType;
import com.ftn.owpproject.model.enums.TypeOfAccommodation;

public class Travel {
    
	private TransportationType transportation;
    private TypeOfAccommodation accommodationUnit;
    private String destinationName;
    private String locationImage; // Path to the image
    private TravelCategory travelCategory;
    private Date departureDateTime;
    private Date returnDateTime;
    private int numberOfNights;
    private double arrangmentPrice;
    private int totalSeats;
    private int availableSeats;

    
	public TypeOfAccommodation getAccommodationUnit() {
		return accommodationUnit;
	}
	public void setAccommodationUnit(TypeOfAccommodation accommodationUnit) {
		this.accommodationUnit = accommodationUnit;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getLocationImage() {
		return locationImage;
	}
	public void setLocationImage(String locationImage) {
		this.locationImage = locationImage;
	}

	public Date getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(Date departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public Date getReturnDateTime() {
		return returnDateTime;
	}
	public void setReturnDateTime(Date returnDateTime) {
		this.returnDateTime = returnDateTime;
	}
	public int getNumberOfNights() {
		return numberOfNights;
	}
	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
	}
	
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public int getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	public TransportationType getTransportation() {
		return transportation;
	}
	public void setTransportation(TransportationType transportation) {
		this.transportation = transportation;
	}
	public double getArrangmentPrice() {
		return arrangmentPrice;
	}
	public void setArrangmentPrice(double arrangmentPrice) {
		this.arrangmentPrice = arrangmentPrice;
	}
	public TravelCategory getTravelCategory() {
		return travelCategory;
	}
	public void setTravelCategory(TravelCategory travelCategory) {
		this.travelCategory = travelCategory;
	}
	
	
}

