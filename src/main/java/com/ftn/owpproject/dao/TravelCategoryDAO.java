package com.ftn.owpproject.dao;

import java.util.List;

import com.ftn.owpproject.model.TravelCategory;

public interface TravelCategoryDAO {
	
	public TravelCategory findOne(Long id); 
	
	public List<TravelCategory> findAll(); 
	
	public int save(TravelCategory travelCategory); 
	
	public int update(TravelCategory travelCategory); 
	
	public int delete(Long id); 
	
	public Long getIdByName(String categoryName);
}
