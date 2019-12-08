package com.mwikali.imdonor.models;

public class UserDonor {
    public String id, firstName, lastName, dob, bloodGroup, email, phone, lastDonationDate, donationNo;
    public boolean isDonor;

    //no argument constructor needed for Firestore deserializtion
    public UserDonor() {
    }

    public UserDonor(String id, String firstName, String lastName, String dob, String bloodGroup, String email, String phone, String lastDonationDate, String donationNo, boolean isDonor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.phone = phone;
        this.lastDonationDate = lastDonationDate;
        this.donationNo = donationNo;
        this.isDonor = isDonor;
    }
}
