package com.mwikali.imdonor.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.mwikali.imdonor.BR;

public class UserBank extends BaseObservable {
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

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
        notifyPropertyChanged(BR.county);
    }

    @Bindable
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
        notifyPropertyChanged(BR.town);
    }

    @Bindable
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        notifyPropertyChanged(BR.streetAddress);
    }
}
