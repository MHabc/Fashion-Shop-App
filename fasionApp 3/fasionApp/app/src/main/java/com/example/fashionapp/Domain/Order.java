package com.example.fashionapp.Domain;

import java.io.Serializable;
import java.util.UUID;

public class Order implements Serializable {
    private String userId;
    private String phoneNumber;
    private String deliveryAddress;
    private double totalFee;
    private String items;
    private String timestamp;
    private String timestamp2;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String userId, String phoneNumber, String deliveryAddress, double totalFee, String items, String timestamp,String timestamp2) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.totalFee = totalFee;
        this.items = items;
         this.timestamp=timestamp;
        this.timestamp2 =generateOrderId();
    }

    private String generateOrderId() {
        // Sử dụng thời gian hiện tại và một số ngẫu nhiên để tạo mã số đơn hàng
        String uniqueId = Long.toString(System.currentTimeMillis()); // Thời gian hiện tại (millis)
        uniqueId += "-" + (int) (Math.random() * 10000); // Số ngẫu nhiên từ 0 đến 9999
        return uniqueId;
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

    public String getTimestamp2() {
        return timestamp2;
    }

    public void setTimestamp2(String timestamp2) {
        this.timestamp2 = timestamp2;
    }
}
