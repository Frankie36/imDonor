package com.mwikali.imdonor;

import android.content.Intent;

import androidx.multidex.MultiDexApplication;

import com.mwikali.imdonor.activity.LoginActivity;
import com.mwikali.imdonor.activity.MainActivityBank;
import com.mwikali.imdonor.activity.MainActivityDonor;
import com.mwikali.imdonor.db.TinyDB;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;

public class App extends MultiDexApplication {
    public TinyDB tindyDb;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
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
                Intent intent = new Intent(getApplicationContext(), MainActivityBank.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

}
