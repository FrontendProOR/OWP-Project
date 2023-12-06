package com.ftn.owpproject.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Destination {
    private Long id;
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
    
 // Full Constructor without imgURL
    public Destination(Long id,String city, String country, String continent) {
//        this.id = this.getNextAvailableId();
    	this.id = id;
    	this.city = city;
        this.country = country;
        this.continent = continent;
    }
    
    // Full Constructor with id
    public Destination(Long id, String city, String country, String continent, String imageUrl) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    
    public String toFileString() {
        StringBuilder result = new StringBuilder();

        result.append(id).append(";");
        result.append(city).append(";");
        result.append(country).append(";");
        result.append(continent).append(";");
        result.append(imageUrl);

        return result.toString();
    }
    
    @Override
    public String toString() {
        return this.city + ", " + this.country + " (" + this.continent + ")";
    }
    
    public Long getNextAvailableId() {
        String filePath = "/owpproject/src/main/resources/destinations.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8"));
            Long maxId = Long.parseLong(lines.get(0).trim());
            Long newId = maxId + 1;
            return newId;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
