package com.mwikali.imdonor.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;

public class DonationRequest {
    public String id, userId, firstName, lastName, treatment, bloodGroup, contactName, contact, altContact, hospitalId, hospital, date;
    public int age;
    public float unit;
    public Object timestamp;

    public DonationRequest() {
    }

    public DonationRequest(String id, String userId, String firstName, String lastName, String treatment, String bloodGroup, String contactName, String contact, String altContact, String hospitalId, String hospital, String date, int age, float unit) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.treatment = treatment;
        this.bloodGroup = bloodGroup;
        this.contactName = contactName;
        this.contact = contact;
        this.altContact = altContact;
        this.hospitalId = hospitalId;
        this.hospital = hospital;
        this.date = date;
        this.age = age;
        this.unit = unit;
        this.timestamp = FieldValue.serverTimestamp();
    }

    @Exclude
    public long getCreatedTimestampLong(){
        return (long)timestamp;
    }
}
