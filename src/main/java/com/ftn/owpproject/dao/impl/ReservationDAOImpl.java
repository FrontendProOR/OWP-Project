package com.ftn.owpproject.dao.impl;

import com.ftn.owpproject.dao.ReservationDAO;
import com.ftn.owpproject.dao.TravelDAO;
import com.ftn.owpproject.model.Reservation;
import com.ftn.owpproject.model.Travel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ReservationDAOImpl implements ReservationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TravelDAO travelDAO;

    private class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Travel travel = travelDAO.findOne(rs.getLong("travel_id"));
            return new Reservation(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    travel,
                    rs.getTimestamp("reservation_date").toLocalDateTime(),
                    rs.getInt("reserved_seats"),
                    rs.getDouble("total_price")
            );
        }
    }

    @Override
    public Reservation findOne(Long id) {
        String sql = "SELECT * FROM Reservation WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ReservationRowMapper(), id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM Reservation";
        return jdbcTemplate.query(sql, new ReservationRowMapper());
    }

    @Override
    public List<Reservation> findByUserId(Long userId) {
        String sql = "SELECT * FROM Reservation WHERE user_id = ?";
        return jdbcTemplate.query(sql, new ReservationRowMapper(), userId);
    }

    @Override
    public int save(Reservation reservation) {
        String sql = "INSERT INTO Reservation (user_id, travel_id, reservation_date, reserved_seats, total_price) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, reservation.getUserId(), reservation.getTravel().getId(), 
            Timestamp.valueOf(reservation.getReservationDate()), reservation.getReservedSeats(), reservation.getTotalPrice());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM Reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
