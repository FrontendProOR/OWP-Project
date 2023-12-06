package com.ftn.owpproject.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
	public Destination(Long id, String city, String country, String continent) {

		if (id == null) {
			this.id = getNextAvailableId();
		} else {
			this.id = id;
		}

		this.city = city;
		this.country = country;
		this.continent = continent;
	}
	public Destination( String city, String country, String continent) {
		this.id = getNextAvailableId();
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
		result.append(continent);//append(";")
//		result.append(imageUrl);

		return result.toString();
	}

	@Override
	public String toString() {
		return this.city + ", " + this.country + " (" + this.continent + ")";
	}

	public Long getNextAvailableId() {
	    String fileName = "destinations.txt";
	    URL resource = getClass().getResource("/" + fileName);

	    if (resource == null) {
	        System.err.println("File not found: " + fileName);
	        return null;
	    }

	    try {
	        Path path = Paths.get(resource.toURI());
	        List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

	        if (lines.isEmpty()) {
	            // File is empty, start with ID 1
	            return 1L;
	        }

	        Long maxId = Long.parseLong(lines.get(0).trim());
	        Long newId = maxId + 1;
	        return newId;
	    } catch (IOException | URISyntaxException | NumberFormatException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
