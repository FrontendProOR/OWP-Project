package com.ftn.owpproject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.dao.TravelDAO;
import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.service.TravelService;
@Service
public class DatabaseTravelService implements TravelService {
	@Autowired
	private TravelDAO travelDAO;
	
//	@SuppressWarnings("unused")
//	@Autowired
//	private TravelCategoryDAO travelCategoryDAO;
	
	@Override
	public Travel findOne(Long id) {
		return travelDAO.findOne(id);
	}

	@Override
	public List<Travel> findAll() {
		return travelDAO.findAll();
	}

	@Override
	public Travel save(Travel travel) {
		travelDAO.save(travel);
		return travel;
	}

	@Override
	public Travel update(Travel travel) {
		travelDAO.update(travel);
		return travel;
	}

	@Override
	public Travel delete(Long id) {
		Travel travel = travelDAO.findOne(id);
		if(travel != null) {
			travelDAO.delete(id);
		}
		return travel;
	}
}
