package com.example.fashionapp.Domain;

import android.net.Uri;

public class ReviewDomain {

    private String Name;
    private String Description;
    private String PicUrl;
    private double rating;
    private int ItemId;

    public String getName() {
        return Name;
    }



    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getItemId() {
        return ItemId;
    }

    public ReviewDomain(String name, String description, Uri picUrl, double rating) {
        Name = name;
        Description = description;
        PicUrl = String.valueOf(picUrl);
        this.rating = rating;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public ReviewDomain() {
    }
}
