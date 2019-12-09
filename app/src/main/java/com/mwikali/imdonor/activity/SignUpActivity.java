package com.mwikali.imdonor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mwikali.imdonor.App;
import com.mwikali.imdonor.Constants;
import com.mwikali.imdonor.R;
import com.mwikali.imdonor.models.UserBank;
import com.mwikali.imdonor.models.UserDonor;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout llSignUp;
    TextInputLayout txtInptEmail, txtInptPassword;
    TextInputEditText edtEmail, edtPassword;
    CircularProgressButton btnLogin;
    private boolean isDonor = false;
    private String TAG = SignUpActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        isDonor = getIntent().getExtras().getBoolean("isDonor");

        llSignUp = findViewById(R.id.llSignUp);
        txtInptEmail = findViewById(R.id.txtInptEmail);
        edtEmail = findViewById(R.id.edtEmail);
        txtInptPassword = findViewById(R.id.txtInptPassword);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        TextView tvSignUp = findViewById(R.id.txt_sign_up);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {
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

    private void validateAndLogin() {
        String email, password;
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        if (!validateEmail(email, txtInptEmail, edtEmail))
            return;

        if (TextUtils.isEmpty(password.trim())) {
            edtPassword.requestFocus();
            txtInptPassword.setError(getString(R.string.err_password));
            return;
        } else {
            txtInptPassword.setErrorEnabled(false);
        }

        login(email, password);
    }

    private void login(String email, String password) {
        btnLogin.startAnimation();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            checkIfUserExists(user);

                        } else {
                            btnLogin.revertAnimation();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkIfUserExists(final FirebaseUser user) {
        DocumentReference docRef;
        if (isDonor) {
            docRef = database.collection(Constants.KEY_COLLECTION_USERS).document(user.getUid());
        } else {
            docRef = database.collection(Constants.KEY_COLLECTION_BANKS).document(user.getUid());
        }

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        btnLogin.revertAnimation();

                        Intent intent;
                        if (isDonor) {
                            UserDonor userDonor = document.toObject(UserDonor.class);
                            //Store user data to app
                            App.getInstance().tindyDb.putObject(Constants.KEY_DONOR, userDonor);
                            intent = new Intent(getApplicationContext(), MainActivityDonor.class);
                        } else {
                            UserBank userBank = document.toObject(UserBank.class);
                            //Store user data to app
                            App.getInstance().tindyDb.putObject(Constants.KEY_DONOR, userBank);
                            intent = new Intent(getApplicationContext(), MainActivityBank.class);
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), getString(R.string.welcome), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d(TAG, "No such document");
                        btnLogin.revertAnimation();

                        Snackbar.make(llSignUp, getString(R.string.err_find_user), Snackbar.LENGTH_LONG)
                                .setAction(R.string.sign_up, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        signUp();
                                    }
                                }).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    btnLogin.revertAnimation();
                    Snackbar.make(llSignUp, getString(R.string.err_login), Snackbar.LENGTH_LONG)
                            .setAction(R.string.try_again, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkIfUserExists(user);
                                }
                            }).show();
                }
            }
        });

    }
}
