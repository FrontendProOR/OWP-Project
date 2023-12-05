package com.ftn.owpproject.model;

public class Transportation {
    private int id;
    private String type; // (e.g., airplane, bus, ship, ...)
    private int numberOfSeats;
    private Destination finalDestination; // Updated attribute name
    private String description;

    // Empty Constructor
    public Transportation() {
    }

    // Full Constructor without id
    public Transportation(String type, int numberOfSeats, Destination finalDestination, String description) {
        this.type = type;
        this.numberOfSeats = numberOfSeats;
        this.finalDestination = finalDestination;
        this.description = description;
    }

    // Full Constructor with id
    public Transportation(int id, String type, int numberOfSeats, Destination finalDestination, String description) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}

