package com.jay.propertyzone.models;

import org.parceler.Parcel;

@Parcel
public class Property {
    private String imageUrl;
    private String price;
    private String title;
    private String location;
    private String description;
    private String phone;
    private String id;


    public Property() {
        // Default constructor
    }

    public Property(String price, String title, String location, String description,String phone, String imageUrl) {
        this.price = price;
        this.title = title;
        this.location = location;
        this.description = description;
        this.phone = phone;
        this.id = "";
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getImageUri() {
        return imageUrl;
    }

    public Property(String imageUri) {
        this.imageUrl = imageUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImageUri(String imageUri) {
        this.imageUrl = imageUri;
    }

    public void setId(String id) {
        this.id = id;
    }
}
