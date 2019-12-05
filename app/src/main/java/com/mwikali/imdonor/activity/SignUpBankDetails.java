package com.mwikali.imdonor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mwikali.imdonor.R;

public class SignUpBankDetails extends AppCompatActivity {

    TextInputLayout txtInptBankName, txtInptLocation, txtInptEmail, txtInptPhone, txtInptCounty, txtInptTown, txtInptStreetAddress;
    TextInputEditText edtBankName, edtLocation, edtEmail, edtPhone, edtCounty, edtTown, edtStreetAddress;
    FloatingActionButton fabPassword;
    String name, location, email, phone, county, town, streetAddress;
    boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_bank_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpdate = bundle.getBoolean("isUpdate");
        }

        String toolbarTitle;
        if (isUpdate) {
            toolbarTitle = getString(R.string.edit_account_details);
            fabPassword.setVisibility(View.GONE);
        } else {
            toolbarTitle = getString(R.string.add_account_details);
        }

        getSupportActionBar().setTitle(toolbarTitle);

        fabPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });
    }

    public void initViews() {
        txtInptBankName = findViewById(R.id.txtInptBankName);
        txtInptLocation = findViewById(R.id.txtInptLocation);
        txtInptEmail = findViewById(R.id.txtInptEmail);
        txtInptPhone = findViewById(R.id.txtInptPhone);
        txtInptCounty = findViewById(R.id.txtInptCounty);
        txtInptTown = findViewById(R.id.txtInptTown);
        txtInptStreetAddress = findViewById(R.id.txtInptStreetAddress);
        edtBankName = findViewById(R.id.edtBankName);
        edtLocation = findViewById(R.id.edtLocation);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtCounty = findViewById(R.id.edtCounty);
        edtTown = findViewById(R.id.edtTown);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        fabPassword = findViewById(R.id.fabPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        if (!isUpdate) {
            menu.removeItem(R.id.action_done);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            validateAndSubmit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void validateAndSubmit() {
        name = edtBankName.getText().toString();
        location = edtLocation.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        county = edtCounty.getText().toString();
        town = edtCounty.getText().toString();
        streetAddress = edtStreetAddress.getText().toString();


        if (TextUtils.isEmpty(name.trim())) {
            txtInptBankName.setError(getString(R.string.err_bank_name));
            edtBankName.requestFocus();
            return;
        } else {
            txtInptBankName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(location.trim())) {
            txtInptLocation.setError(getString(R.string.err_location_name));
            edtLocation.requestFocus();
            return;
        } else {
            txtInptLocation.setErrorEnabled(false);
        }

        if (!validateEmail(email, txtInptEmail, edtEmail)) {
            return;
        }

        if (!validateNumber(phone)) {
            return;
        }

        if (TextUtils.isEmpty(county.trim())) {
            txtInptCounty.setError(getString(R.string.err_county));
            edtCounty.requestFocus();
            return;
        } else {
            txtInptCounty.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(town.trim())) {
            txtInptTown.setError(getString(R.string.err_town));
            edtTown.requestFocus();
            return;
        } else {
            txtInptTown.setErrorEnabled(false);
        }

        if (isUpdate) {
            upDateAccount();
        } else {
            showPasswordDialog();
        }
    }

    private void showPasswordDialog() {

        MaterialDialog materialDialog = new MaterialDialog.Builder(SignUpBankDetails.this)
                .title(R.string.create_password)
                .customView(R.layout.dialog_password, true)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .build();

        final TextInputLayout txtInptEmail, txtInptPassword, txtInptRptPassword;
        final TextInputEditText edtEmail, edtPassword, edtRptPassword;

        txtInptEmail = materialDialog.getCustomView().findViewById(R.id.txtInptEmail);
        txtInptPassword = materialDialog.getCustomView().findViewById(R.id.txtInptPassword);
        txtInptRptPassword = materialDialog.getCustomView().findViewById(R.id.txtInptRptPassword);
        edtEmail = materialDialog.getCustomView().findViewById(R.id.edtEmail);
        edtPassword = materialDialog.getCustomView().findViewById(R.id.edtPassword);
        edtRptPassword = materialDialog.getCustomView().findViewById(R.id.edtRptPassword);

        if (!TextUtils.isEmpty(email)) {
            edtEmail.setText(email);
        }

        materialDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, rptPassword;
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                rptPassword = edtRptPassword.getText().toString();

                if (!validateEmail(email, txtInptEmail, edtEmail))
                    return;

                if (TextUtils.isEmpty(password.trim())) {
                    edtPassword.requestFocus();
                    txtInptPassword.setError(getString(R.string.err_password));
                    return;
                } else {
                    txtInptPassword.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(rptPassword.trim())) {
                    edtRptPassword.requestFocus();
                    txtInptRptPassword.setError(getString(R.string.err_password));
                    return;
                } else {
                    txtInptRptPassword.setErrorEnabled(false);
                }

                if (!TextUtils.equals(password, rptPassword)) {
                    txtInptPassword.setError(getString(R.string.err_password_mismatch));
                    txtInptRptPassword.setError(getString(R.string.err_password_mismatch));
                    edtRptPassword.requestFocus();
                    return;
                } else {
                    txtInptPassword.setErrorEnabled(false);
                    txtInptRptPassword.setErrorEnabled(false);
                }

                createAccount();
            }
        });

        materialDialog.show();
    }

    //check if email is empty and that syntax is okay
    private boolean validateEmail(String email, TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        if (email.trim().isEmpty() || !isValidEmail(email.trim())) {
            textInputEditText.requestFocus();
            textInputLayout.setError(getString(R.string.err_msg_email));
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * @param phoneNumber pass our phoneNumber
     * @return true if is a valid phone number
     */
    private boolean validateNumber(String phoneNumber) {
        if (phoneNumber.trim().isEmpty() || !isValidNumber(phoneNumber.trim()) || phoneNumber.trim().length() < 10 || phoneNumber.trim().length() > 13) {
            edtPhone.requestFocus();
            txtInptPhone.setError(getString(R.string.err_msg_number));
            return false;
        } else {
            txtInptPhone.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidNumber(String id) {
        //Only allow '+' character in phone...the rest should be digits
        for (char c : id.replace("+", "").toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private void upDateAccount() {

    }

    private void createAccount() {

    }

}
