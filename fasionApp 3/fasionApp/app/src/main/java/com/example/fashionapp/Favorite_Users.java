package com.example.fashionapp;

import com.example.fashionapp.Domain.ItemDomain;

import java.util.ArrayList;

public class Favorite_Users {
    public Favorite_Users() {
    }

    public Favorite_Users(String id, ArrayList<ItemDomain> itemDomains) {
        this.id = id;
        this.itemDomains = itemDomains;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ItemDomain> getItemDomains() {
        return itemDomains;
    }

    public void setItemDomains(ArrayList<ItemDomain> itemDomains) {
        this.itemDomains = itemDomains;
    }

    String id;
    ArrayList<ItemDomain> itemDomains;
}
