package com.mwikali.imdonor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.mwikali.imdonor.models.UserDonor;

import java.util.Calendar;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class SignUpDonorDetails extends AppCompatActivity {

    //Declare our views
    CoordinatorLayout coodSignUpDonor;
    TextInputLayout txtInptFirstName, txtInptLastName, txtInptDob, txtInptEmail, txtInptPhone;
    TextInputEditText edtFirstName, edtLastName, edtDob, edtEmail, edtPhone;
    MaterialButtonToggleGroup mbtgBloodgroup;
    SwitchCompat swchDonor;
    String firstName, lastName, dob, email, phone, bloodGroup;
    boolean isDonor, isUpdate;
    CircularProgressImageButton fabPassword;
    DatePickerDialog picker;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String TAG = SignUpDonorDetails.class.getSimpleName();
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_donor_details);
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

        //To stop keyboard from showing
        edtDob.setInputType(InputType.TYPE_NULL);
        edtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR) - 16; //Since minimum years for donation is 16
                // date picker dialog
                picker = new DatePickerDialog(SignUpDonorDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //check if year selected is invalid i.e: in the future or over 130 years ago
                                if (year > calendar.get(Calendar.YEAR) || year < calendar.get(Calendar.YEAR) - 130) {
                                    edtDob.getText().clear();
                                    txtInptDob.setError(getString(R.string.err_invalid_age));
                                } else {
                                    txtInptDob.setErrorEnabled(false);

                                    /**
                                     * Since minimum years for donation is 16 and max 65
                                     * We allow user to log in but inform him/her of this
                                     */
                                    if (year > calendar.get(Calendar.YEAR) - 16) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.err_not_old_enough), Toast.LENGTH_LONG).show();
                                    } else if (year < calendar.get(Calendar.YEAR) - 65) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.err_old), Toast.LENGTH_LONG).show();
                                    }

                                    String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    edtDob.setText(date);
                                    dob = date;
                                }
                            }
                        }, year, month, day);

                picker.show();
            }
        });

        swchDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new MaterialDialog.Builder(SignUpDonorDetails.this)
                            .title(R.string.requirements)
                            .content(R.string.requirements_details)
                            .positiveText(R.string.confirm)
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    swchDonor.setChecked(false);
                                    isDonor = false;
                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    swchDonor.setChecked(true);
                                    isDonor = true;
                                }
                            })
                            .negativeText(R.string.no)
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    swchDonor.setChecked(false);
                                    isDonor = false;
                                }
                            }).show();
                }
            }
        });


        mbtgBloodgroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.btnBloodGroupAPos:
                        bloodGroup = getString(R.string.a_positive);
                        break;
                    case R.id.btnBloodGroupANeg:
                        bloodGroup = getString(R.string.a_negative);
                        break;
                    case R.id.btnBloodGroupBPos:
                        bloodGroup = getString(R.string.b_positive);
                        break;
                    case R.id.btnBloodGroupBNeg:
                        bloodGroup = getString(R.string.b_negative);
                        break;
                    case R.id.btnBloodGroupOPos:
                        bloodGroup = getString(R.string.o_positive);
                        break;
                    case R.id.btnBloodGroupONeg:
                        bloodGroup = getString(R.string.o_negative);
                        break;
                    case R.id.btnBloodGroupABPos:
                        bloodGroup = getString(R.string.ab_positive);
                        break;
                    case R.id.btnBloodGroupABNeg:
                        bloodGroup = getString(R.string.ab_negative);
                        break;
                }
            }
        });


        fabPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });

    }

    //Initialize our views
    private void initViews() {
        txtInptFirstName = findViewById(R.id.txtInptFirstName);
        txtInptLastName = findViewById(R.id.txtInptLastName);
        txtInptDob = findViewById(R.id.txtInptDob);
        txtInptEmail = findViewById(R.id.txtInptEmail);
        txtInptPhone = findViewById(R.id.txtInptPhone);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtDob = findViewById(R.id.edtDob);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        mbtgBloodgroup = findViewById(R.id.mbtgBloodgroup);
        swchDonor = findViewById(R.id.swchDonor);
        fabPassword = findViewById(R.id.fabPassword);
        coodSignUpDonor = findViewById(R.id.coodSignUpDonor);
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
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        dob = edtDob.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();

        if (TextUtils.isEmpty(firstName.trim())) {
            txtInptFirstName.setError(getString(R.string.err_first_name));
            edtFirstName.requestFocus();
            return;
        } else {
            txtInptFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(lastName.trim())) {
            txtInptLastName.setError(getString(R.string.err_last_name));
            edtLastName.requestFocus();
            return;
        } else {
            txtInptLastName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(dob.trim())) {
            txtInptDob.setError(getString(R.string.err_dob));
            edtDob.requestFocus();
            return;
        } else {
            txtInptDob.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_contact), Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!TextUtils.isEmpty(email)) {
                if (!validateEmail(email, txtInptEmail, edtEmail)) {
                    return;
                }
            } else if (!TextUtils.isEmpty(phone)) {
                if (!validateNumber(phone)) {
                    return;
                }
            }
        }

        if (isUpdate) {
            upDateAccount();
        } else {
            showPasswordDialog();
        }

    }

    private void showPasswordDialog() {

        materialDialog = new MaterialDialog.Builder(SignUpDonorDetails.this)
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

                registerUser(email, password);
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

    private void registerUser(String email, String password) {
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
                            Toast.makeText(SignUpDonorDetails.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUser(final FirebaseUser user) {
        fabPassword.startAnimation();
        final UserDonor userDonor = new UserDonor(user.getUid(), firstName, lastName, dob, bloodGroup,
                email, phone, "", "", isDonor);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(user.getUid())
                .set(userDonor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                        fabPassword.revertAnimation();

                        //Store user data to app
                        App.getInstance().tindyDb.putObject(Constants.KEY_DONOR, userDonor);

                        //Launch main activity class
                        Intent intent = new Intent(getApplicationContext(), MainActivityDonor.class);
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
                        Snackbar.make(coodSignUpDonor, getString(R.string.err_login), Snackbar.LENGTH_LONG)
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
