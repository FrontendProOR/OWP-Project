package com.ftn.owpproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftn.owpproject.model.Travel;

@Service
public interface TravelService {
	Travel findOne(Long id); 
	List<Travel> findAll(); 
	Travel save(Travel travel); 
	Travel update(Travel travel); 
	Travel delete(Long id); 
	boolean hasReservations(Long travelId);
	double getCurrentPrice(Travel travel);
	int updateAvailableSeats(Long travelId, int availableSeats);
	void updatePrice(Long travelId, double newPrice);
	void updateAllTravelPrices();
}
