package com.ftn.owpproject.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long id;
    private Long userId;
    private Travel travel;
    private LocalDateTime reservationDate;
    private int reservedSeats;
    private double totalPrice;

    public Reservation(Long id, Long userId, Travel travel, LocalDateTime reservationDate, int reservedSeats, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.travel = travel;
        this.reservationDate = reservationDate;
        this.reservedSeats = reservedSeats;
        this.totalPrice = totalPrice;
    }
    
    public Reservation( Long userId, Travel travel, LocalDateTime reservationDate, int reservedSeats, double totalPrice) {
        super();
        this.userId = userId;
        this.travel = travel;
        this.reservationDate = reservationDate;
        this.reservedSeats = reservedSeats;
        this.totalPrice = totalPrice;
    }
    
    public Reservation(Long userId, Travel travel, LocalDateTime reservationDate, int reservedSeats) {
        this.userId = userId;
        this.travel = travel;
        this.reservationDate = reservationDate;
        this.reservedSeats = reservedSeats;
        this.totalPrice = calculateTotalPrice(travel, reservedSeats);
    }

    public Reservation(Long id, Long userId, Travel travel, LocalDateTime reservationDate, int reservedSeats) {
        this.id = id;
        this.userId = userId;
        this.travel = travel;
        this.reservationDate = reservationDate;
        this.reservedSeats = reservedSeats;
        this.totalPrice = calculateTotalPrice(travel, reservedSeats);
    }

    private double calculateTotalPrice(Travel travel, int reservedSeats) {
        double price = travel.getArrangmentPrice() * reservedSeats;
        if (travel.getDiscountPercentage() > 0 && LocalDateTime.now().isBefore(travel.getDiscountEndDate())) {
            price = price - (price * (travel.getDiscountPercentage() / 100));
        }
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(int reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
