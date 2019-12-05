package com.mwikali.imdonor.utils;

import android.content.Context;

import com.mwikali.imdonor.App;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;

public class AppUtils {

    public UserDonor getDonorUserAccount() {
        UserDonor userDonor = App.getInstance().tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class);
        return userDonor;
    }

    public String getDonorUniqueId() {
        UserDonor userDonor = App.getInstance().tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class);
        return getNowTimeStamp() + userDonor.id;
    }

    public UserBank getBankUserAccount() {
        UserBank userBank = App.getInstance().tindyDb.getObject(Constants.KEY_BANK, UserBank.class);
        return userBank;
    }

    public String getBankUniqueId() {
        UserBank userBank = App.getInstance().tindyDb.getObject(Constants.KEY_BANK, UserBank.class);
        return getNowTimeStamp() + userBank.id;
    }

    public static String sanitizePhoneNumber(String phone) {

        if (phone.equals("")) {
            return "";
        }

        if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }
        if (phone.length() == 13 && phone.startsWith("+")) {
            String p = phone.replaceFirst("^+", "");
            return p;
        }
        return phone;
    }

    private Long getNowTimeStamp() {
        return System.currentTimeMillis();
    }
}
