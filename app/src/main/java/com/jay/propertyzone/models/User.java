package com.jay.propertyzone.models;

public class User {
    private  String id;
    private  String name;
    private  String email;
    private  String country;
    private  String PropertyLevel;

    public User() {
        //
    }

    public User(String name, String email,String country) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.PropertyLevel = PropertyLevel;
    }

    public String getPropertyLevel() {
        return PropertyLevel;
    }

    public void setPropertyLevel(String propertyLevel) {
        this.PropertyLevel = propertyLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", PropertyLevel='" + PropertyLevel + '\'' +
                '}';
    }
}
