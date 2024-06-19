package com.ftn.owpproject.dao;

import com.ftn.owpproject.model.Reservation;

import java.util.List;

public interface ReservationDAO {
    Reservation findOne(Long id);
    List<Reservation> findAll();
    List<Reservation> findByUserId(Long userId);
    int save(Reservation reservation);
    int delete(Long id);
}
