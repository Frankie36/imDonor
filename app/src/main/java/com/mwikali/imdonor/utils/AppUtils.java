package com.mwikali.imdonor.utils;

import android.content.Context;
import android.content.Intent;

import com.mwikali.imdonor.App;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.activity.LoginActivity;
import com.mwikali.imdonor.activity.SignUpActivity;
import com.mwikali.imdonor.db.TinyDB;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;

public class AppUtils {

    public UserDonor getDonorUserAccount() {
        UserDonor userDonor = App.getInstance().tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class);
        return userDonor;
    }

    public String generateDonorUniqueId() {
        UserDonor userDonor = App.getInstance().tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class);
        return getNowTimeStamp() + userDonor.id;
    }

    public UserBank getBankUserAccount() {
        UserBank userBank = App.getInstance().tindyDb.getObject(Constants.KEY_BANK, UserBank.class);
        return userBank;
    }

    public String generateBankUniqueId() {
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

    private void clearDb() {
        App.getInstance().tindyDb.clear();
    }

    public void logout(Context context) {
        clearDb();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
