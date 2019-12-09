package com.mwikali.imdonor.models;

public class UserBank {
    public String id, name, email, phone, county, town, streetAddress;

    public UserBank() {
    }

    public UserBank(String id, String name, String email, String phone, String county, String town, String streetAddress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.county = county;
        this.town = town;
        this.streetAddress = streetAddress;
    }

    @Override
    public String toString() {
        return name;
    }
}
