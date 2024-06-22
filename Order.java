package com.example.fashionapp.Domain;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String userId;
    private String phoneNumber;
    private String deliveryAddress;
    private double totalFee;
    private String items;
    private String timestamp;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String userId, String phoneNumber, String deliveryAddress, double totalFee, String items,String timestamp) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.totalFee = totalFee;
        this.items = items;
         this.timestamp=timestamp;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public String  getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
