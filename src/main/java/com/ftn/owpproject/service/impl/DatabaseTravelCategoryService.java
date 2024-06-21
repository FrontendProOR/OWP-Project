package com.ftn.owpproject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftn.owpproject.dao.TravelCategoryDAO;
import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.model.enums.TravelCategoryEnum;
import com.ftn.owpproject.service.TravelCategoryService;

@Service
public class DatabaseTravelCategoryService implements TravelCategoryService {
    @Autowired
    private TravelCategoryDAO travelCategoryDAO;
    
    @Override
    public TravelCategory findOne(Long id) {
        return travelCategoryDAO.findOne(id);
    }

    @Override
    public List<TravelCategory> findAll() {
        return travelCategoryDAO.findAll();
    }

    @Override
    public TravelCategory save(TravelCategory travelCategory) {
        travelCategoryDAO.save(travelCategory);
        return travelCategory;
    }

    @Override
    public TravelCategory update(TravelCategory travelCategory) {
        travelCategoryDAO.update(travelCategory);
        return travelCategory;
    }

    @Override
    public TravelCategory delete(Long id) {
        TravelCategory travelCategory = travelCategoryDAO.findOne(id);
        if(travelCategory != null) {
            travelCategoryDAO.delete(id);
        }
        return travelCategory;
    }

    @Override
    public Long getIdByName(String categoryName) {
        return travelCategoryDAO.getIdByName(categoryName);
    }
    
    @Override
    public TravelCategory findByCategoryName(TravelCategoryEnum categoryName) {
        return travelCategoryDAO.findByCategoryName(categoryName);
    }
}
