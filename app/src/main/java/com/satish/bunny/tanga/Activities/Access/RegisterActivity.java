package com.satish.bunny.tanga.Activities.Access;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.satish.bunny.tanga.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterPassword1;
    private Button bRegister;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider phoneAuthProvider;
    private boolean PhoneVerificationStatus;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etRegisterPassword1 = (EditText) findViewById(R.id.etRegisterPassword1);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.goOnline();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.goOffline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.goOffline();
    }

    private void registerUser(){
        final String rEmail = etRegisterEmail.getText().toString().trim();
        final String rPassword = etRegisterPassword.getText().toString().trim();
        final String rPassword1 = etRegisterPassword1.getText().toString().trim();
        if(TextUtils.isEmpty(rEmail)){
            Toast.makeText(this,"Please Enter Email-Id",Toast.LENGTH_LONG).show();
            return;
        }
//        if(rEmail.length()<5||(!(rEmail.length()!=0 && rEmail.contains("@")&& rEmail.contains(".")&&(rEmail.endsWith("m")||rEmail.endsWith("M"))))){
//            Toast.makeText(this, "Invalid Email-ID", Toast.LENGTH_SHORT).show();
//        }
        if(TextUtils.isEmpty(rPassword) || TextUtils.isEmpty(rPassword1)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_LONG).show();
            return;
        }
        if(!rPassword.contains(rPassword1)){
            Toast.makeText(this, "Password's Didn't Match", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rPassword.contains(rPassword1)){
        progressDialog.setMessage("Registering... With Us.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(rEmail,rPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this, "Email Verification Sent",Toast.LENGTH_SHORT).show();
                                    makeUserType(rEmail,rPassword);
                                }
                            });
                        }else if (task.getException() instanceof FirebaseAuthUserCollisionException)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Given Email-ID is Used", Toast.LENGTH_SHORT).show();

                        }else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            //If email are in incorrect  format
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Enter a Valid Email-ID", Toast.LENGTH_SHORT).show();

                        }else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            //if password not 'stronger'
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Choose a Strong Password", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            //OTHER THING
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error! has Occurred. Please, try again Later..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }
    private void makeUserType(String rEmail,String rPassword) {
        {
        firebaseAuth.signInWithEmailAndPassword(rEmail, rPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userID = firebaseAuth.getCurrentUser().getUid();
                            if (firebaseUser != null) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/UserType");
                                databaseReference.setValue("Unknown");
                                boolean emailVerified = firebaseUser.isEmailVerified();
                                if (emailVerified) {
                                    firebaseAuth.signOut();
                                    finish();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Email is needed to be verified", Toast.LENGTH_LONG).show();
                                    firebaseAuth.signOut();
                                    return;
                                }
                            }
                        }
                    }
                });
        }

    }

    @Override
    public void onClick(View v) {
        if(v==bRegister){

            registerUser();

        }
        if(v == tvLogin){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

}}