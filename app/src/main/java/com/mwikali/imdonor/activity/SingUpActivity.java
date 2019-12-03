package com.mwikali.imdonor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mwikali.imdonor.R;

public class SingUpActivity extends AppCompatActivity {

    private boolean isDonor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        isDonor = getIntent().getExtras().getBoolean("isDonor");

        TextView tvSignUp = findViewById(R.id.txt_signup);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDonor) {
                    startActivity(new Intent(SingUpActivity.this, SignUpDonorDetails.class));
                } else {
                    startActivity(new Intent(SingUpActivity.this, SignUpBankDetails.class));
                }
            }
        });

    }
}
