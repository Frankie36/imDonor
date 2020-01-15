package com.mwikali.imdonor.activity;

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

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.Donation;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;
import com.mwikali.imdonor.utils.AppUtils;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;

public class NewDonationActivity extends AppCompatActivity {
    private CoordinatorLayout coodNewDonation;
    private CircularProgressImageButton fabUploadDonation;
    private TextInputLayout txtInptDate, txtInptWeight, txtInptHb;
    private TextInputEditText edtDate, edtWeight, edtHb;
    private SearchableSpinner spnDonors;
    private MaterialButtonToggleGroup mbtgBloodgroup;
    private CheckBox chckHepB, chckHepC, chckHiv, chckSyphilis;
    private boolean isHepBPos, isHepCPos, isHivPos, isSyphilis;
    private String date, weight, bloodGroup, hb;
    private FirebaseFirestore database;
    private List<UserDonor> userDonorList = new ArrayList<>();
    private String TAG = NewDonationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_donation);
        database = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        getDonors();

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

        fabUploadDonation.setOnClickListener(new View.OnClickListener() {
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
        coodNewDonation = findViewById(R.id.coodNewDonation);
        fabUploadDonation = findViewById(R.id.fabUploadDonation);
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

    private void getDonors() {
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("","")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userDonorList.add(document.toObject(UserDonor.class));
                            }

                            final ArrayAdapter<UserDonor> userBankArrayAdapter =
                                    new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, userDonorList);
                            userBankArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                            spnDonors.setAdapter(userBankArrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void validateAndUpload() {
        date = edtDate.getText().toString();
        weight = edtWeight.getText().toString();
        hb = edtHb.getText().toString();

        if (validateEmpty(txtInptDate, edtDate, date, R.string.err_date))
            return;

        if (spnDonors.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.select_donor), Toast.LENGTH_LONG).show();
            return;
        }

        if (validateEmpty(txtInptWeight, edtWeight, weight, R.string.err_weight)) {
            return;
        }

        if (bloodGroup.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_group), Toast.LENGTH_LONG).show();
            return;
        }

        if (validateEmpty(txtInptHb, edtHb, hb, R.string.err_hb)) {
            return;
        }

        createDonation();
    }

    private void createDonation() {
        fabUploadDonation.animate();

        String uniqueId = new AppUtils().generateBankUniqueId();
        UserBank userBank = new AppUtils().getBankUserAccount();

        Donation donation = new Donation(uniqueId, "", userBank.id,
                "", userBank.name, date, weight, bloodGroup, isHivPos, isHepBPos, isHepCPos, isSyphilis, hb, "");

        database.collection(Constants.KEY_COLLECTION_BLOOD_DONATIONS)
                .document(uniqueId)
                .set(donation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        fabUploadDonation.revertAnimation();
                        finish();
                        Toast.makeText(getApplicationContext(), getString(R.string.success_blood_donation), Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        fabUploadDonation.revertAnimation();
                        Snackbar.make(coodNewDonation, getString(R.string.err_processing_request), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.try_again), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createDonation();
                                    }
                                }).show();
                    }
                });

    }

    public boolean validateEmpty(TextInputLayout textInputLayout, TextInputEditText textInputEditText, String content, int error) {
        if (TextUtils.isEmpty(content.trim())) {
            textInputLayout.setError(getString(error));
            textInputEditText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            return true;
        }
    }

}
