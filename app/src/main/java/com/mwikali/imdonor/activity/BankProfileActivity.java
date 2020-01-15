package com.mwikali.imdonor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.databinding.ActivityBankProfileBinding;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.utils.AppUtils;

public class BankProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBankProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bank_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UserBank userBank = new AppUtils().getBankUserAccount();
        binding.setUserBank(userBank);

        MyClickHandlers handlers = new MyClickHandlers(getApplicationContext());
        binding.setHandlers(handlers);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new AppUtils().logout(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyClickHandlers {
        Context context;

        MyClickHandlers(Context context) {
            this.context = context;
        }

        public void onFabClicked(View view) {
            startActivity(new Intent(BankProfileActivity.this, SignUpBankDetails.class));
        }
    }
}
