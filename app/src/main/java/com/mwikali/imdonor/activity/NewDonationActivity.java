package com.mwikali.imdonor.activity;

import android.os.Bundle;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mwikali.imdonor.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class NewDonationActivity extends AppCompatActivity {
    private FloatingActionButton fabUploadDonor;
    private TextInputLayout txtInptDate, txtInptWeight, txtInptHb;
    private TextInputEditText edtDate, edtWeight, edtHb;
    private SearchableSpinner spnDonors;
    private MaterialButtonToggleGroup mbtgBloodgroup;
    private CheckBox chckHepB, chckHepC, chckHiv, chckSyphilis;
    private boolean isHepBPos, isHepCPos, isHivPos, isSyphilis;
    private String date, weight, bloodGroup, hb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        fabUploadDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndUpload();
            }
        });

        chckHepB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHepBPos = isChecked;
            }
        });

        chckHepC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHepCPos = isChecked;
            }
        });

        chckHiv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHivPos = isChecked;
            }
        });

        chckSyphilis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSyphilis = isChecked;
            }
        });
    }

    private void initViews() {
        fabUploadDonor = findViewById(R.id.fabUploadDonor);
        txtInptDate = findViewById(R.id.txtInptDate);
        edtDate = findViewById(R.id.edtDate);
        spnDonors = findViewById(R.id.spnDonors);
        txtInptWeight = findViewById(R.id.txtInptWeight);
        edtWeight = findViewById(R.id.edtWeight);
        mbtgBloodgroup = findViewById(R.id.mbtgBloodgroup);
        txtInptHb = findViewById(R.id.txtInptHb);
        edtHb = findViewById(R.id.edtHb);
        chckHepB = findViewById(R.id.chckHepB);
        chckHepC = findViewById(R.id.chckHepC);
        chckHiv = findViewById(R.id.chckHiv);
        chckSyphilis = findViewById(R.id.chckSyphilis);
    }

    private void validateAndUpload() {
        date = edtDate.getText().toString();
        weight = edtWeight.getText().toString();
        hb = edtHb.getText().toString();

        if (!validateEmpty(txtInptDate, edtDate, date, R.string.err_date))
            return;

        if (spnDonors.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.select_donor), Toast.LENGTH_LONG).show();
            return;
        }

        if (!validateEmpty(txtInptWeight, edtWeight, weight, R.string.err_weight)) {
            return;
        }

        if(bloodGroup.isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.err_group), Toast.LENGTH_LONG).show();
            return;
        }

        if (!validateEmpty(txtInptHb, edtHb, hb, R.string.err_hb)) {
            return;
        }


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

}
