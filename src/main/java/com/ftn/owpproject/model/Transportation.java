package com.ftn.owpproject.model;

import com.ftn.owpproject.model.enums.TransportationType;

public class Transportation {
    private int id;
    private TransportationType type; // (e.g., airplane, bus, ship, ...)
    private int numberOfSeats;
    private Destination finalDestination; // Updated attribute name
    private String description;

    // Empty Constructor
    public Transportation() {
    }

    // Full Constructor without id
    public Transportation(TransportationType type, int numberOfSeats, Destination finalDestination, String description) {
        this.type = type;
        this.numberOfSeats = numberOfSeats;
        this.finalDestination = finalDestination;
        this.description = description;
    }

    // Full Constructor with id
    public Transportation(int id, TransportationType type, int numberOfSeats, Destination finalDestination, String description) {
        this.id = id;
        this.type = type;
        this.numberOfSeats = numberOfSeats;
        this.finalDestination = finalDestination;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransportationType getType() {
        return type;
    }

    public void setType(TransportationType type) {
        this.type = type;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Destination getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(Destination finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String toFileString() {
        StringBuilder result = new StringBuilder();

        result.append(id).append(";");
        result.append(type).append(";");
        result.append(numberOfSeats).append(";");
        result.append(finalDestination).append(";");
        result.append(description);

        return result.toString();
    }
    
    @Override
    public String toString() {
        return this.type + " - " + this.finalDestination;
    }
}

