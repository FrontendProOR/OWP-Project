package com.ftn.owpproject.model;

import java.time.LocalDateTime;


import com.ftn.owpproject.model.enums.TransportationType;
import com.ftn.owpproject.model.enums.TypeOfAccommodation;

public class Travel {

	private Long id;
	private TransportationType transportationType;
	private TypeOfAccommodation accommodationType;
	private String destinationName;
	private String locationImage; 
	private TravelCategory travelCategory;
	private LocalDateTime departureDateTime;
	private LocalDateTime returnDateTime;
	private int numberOfNights;
	private double arrangmentPrice;
	private int totalSeats;
	private int availableSeats;
	
	public Travel(TransportationType transportationType, TypeOfAccommodation accommodationType, String destinationName,
            String locationImage, TravelCategory travelCategory, LocalDateTime departureDateTime, LocalDateTime returnDateTime,
            double arrangmentPrice, int totalSeats, int availableSeats) {
        super();
        this.transportationType = transportationType;
        this.accommodationType = accommodationType;
        this.destinationName = destinationName;
        this.locationImage = locationImage;
        this.travelCategory = travelCategory;
        this.departureDateTime = departureDateTime;
        this.returnDateTime = returnDateTime;
        this.numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(departureDateTime, returnDateTime);
        this.arrangmentPrice = arrangmentPrice;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }
	
    public Travel(TransportationType transportationType, TypeOfAccommodation accommodationType, String destinationName,
			String locationImage, TravelCategory travelCategory, LocalDateTime departureDateTime, LocalDateTime returnDateTime,
			int numberOfNights, double arrangmentPrice, int totalSeats, int availableSeats) {
		super();
		this.transportationType = transportationType;
		this.accommodationType = accommodationType;
		this.destinationName = destinationName;
		this.locationImage = locationImage;
		this.travelCategory = travelCategory;
		this.departureDateTime = departureDateTime;
		this.returnDateTime = returnDateTime;
		this.numberOfNights = numberOfNights;
		this.arrangmentPrice = arrangmentPrice;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}
    
	public Travel(Long id, TransportationType transportationType, TypeOfAccommodation accommodationType,
			String destinationName, String locationImage, TravelCategory travelCategory, LocalDateTime departureDateTime,
			LocalDateTime returnDateTime, int numberOfNights, double arrangmentPrice, int totalSeats, int availableSeats) {
		super();
		this.id = id;
		this.transportationType = transportationType;
		this.accommodationType = accommodationType;
		this.destinationName = destinationName;
		this.locationImage = locationImage;
		this.travelCategory = travelCategory;
		this.departureDateTime = departureDateTime;
		this.returnDateTime = returnDateTime;
		this.numberOfNights = numberOfNights;
		this.arrangmentPrice = arrangmentPrice;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}
    
	public TypeOfAccommodation getAccommodationType() {
		return accommodationType;
	}
	public void setAccommodationType(TypeOfAccommodation accommodationType) {
		this.accommodationType = accommodationType;
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

	public LocalDateTime getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(LocalDateTime departureDateTime) {
		this.departureDateTime = departureDateTime;
	}
	public LocalDateTime getReturnDateTime() {
		return returnDateTime;
	}
	public void setReturnDateTime(LocalDateTime returnDateTime) {
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
	public TransportationType getTransportationType() {
		return transportationType;
	}
	public void setTransportationType(TransportationType transportationType) {
		this.transportationType = transportationType;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}

