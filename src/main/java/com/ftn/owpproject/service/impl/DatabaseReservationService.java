package com.ftn.owpproject.service.impl;

import com.ftn.owpproject.dao.ReservationDAO;
import com.ftn.owpproject.dao.TravelDAO;
import com.ftn.owpproject.model.Reservation;
import com.ftn.owpproject.model.Travel;
import com.ftn.owpproject.service.ReservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseReservationService implements ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    @Autowired
    private TravelDAO travelDAO;

    @Override
    public List<Reservation> findByUserId(Long userId) {
        return reservationDAO.findByUserId(userId);
    }

    @Override
    public void cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationDAO.findOne(reservationId);
        if (reservation == null) {
            throw new Exception("Reservation not found");
        }

        Travel travel = reservation.getTravel();
        if (travel == null) {
            throw new Exception("Travel not found");
        }

        if (travel.getDepartureDateTime().isBefore(LocalDateTime.now().plusHours(48))) {
            throw new Exception("Cannot cancel reservation less than 48 hours before travel starts");
        }

        travel.setAvailableSeats(travel.getAvailableSeats() + reservation.getReservedSeats());
        travelDAO.update(travel);
        reservationDAO.delete(reservationId);
    }

    @Override
    public void save(Reservation reservation) {
        reservationDAO.save(reservation);
    }
}
