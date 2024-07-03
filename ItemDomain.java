package com.example.fashionapp.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemDomain implements Serializable {
    private String title;
    private String itemId;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double oldPrice;
    private int review;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private double rating;
    private int NumberinCard;
    public ItemDomain() {
        // Hàm tạo không đối số cần thiết cho việc giải mã Firebase
    }
    public ItemDomain(String title, String description, ArrayList<String> picUrl, double price, double oldPrice, int review, double rating, int numberInCard)
    {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.review = review;
        this.rating = rating;
        this.NumberinCard = numberInCard;
    }
    public ItemDomain(String itemid,String title, String description, ArrayList<String> picUrl, double price, double oldPrice, int review, double rating, int numberInCard)
    {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.review = review;
        this.rating = rating;
        this.NumberinCard = numberInCard;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberinCard() {
        return NumberinCard;
    }
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setNumberinCard(int numberinCard) {
        this.NumberinCard = numberinCard;
    }
}
