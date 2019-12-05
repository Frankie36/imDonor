package com.mwikali.imdonor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.UserDonor;

import java.util.Calendar;

public class SignUpDonorDetails extends AppCompatActivity {

    //Declare our views
    TextInputLayout txtInptFirstName, txtInptLastName, txtInptDob, txtInptEmail, txtInptPhone;
    TextInputEditText edtFirstName, edtLastName, edtDob, edtEmail, edtPhone;
    MaterialButtonToggleGroup mbtgBloodgroup;
    SwitchCompat swchDonor;
    String firstName, lastName, dob, email, phone, bloodGroup;
    boolean isDonor, isUpdate;
    FloatingActionButton fabPassword;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_donor_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpdate = bundle.getBoolean("isUpdate");
        }

        if (isUpdate) {
            fabPassword.setVisibility(View.GONE);
        }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donor_sign_up, menu);
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

        MaterialDialog materialDialog = new MaterialDialog.Builder(SignUpDonorDetails.this)
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

        if(!TextUtils.isEmpty(email)){
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
        //UserDonor(1, firstName, lastName, dob, isDonor, bloodGroup, email, phone);
    }

}
