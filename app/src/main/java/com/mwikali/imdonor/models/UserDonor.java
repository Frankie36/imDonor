package com.mwikali.imdonor.models;

public class UserDonor {
 public String id, firstName, lastName, dob, bloodGroup, email, phone;
 public boolean isDonor;

    public UserDonor(String id, String firstName, String lastName, String dob, String bloodGroup, String email, String phone, boolean isDonor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.phone = phone;
        this.isDonor = isDonor;
    }
}
