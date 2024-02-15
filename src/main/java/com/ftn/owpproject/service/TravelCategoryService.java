package com.ftn.owpproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ftn.owpproject.model.TravelCategory;
@Service
public interface TravelCategoryService {
	TravelCategory findOne(Long id); 
	
	List<TravelCategory> findAll(); 
	
	TravelCategory save(TravelCategory travelCategory); 
	
	TravelCategory update(TravelCategory travelCategory); 
	
	TravelCategory delete(Long id); 
}
