package com.ftn.owpproject.service;

import java.util.List;

import com.ftn.owpproject.model.TravelCategory;

public interface TravelCategoryService {
	TravelCategory findOne(Long id); 
	
	List<TravelCategory> findAll(); 
	
	TravelCategory save(TravelCategory travelCategory); 
	
	TravelCategory update(TravelCategory travelCategory); 
	
	TravelCategory delete(Long id); 
}
