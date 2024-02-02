package com.ftn.owpproject.dao;

import java.util.List;

import com.ftn.owpproject.model.TravelCategory;

public interface TravelCategoryDAO {
	TravelCategory findOne(Long id); 
	
	List<TravelCategory> findAll(); 
	
	TravelCategory save(TravelCategory travelCategory); 
	
	TravelCategory update(TravelCategory travelCategory); 
	
	TravelCategory delete(Long id); 
}
