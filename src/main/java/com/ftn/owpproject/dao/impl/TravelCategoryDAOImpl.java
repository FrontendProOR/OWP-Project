package com.ftn.owpproject.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.ftn.owpproject.dao.TravelCategoryDAO;
import com.ftn.owpproject.dao.impl.UserDAOImpl.UserRowCallBackHandler;
import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.model.enums.TravelCategoryEnum;
import com.ftn.owpproject.model.enums.UserRole;
import com.ftn.owpproject.service.TravelCategoryService;

public class TravelCategoryDAOImpl implements TravelCategoryDAO{

	@SuppressWarnings("unused")
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unused")
	public class TravelCategoryRowCallBackHandler implements RowCallbackHandler {
		private Map<Long, TravelCategory> travelCategories = new LinkedHashMap<>();
		
		@Override
		public void processRow(ResultSet resultSet)throws SQLException{
			int index = 1;
			Long id = resultSet.getLong(index++);
			String name = resultSet.getString(index++);
			String description = resultSet.getString(index++);
			TravelCategory travelCategory = travelCategories.get(id);
			TravelCategoryEnum nameEnum = TravelCategoryEnum.valueOf(name);
			if(travelCategory == null) {
				travelCategory = new TravelCategory(id,nameEnum,description);
			}
		}
		
		public List<TravelCategory> getTravelCategories(){
			return new ArrayList<>(travelCategories.values());
		}
	}
	
	@Override
	public TravelCategory findOne(Long id) {
		String sql = "SELECT * FROM TravelCategory WHERE id = ?";

        TravelCategoryRowCallBackHandler rowCallbackHandler = new TravelCategoryRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        return rowCallbackHandler.getTravelCategories().get(0);
	}

	@Override
	public List<TravelCategory> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TravelCategory save(TravelCategory travelCategory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TravelCategory update(TravelCategory travelCategory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TravelCategory delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
