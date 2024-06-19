package com.ftn.owpproject.service;

import java.util.List;

import com.ftn.owpproject.model.Travel;

public interface TravelService {
	Travel findOne(Long id); 
	List<Travel> findAll(); 
	Travel save(Travel travel); 
	Travel update(Travel travel); 
	Travel delete(Long id); 
	int updateAvailableSeats(Long travelId, int availableSeats);
}
