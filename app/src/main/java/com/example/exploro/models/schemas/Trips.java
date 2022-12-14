package com.example.exploro.models.schemas;

public class Trips {
    String city, country, place;

    public Trips(String city, String country, String place) {
        this.city = city;
        this.country = country;
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
