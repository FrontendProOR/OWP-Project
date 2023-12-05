package com.ftn.owpproject.model;

import java.util.List;

public class AccommodationUnit {
    private int id;
    private String name;
    private String type; // (e.g., apartment, hotel room only, hotel bed and breakfast, half board, ...)
    private int capacity;
    private Destination destination;
    private List<String> reviews;
    private List<String> services; // (e.g., wi-fi, bathroom, TV, air conditioning, ...)
    private String description;

    // Empty Constructor
    public AccommodationUnit() {
    }

    // Full Constructor without id
    public AccommodationUnit(String name, String type, int capacity, Destination destination,
                             List<String> reviews, List<String> services, String description) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.destination = destination;
        this.reviews = reviews;
        this.services = services;
        this.description = description;
    }

    // Full Constructor with id
    public AccommodationUnit(int id, String name, String type, int capacity, Destination destination,
                             List<String> reviews, List<String> services, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.destination = destination;
        this.reviews = reviews;
        this.services = services;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

