package com.ftn.owpproject.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.ftn.owpproject.dao.TravelDAO;
import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.model.enums.TransportationType;
import com.ftn.owpproject.model.enums.TravelCategoryEnum;
import com.ftn.owpproject.model.enums.TypeOfAccommodation;

@Repository
public class TravelDAOImpl implements TravelDAO {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public class TravelRowCallbackHandler implements RowCallbackHandler {

	private Map<Long, Travel> travels = new LinkedHashMap<>();
	
	@Override
	public void processRow(ResultSet resultSet) throws SQLException {
        
		int index = 1;
        Long id = resultSet.getLong(index++);
        String transportationTypeStr = resultSet.getString(index++);
        TransportationType transportationType = TransportationType.valueOf(transportationTypeStr.toUpperCase());
        String accommodationTypeStr = resultSet.getString(index++);
        TypeOfAccommodation accommodationType = TypeOfAccommodation.valueOf(accommodationTypeStr.toUpperCase());
        String destinationName = resultSet.getString(index++);
        String locationImage = resultSet.getString(index++);
        Long travelCategoryId = resultSet.getLong(index++);
        TravelCategory travelCategory = getTravelCategoryById(travelCategoryId);
        LocalDateTime departureDateTime = resultSet.getTimestamp(index++).toLocalDateTime();
        LocalDateTime returnDateTime = resultSet.getTimestamp(index++).toLocalDateTime();
        int numberOfNights = resultSet.getInt(index++);
        double arrangementPrice = resultSet.getDouble(index++);
        int totalSeats = resultSet.getInt(index++);
        int availableSeats = resultSet.getInt(index++);

        Travel travel = new Travel(id, transportationType, accommodationType, destinationName, locationImage,
                travelCategory, departureDateTime, returnDateTime, numberOfNights, arrangementPrice, totalSeats,
                availableSeats);

        travels.put(travel.getId(), travel);
    }
	
    public List<Travel> getTravels() {
		return new ArrayList<>(travels.values());
	}
    
}
	
	@SuppressWarnings("deprecation")
	private TravelCategory getTravelCategoryById(Long categoryId) {
	    String sql = "SELECT * FROM TravelCategory WHERE id = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[]{categoryId}, (rs, rowNum) ->
	            new TravelCategory(
	                    rs.getLong("id"),
	                    TravelCategoryEnum.valueOf(rs.getString("name")),
	                    rs.getString("description")));
	}
	
	@Override
	public Travel findOne(Long id) {
	    String sql = "SELECT * FROM Travel WHERE id = ?";
	    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
	        return new Travel(
	            rs.getLong("id"),
	            TransportationType.valueOf(rs.getString("transportation_type")),
	            TypeOfAccommodation.valueOf(rs.getString("accommodation_type")),
	            rs.getString("destination_name"),
	            rs.getString("location_image"),
	            getTravelCategoryById(rs.getLong("travel_category_id")),
	            rs.getTimestamp("departure_date_time").toLocalDateTime(),
	            rs.getTimestamp("return_date_time").toLocalDateTime(),
	            rs.getInt("number_of_nights"),
	            rs.getDouble("arrangement_price"),
	            rs.getInt("total_seats"),
	            rs.getInt("available_seats")
	        );
	    }, id);
	}
	
	@Override
	public List<Travel> findAll() {
	    String sql = "SELECT * FROM Travel";
	    TravelRowCallbackHandler rowCallbackHandler = new TravelRowCallbackHandler();
		jdbcTemplate.query(sql, rowCallbackHandler);

		return rowCallbackHandler.getTravels();
	}
	
	@Override
	public int save(Travel travel) {
	    String sql = "INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangement_price, total_seats, available_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    int rowsAffected = jdbcTemplate.update(sql, travel.getTransportationType().toString(), travel.getAccommodationType().toString(), travel.getDestinationName(), travel.getLocationImage(), travel.getTravelCategory().getId(), Timestamp.valueOf(travel.getDepartureDateTime()), Timestamp.valueOf(travel.getReturnDateTime()), travel.getNumberOfNights(), travel.getArrangmentPrice(), travel.getTotalSeats(), travel.getAvailableSeats());

	    return rowsAffected > 0 ? 1 : 0;
	}

	@Override
	public int update(Travel travel) {
	    String sql = "UPDATE Travel SET transportation_type = ?, accommodation_type = ?, destination_name = ?, location_image = ?, travel_category_id = ?, departure_date_time = ?, return_date_time = ?, number_of_nights = ?, arrangement_price = ?, total_seats = ?, available_seats = ? WHERE id = ?";
	    int rowsAffected = jdbcTemplate.update(sql, travel.getTransportationType().toString(), travel.getAccommodationType().toString(), travel.getDestinationName(), travel.getLocationImage(), travel.getTravelCategory().getId(), Timestamp.valueOf(travel.getDepartureDateTime()), Timestamp.valueOf(travel.getReturnDateTime()), travel.getNumberOfNights(), travel.getArrangmentPrice(), travel.getTotalSeats(), travel.getAvailableSeats(), travel.getId());

	    return rowsAffected > 0 ? 1 : 0;
	}


	@Override
	public int delete(Long id) {
	    String sql = "DELETE FROM Travel WHERE id = ?";
	    return jdbcTemplate.update(sql, id);
	}


}