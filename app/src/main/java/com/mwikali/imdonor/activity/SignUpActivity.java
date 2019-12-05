package com.mwikali.imdonor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mwikali.imdonor.R;

public class SignUpActivity extends AppCompatActivity {

    private boolean isDonor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        isDonor = getIntent().getExtras().getBoolean("isDonor");

        TextView tvSignUp = findViewById(R.id.txt_signup);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDonor) {
                    Intent intent = new Intent(SignUpActivity.this, SignUpDonorDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isUpdate", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SignUpActivity.this, SignUpBankDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isUpdate", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }
}
