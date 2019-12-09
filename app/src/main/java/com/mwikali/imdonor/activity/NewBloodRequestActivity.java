package com.mwikali.imdonor.activity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButtonToggleGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mwikali.imdonor.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class NewBloodRequestActivity extends AppCompatActivity {

    TextInputLayout txtInptFirstName, txtInptLastName, txtInptAge, txtInptTreatment, txtInptNoOfUnits, txtInptRequiredDate, txtInptHospitalName, txtInptContactNo, txtInptAltContactNo, txtInptRequestedBy;
    TextInputEditText edtFirstName, edtLastName, edtAge, edtTreatment, edtNoOfUnits, edtRequiredDate, edtHospitalName, edtContactNo, edtAltContactNo, edtRequestedBy;
    CircularProgressImageButton fabRequest;
    MaterialButtonToggleGroup mbtgBloodgroup;
    String firstName, lastName, age, treatment, noOfUnits, requiredDate, hospital, contactNo, altContactNo, requestedBy, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blood_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

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

        fabRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initViews() {
        txtInptFirstName = findViewById(R.id.txtInptFirstName);
        txtInptLastName = findViewById(R.id.txtInptLastName);
        txtInptAge = findViewById(R.id.txtInptAge);
        txtInptTreatment = findViewById(R.id.txtInptTreatment);
        txtInptNoOfUnits = findViewById(R.id.txtInptNoOfUnits);
        txtInptRequiredDate = findViewById(R.id.txtInptRequiredDate);
        txtInptHospitalName = findViewById(R.id.txtInptHospitalName);
        txtInptContactNo = findViewById(R.id.txtInptContactNo);
        txtInptAltContactNo = findViewById(R.id.txtInptAltContactNo);
        txtInptRequestedBy = findViewById(R.id.txtInptRequestedBy);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAge = findViewById(R.id.edtAge);
        edtTreatment = findViewById(R.id.edtTreatment);
        edtNoOfUnits = findViewById(R.id.edtNoOfUnits);
        edtRequiredDate = findViewById(R.id.edtRequiredDate);
        edtHospitalName = findViewById(R.id.edtHospitalName);
        edtContactNo = findViewById(R.id.edtContactNo);
        edtAltContactNo = findViewById(R.id.edtAltContactNo);
        edtRequestedBy = findViewById(R.id.edtRequestedBy);
        mbtgBloodgroup = findViewById(R.id.mbtgBloodgroup);
        fabRequest = findViewById(R.id.fabRequest);
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
}
