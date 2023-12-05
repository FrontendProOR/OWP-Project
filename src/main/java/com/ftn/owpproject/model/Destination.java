package com.ftn.owpproject.model;

public class Destination {
    private int id;
    private String city;
    private String country;
    private String continent;
    private String imageUrl; // Working with images is mandatory

    // Empty Constructor
    public Destination() {
    }

    // Full Constructor without id
    public Destination(String city, String country, String continent, String imageUrl) {
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.imageUrl = imageUrl;
    }

    // Full Constructor with id
    public Destination(int id, String city, String country, String continent, String imageUrl) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
