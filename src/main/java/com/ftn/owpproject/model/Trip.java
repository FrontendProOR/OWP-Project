package com.ftn.owpproject.model;

import java.time.LocalDateTime;

import com.ftn.owpproject.model.enums.TravelCategory;

public class Trip {
    private int tripCode;
    private Destination destination;
    private Transportation transportation;
    private AccommodationUnit accommodationUnit;
    private TravelCategory travelCategory; // (e.g., Skiing, Summer vacation, Last minute, New Year, ...)
    private LocalDateTime departureDateTime;
    private LocalDateTime returnDateTime;
    private int numberOfNights;
    private double tripPrice;

    // Empty Constructor
    public Trip() {
    }

    // Full Constructor without tripCode
    public Trip(Destination destination, Transportation transportation, AccommodationUnit accommodationUnit,
                TravelCategory travelCategory, LocalDateTime departureDateTime, LocalDateTime returnDateTime,
                int numberOfNights, double tripPrice) {
        this.destination = destination;
        this.transportation = transportation;
        this.accommodationUnit = accommodationUnit;
        this.travelCategory = travelCategory;
        this.departureDateTime = departureDateTime;
        this.returnDateTime = returnDateTime;
        this.numberOfNights = numberOfNights;
        this.tripPrice = tripPrice;
    }

    // Full Constructor with tripCode
    public Trip(int tripCode, Destination destination, Transportation transportation, AccommodationUnit accommodationUnit,
                TravelCategory travelCategory, LocalDateTime departureDateTime, LocalDateTime returnDateTime,
                int numberOfNights, double tripPrice) {
        this.tripCode = tripCode;
        this.destination = destination;
        this.transportation = transportation;
        this.accommodationUnit = accommodationUnit;
        this.travelCategory = travelCategory;
        this.departureDateTime = departureDateTime;
        this.returnDateTime = returnDateTime;
        this.numberOfNights = numberOfNights;
        this.tripPrice = tripPrice;
    }

    // Getters and Setters
    public int getTripCode() {
        return tripCode;
    }

    public void setTripCode(int tripCode) {
        this.tripCode = tripCode;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public AccommodationUnit getAccommodationUnit() {
        return accommodationUnit;
    }

    public void setAccommodationUnit(AccommodationUnit accommodationUnit) {
        this.accommodationUnit = accommodationUnit;
    }

    public TravelCategory getTravelCategory() {
        return travelCategory;
    }

    public void setTravelCategory(TravelCategory travelCategory) {
        this.travelCategory = travelCategory;
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

    public double getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(double tripPrice) {
        this.tripPrice = tripPrice;
    }
    
    public String toFileString() {
        StringBuilder result = new StringBuilder();

        result.append(tripCode).append(";");
        result.append(destination).append(";");
        result.append(transportation).append(";");
        result.append(accommodationUnit).append(";");
        result.append(travelCategory).append(";");
        result.append(departureDateTime).append(";");
        result.append(returnDateTime).append(";");
        result.append(numberOfNights).append(";");
        result.append(tripPrice);

        return result.toString();
    }
    
    @Override
    public String toString() {
        return this.tripCode + " - " + this.destination + " (" + this.travelCategory + ")";
    }
}

