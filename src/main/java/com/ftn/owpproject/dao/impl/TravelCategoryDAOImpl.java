package com.ftn.owpproject.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.ftn.owpproject.dao.TravelCategoryDAO;
import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.model.enums.TravelCategoryEnum;

@Repository
public class TravelCategoryDAOImpl implements TravelCategoryDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	public class TravelCategoryRowCallBackHandler implements RowCallbackHandler {
//		private Map<Long, TravelCategory> travelCategories = new LinkedHashMap<>();
//		
//		@Override
//		public void processRow(ResultSet resultSet)throws SQLException{
//			int index = 1;
//			Long id = resultSet.getLong(index++);
//			String name = resultSet.getString(index++);
//			String description = resultSet.getString(index++);
//			TravelCategory travelCategory = travelCategories.get(id);
//			TravelCategoryEnum nameEnum = TravelCategoryEnum.valueOf(name);
//			if(travelCategory == null) {
//				travelCategory = new TravelCategory(id,nameEnum,description);
//			}
//		}
//		
//		public List<TravelCategory> getTravelCategories(){
//			return new ArrayList<>(travelCategories.values());
//		}
//	}
	public class TravelCategoryRowCallBackHandler implements RowCallbackHandler {
	    private Map<Long, TravelCategory> travelCategories = new LinkedHashMap<>();

	    @Override
	    public void processRow(ResultSet resultSet) throws SQLException {
	        Long id = resultSet.getLong("id");
	        String name = resultSet.getString("name");
	        String description = resultSet.getString("description");

	        TravelCategoryEnum nameEnum = TravelCategoryEnum.valueOf(name);

	        TravelCategory travelCategory = new TravelCategory(id, nameEnum, description);

	        travelCategories.put(id, travelCategory);
	    }

	    public List<TravelCategory> getTravelCategories() {
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
	    String sql = "SELECT * FROM TravelCategory";
	    TravelCategoryRowCallBackHandler rowCallBackHandler = new TravelCategoryRowCallBackHandler();
	    jdbcTemplate.query(sql, rowCallBackHandler);
	    return rowCallBackHandler.getTravelCategories();
	}


	@Override
	public int save(TravelCategory travelCategory) {
		PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				String sql = "INSERT INTO TravelCategory (name,description) VALUES (?,?)";
				
				PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int index = 1;
				preparedStatement.setString(index++, travelCategory.getCategoryName().toString());
				preparedStatement.setString(index++, travelCategory.getDescription());
				
				return preparedStatement;
			}

		};
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
		
		return success?1:0;
		
	}

	@Override
	public Long getIdByName(String categoryName) {
	    String sql = "SELECT id FROM TravelCategory WHERE name = ?";
	    try {
	        System.out.println("Executing SQL: " + sql + " with parameter: " + categoryName);
	        Long categoryId = jdbcTemplate.queryForObject(sql, new Object[]{categoryName}, new RowMapper<Long>() {
	            @Override
	            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
	                return rs.getLong("id");
	            }
	        });
	        System.out.println("Category ID found: " + categoryId);
	        return categoryId;
	    } catch (EmptyResultDataAccessException e) {
	        System.err.println("No category found with name: " + categoryName);
	        return null; 
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; 
	    }
	}
	@Override
	public int update(TravelCategory travelCategory) {
		String sql = "UPDATE TravelCategory SET name = ?, description = ?";	
		boolean success = jdbcTemplate.update(sql) == 1; 
		return success?1:0;
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM TravelCategory WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
    public TravelCategory findByCategoryName(TravelCategoryEnum categoryName) {
        String sql = "SELECT * FROM TravelCategory WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{categoryName.toString()}, new RowMapper<TravelCategory>() {
                @Override
                public TravelCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    TravelCategoryEnum nameEnum = TravelCategoryEnum.valueOf(name);

                    return new TravelCategory(id, nameEnum, description);
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
