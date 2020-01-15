package com.mwikali.imdonor.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

public class Donation {
    public String donationId, donorId, bankId, donorName, bankName, date, weight, bloodGroup, hb, bp;
    public boolean hiv, hepB, hepC, syphillis;
    public Object timestamp;

    public Donation() {
    }

    public Donation(String donationId, String donorId, String bankId, String donorName, String bankName, String date, String weight, String bloodGroup, boolean hiv, boolean hepB, boolean hepC, boolean syphillis, String hb, String bp) {
        this.donationId = donationId;
        this.donorId = donorId;
        this.bankId = bankId;
        this.donorName = donorName;
        this.bankName = bankName;
        this.date = date;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
        this.hiv = hiv;
        this.hepB = hepB;
        this.hepC = hepC;
        this.syphillis = syphillis;
        this.hb = hb;
        this.bp = bp;
        this.timestamp = FieldValue.serverTimestamp();
    }

    @Exclude
    public long getCreatedTimestampLong() {
        return (long) timestamp;
    }
}
