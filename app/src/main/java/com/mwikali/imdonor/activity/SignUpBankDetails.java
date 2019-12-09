package com.mwikali.imdonor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mwikali.imdonor.App;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.UserBank;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class SignUpBankDetails extends AppCompatActivity {
    CoordinatorLayout coodSignUpBank;
    TextInputLayout txtInptBankName, txtInptLocation, txtInptEmail, txtInptPhone, txtInptCounty, txtInptTown, txtInptStreetAddress;
    TextInputEditText edtBankName, edtLocation, edtEmail, edtPhone, edtCounty, edtTown, edtStreetAddress;
    CircularProgressImageButton fabPassword;
    String name, location, email, phone, county, town, streetAddress;
    boolean isUpdate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    MaterialDialog materialDialog;
    private String TAG = SignUpBankDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_bank_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

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
        coodSignUpBank = findViewById(R.id.coodSignUpBank);
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

        materialDialog = new MaterialDialog.Builder(SignUpBankDetails.this)
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

                if (password.length() < 6) {
                    edtPassword.requestFocus();
                    txtInptPassword.setError(getString(R.string.err_password_weak));
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

                registerBank(email, password);
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

    private void registerBank(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            materialDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpBankDetails.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUser(final FirebaseUser user) {
        fabPassword.startAnimation();
        final UserBank userBank = new UserBank(user.getUid(), name, email, phone, county, town, streetAddress);

        database.collection(Constants.KEY_COLLECTION_BANKS)
                .document(user.getUid())
                .set(userBank)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        fabPassword.revertAnimation();

                        //Store user data to app
                        App.getInstance().tindyDb.putObject(Constants.KEY_BANK, userBank);

                        //Launch main activity class
                        Intent intent = new Intent(getApplicationContext(), MainActivityBank.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), getString(R.string.welcome), Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fabPassword.revertAnimation();
                        Snackbar.make(coodSignUpBank, getString(R.string.err_login), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createUser(user);
                                    }
                                }).show();
                    }
                });
    }

}
