package com.mwikali.imdonor.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.DonationRequest;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;
import com.mwikali.imdonor.utils.AppUtils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class NewBloodRequestActivity extends AppCompatActivity {

    CoordinatorLayout coodBloodRequest;
    TextInputLayout txtInptFirstName, txtInptLastName, txtInptAge, txtInptTreatment, txtInptNoOfUnits, txtInptRequiredDate, txtInptContactNo, txtInptAltContactNo;
    TextInputEditText edtFirstName, edtLastName, edtAge, edtTreatment, edtNoOfUnits, edtRequiredDate, edtContactNo, edtAltContactNo;
    CircularProgressImageButton fabRequest;
    MaterialButtonToggleGroup mbtgBloodgroup;
    DatePickerDialog picker;
    SearchableSpinner spnHospital;
    String firstName;
    String lastName;
    String age;
    String treatment;
    String noOfUnits;
    String requiredDate;
    UserBank hospital;
    String contactNo;
    String altContactNo;
    String bloodGroup;
    TextView tvSelectHospital;
    private String TAG = NewBloodRequestActivity.class.getSimpleName();
    private FirebaseFirestore database;
    List<UserBank> userBanks = new ArrayList<>();
    private boolean isDonor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blood_request);
        database = FirebaseFirestore.getInstance();

        isDonor = getIntent().getExtras().getBoolean("isDonor");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        if (isDonor) {
            getRegisteredBanks();
        } else {
            tvSelectHospital.setVisibility(View.GONE);
            spnHospital.setVisibility(View.GONE);
        }


        //To stop keyboard from showing
        edtRequiredDate.setInputType(InputType.TYPE_NULL);
        edtRequiredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(NewBloodRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                edtRequiredDate.setText(date);
                                requiredDate = date;
                            }
                        }, year, month, day);

                //to prevent selection of past dates
                picker.getDatePicker().setMinDate(calendar.getTimeInMillis());

                picker.show();
            }
        });

        mbtgBloodgroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
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
            }
        });

        fabRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRequestBlood();
            }
        });
    }

    private void initViews() {
        coodBloodRequest = findViewById(R.id.coodBloodRequest);
        txtInptFirstName = findViewById(R.id.txtInptFirstName);
        txtInptLastName = findViewById(R.id.txtInptLastName);
        txtInptAge = findViewById(R.id.txtInptAge);
        txtInptTreatment = findViewById(R.id.txtInptTreatment);
        txtInptNoOfUnits = findViewById(R.id.txtInptNoOfUnits);
        txtInptRequiredDate = findViewById(R.id.txtInptRequiredDate);
        //txtInptHospitalName = findViewById(R.id.txtInptHospitalName);
        txtInptContactNo = findViewById(R.id.txtInptContactNo);
        txtInptAltContactNo = findViewById(R.id.txtInptAltContactNo);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAge = findViewById(R.id.edtAge);
        edtTreatment = findViewById(R.id.edtTreatment);
        edtNoOfUnits = findViewById(R.id.edtNoOfUnits);
        edtRequiredDate = findViewById(R.id.edtRequiredDate);
        //edtHospitalName = findViewById(R.id.edtHospitalName);
        edtContactNo = findViewById(R.id.edtContactNo);
        edtAltContactNo = findViewById(R.id.edtAltContactNo);
        mbtgBloodgroup = findViewById(R.id.mbtgBloodgroup);
        tvSelectHospital = findViewById(R.id.tvSelectHospital);
        spnHospital = findViewById(R.id.spnHospital);
        spnHospital.setTitle(getString(R.string.select_hospital));
        fabRequest = findViewById(R.id.fabRequest);
    }

    private void getRegisteredBanks() {
        database.collection(Constants.KEY_COLLECTION_BANKS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userBanks.add(document.toObject(UserBank.class));
                            }

                            final ArrayAdapter<UserBank> userBankArrayAdapter =
                                    new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, userBanks);
                            userBankArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            spnHospital.setAdapter(userBankArrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void validateAndRequestBlood() {
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        age = edtAge.getText().toString();
        treatment = edtTreatment.getText().toString();
        noOfUnits = edtNoOfUnits.getText().toString();
        requiredDate = edtRequiredDate.getText().toString();
        contactNo = edtContactNo.getText().toString();
        altContactNo = edtAltContactNo.getText().toString();

        if (validateEmpty(txtInptFirstName, edtFirstName, firstName, R.string.err_first_name)) {
            return;
        }
        if (validateEmpty(txtInptLastName, edtLastName, lastName, R.string.err_last_name)) {
            return;
        }
        if (validateEmpty(txtInptAge, edtAge, age, R.string.err_invalid_age_alt)) {
            return;
        }
        if (validateEmpty(txtInptTreatment, edtTreatment, treatment, R.string.err_treatment)) {
            return;
        }
        if (validateEmpty(txtInptNoOfUnits, edtNoOfUnits, noOfUnits, R.string.err_no_of_units)) {
            return;
        }
        if (validateEmpty(txtInptRequiredDate, edtRequiredDate, requiredDate, R.string.err_date)) {
            return;
        }

        if (isDonor) {
            if (spnHospital.getSelectedItem() == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.select_hospital), Toast.LENGTH_LONG).show();
                return;
            } else {
                hospital = userBanks.get(spnHospital.getSelectedItemPosition());
            }
        } else {
            hospital = new AppUtils().getBankUserAccount();
        }

        if (mbtgBloodgroup.getCheckedButtonIds().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_blood_group), Toast.LENGTH_LONG).show();
            return;
        }

        if (validateNumber(contactNo)) {
            return;
        }

        if (!TextUtils.isEmpty(altContactNo)) {
            if (validateNumber(altContactNo)) {
                return;
            }
        }

        createBloodRequest();
    }

    private void createBloodRequest() {
        fabRequest.startAnimation();

        String uniqueId, contactName;
        if (isDonor) {
            uniqueId = new AppUtils().getDonorUniqueId();
            UserDonor userDonor = new AppUtils().getDonorUserAccount();
            contactName = userDonor.firstName + " " + userDonor.lastName;
        } else {
            uniqueId = new AppUtils().getBankUniqueId();
            UserBank userBank = new AppUtils().getBankUserAccount();
            contactName = userBank.name;
        }

        final DonationRequest donationRequest = new DonationRequest(uniqueId, firstName, lastName, treatment,
                bloodGroup, contactName, contactNo, altContactNo, hospital.id,
                hospital.name, requiredDate, Integer.parseInt(age), Float.parseFloat(noOfUnits), FieldValue.serverTimestamp());

        database.collection(Constants.KEY_COLLECTION_BLOOD_REQUESTS)
                .document(uniqueId)
                .set(donationRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        fabRequest.revertAnimation();
                        finish();
                        Toast.makeText(getApplicationContext(), getString(R.string.success_blood_request), Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fabRequest.revertAnimation();
                        Snackbar.make(coodBloodRequest, getString(R.string.err_processing_request), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createBloodRequest();
                                    }
                                }).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateEmpty(TextInputLayout textInputLayout, TextInputEditText textInputEditText, String content, int error) {
        if (TextUtils.isEmpty(content.trim())) {
            textInputLayout.setError(getString(error));
            textInputEditText.requestFocus();
            return true;
        } else {
            textInputLayout.setErrorEnabled(false);
            return false;
        }
    }

    /**
     * @param phoneNumber pass our phoneNumber
     * @return true if is a valid phone number
     */
    private boolean validateNumber(String phoneNumber) {
        if (phoneNumber.trim().isEmpty() || !isValidNumber(phoneNumber.trim()) || phoneNumber.trim().length() < 10 || phoneNumber.trim().length() > 13) {
            edtContactNo.requestFocus();
            txtInptContactNo.setError(getString(R.string.err_msg_number));
            return true;
        } else {
            txtInptContactNo.setErrorEnabled(false);
        }
        return false;
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
}
