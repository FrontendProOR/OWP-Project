package com.ftn.owpproject.dao;

import java.util.List;

import com.ftn.owpproject.model.Travel;

public interface TravelDAO {
	public Travel findOne(Long id);
	public List<Travel> findAll();
	public int save(Travel travel);
	public int update(Travel travel);
	public int delete(Long id);
}
