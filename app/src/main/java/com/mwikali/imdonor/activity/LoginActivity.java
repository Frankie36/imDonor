package com.mwikali.imdonor.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mwikali.imdonor.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnDonorLogin = findViewById(R.id.btn_login);
        Button btnBankLogin = findViewById(R.id.btn_login_bank);

        btnDonorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SingUpActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDonor",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnBankLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SingUpActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDonor",false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
