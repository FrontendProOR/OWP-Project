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
import com.ftn.owpproject.service.TravelCategoryService;

@Repository
public class TravelDAOImpl implements TravelDAO {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private TravelCategoryService travelCategoryService;
    
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
            double arrangmentPrice = resultSet.getDouble(index++);
            double originalPrice = resultSet.getDouble(index++);  // Dodato
            int totalSeats = resultSet.getInt(index++);
            int availableSeats = resultSet.getInt(index++);
            double discountPercentage = resultSet.getDouble(index++);
            Timestamp discountEndTimestamp = resultSet.getTimestamp(index++);
            LocalDateTime discountEndDate = (discountEndTimestamp != null) ? discountEndTimestamp.toLocalDateTime() : null;

            Travel travel = new Travel(id, transportationType, accommodationType, destinationName, locationImage,
                    travelCategory, departureDateTime, returnDateTime, discountEndDate, numberOfNights, arrangmentPrice, originalPrice, totalSeats, // Ažurirano
                    availableSeats, discountPercentage);

            travels.put(travel.getId(), travel);
        }
        
        public List<Travel> getTravels() {
            return new ArrayList<>(travels.values());
        }
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
                rs.getTimestamp("discount_end_date") != null ? rs.getTimestamp("discount_end_date").toLocalDateTime() : null,
                rs.getInt("number_of_nights"),
                rs.getDouble("arrangment_price"),
                rs.getDouble("original_price"),  
                rs.getInt("total_seats"),
                rs.getInt("available_seats"),
                rs.getDouble("discount_percentage")
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
        String sql = "INSERT INTO Travel (transportation_type, accommodation_type, destination_name, location_image, travel_category_id, departure_date_time, return_date_time, number_of_nights, arrangment_price, original_price, total_seats, available_seats, discount_percentage, discount_end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, travel.getTransportationType().toString(), travel.getAccommodationType().toString(), travel.getDestinationName(), travel.getLocationImage(), travel.getTravelCategory().getId(), Timestamp.valueOf(travel.getDepartureDateTime()), Timestamp.valueOf(travel.getReturnDateTime()), travel.getNumberOfNights(), travel.getArrangmentPrice(), travel.getOriginalPrice(), travel.getTotalSeats(), travel.getAvailableSeats(), travel.getDiscountPercentage(), travel.getDiscountEndDate() != null ? Timestamp.valueOf(travel.getDiscountEndDate()) : null);
    }

    @Override
    public int countReservationsByTravelId(Long travelId) {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE travel_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{travelId}, Integer.class);
    }

    
    @Override
    public int update(Travel travel) {
        List<TravelCategory> travelCategories = travelCategoryService.findAll();
        if (travelCategories == null || travelCategories.isEmpty()) {
            System.err.println("Travel categories list is null or empty");
            return 0;
        }
        
        Long travelCategoryId = null;
        for (TravelCategory category : travelCategories) {
            if (category.getCategoryName().equals(travel.getTravelCategory().getCategoryName())) {
                travelCategoryId = category.getId();
                break;
            }
        }
        
        if (travelCategoryId == null) {
            System.err.println("No category found with name: " + travel.getTravelCategory().getCategoryName());
            return 0;
        }

        String sql = "UPDATE Travel SET transportation_type = ?, accommodation_type = ?, destination_name = ?, location_image = ?, travel_category_id = ?, departure_date_time = ?, return_date_time = ?, number_of_nights = ?, arrangment_price = ?, original_price = ?, total_seats = ?, available_seats = ?, discount_percentage = ?, discount_end_date = ? WHERE id = ?";  // Ažurirano
        return jdbcTemplate.update(sql, travel.getTransportationType().toString(), travel.getAccommodationType().toString(), travel.getDestinationName(), travel.getLocationImage(), travelCategoryId, Timestamp.valueOf(travel.getDepartureDateTime()), Timestamp.valueOf(travel.getReturnDateTime()), travel.getNumberOfNights(), travel.getArrangmentPrice(), travel.getOriginalPrice(), travel.getTotalSeats(), travel.getAvailableSeats(), travel.getDiscountPercentage(), travel.getDiscountEndDate() != null ? Timestamp.valueOf(travel.getDiscountEndDate()) : null, travel.getId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM Travel WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    private TravelCategory getTravelCategoryById(Long categoryId) {
        String sql = "SELECT * FROM TravelCategory WHERE id = ?";
        List<TravelCategory> results = jdbcTemplate.query(sql, new Object[]{categoryId}, (rs, rowNum) -> new TravelCategory(rs.getLong("id"), TravelCategoryEnum.valueOf(rs.getString("name")), rs.getString("description")));
        return results.isEmpty() ? null : results.get(0);
    }
    
    @Override
    public int updateAvailableSeats(Long travelId, int availableSeats) {
        String sql = "UPDATE Travel SET available_seats = ? WHERE id = ?";
        return jdbcTemplate.update(sql, availableSeats, travelId);
    }

    @Override
    public int updatePrice(Long travelId, double newPrice) {
        String sql = "UPDATE Travel SET arrangment_price = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newPrice, travelId);
    }
}
