package com.mwikali.imdonor.models;

import java.util.Date;

public class DonationRequest {
    public String id, name, treatment, bloodGroup, contactName, contact, altContact, hospitalId, hospital;
    public int age, unit;
    public Date date;

    public DonationRequest() {
    }

    public DonationRequest(String id, String name, String treatment, String bloodGroup, String contactName, String contact, String altContact, String hospitalId, String hospital, int age, int unit, Date date) {
        this.id = id;
        this.name = name;
        this.treatment = treatment;
        this.bloodGroup = bloodGroup;
        this.contactName = contactName;
        this.contact = contact;
        this.altContact = altContact;
        this.hospitalId = hospitalId;
        this.hospital = hospital;
        this.age = age;
        this.unit = unit;
        this.date = date;
    }
}
