package com.mwikali.imdonor;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.mwikali.imdonor.activity.LoginActivity;
import com.mwikali.imdonor.activity.MainActivity;
import com.mwikali.imdonor.activity.MainActivityDonor;
import com.mwikali.imdonor.db.TinyDB;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;

public class App extends Application {
    public TinyDB tindyDb;

    @Override
    public void onCreate() {
        super.onCreate();
        tindyDb = new TinyDB(getApplicationContext());

         if (tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class) == null && tindyDb.getObject(Constants.KEY_BANK, UserBank.class) == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (tindyDb.getObject(Constants.KEY_DONOR, UserDonor.class) != null) {
                Intent intent = new Intent(getApplicationContext(), MainActivityDonor.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (tindyDb.getObject(Constants.KEY_BANK, UserBank.class) != null) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

}
