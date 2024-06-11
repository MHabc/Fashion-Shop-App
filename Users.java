package com.example.fashionapp;

import android.net.Uri;

public class Users {
    private String username;
    private String email;
    private String uid;
    private String photoUri;

    public Users(String username, String email, String uid, String photoUri, String strole) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.photoUri = photoUri;
        this.strole = strole;
    }

    public String getStrole() {
        return strole;
    }

    public void setStrole(String strole) {
        this.strole = strole;
    }

    private String strole;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    }

    public Users(String username, String email, String uid, String photoUri) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.photoUri = photoUri;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
