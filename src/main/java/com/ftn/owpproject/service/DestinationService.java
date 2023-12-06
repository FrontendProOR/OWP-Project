package com.ftn.owpproject.service;

import java.util.List;

import com.ftn.owpproject.model.Destination;

public interface DestinationService {
	Destination findOne(Long id);
	List<Destination> findAll();
	Destination save(Destination destination);
	Destination update(Destination destination);
	void delete(Long id);
}
