package com.ftn.owpproject.service;

import com.ftn.owpproject.model.Reservation;

import java.util.List;

import org.springframework.stereotype.Service;
@Service
public interface ReservationService {
    List<Reservation> findByUserId(Long userId);
    Reservation findOne(Long reservationId);
    void cancelReservation(Long reservationId) throws Exception;
    void save(Reservation reservation);
}
