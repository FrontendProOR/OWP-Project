package com.ftn.owpproject.model;

public class LoyaltyCard {
    private int discount;
    private int loyaltyPoints;

    // Empty Constructor
    public LoyaltyCard() {
    }

    // Full Constructor
    public LoyaltyCard(int discount, int loyaltyPoints) {
        this.discount = discount;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Getters and Setters
    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
