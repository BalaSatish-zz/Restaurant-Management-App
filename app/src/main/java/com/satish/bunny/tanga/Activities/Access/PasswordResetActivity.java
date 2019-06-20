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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.satish.bunny.tanga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button bResetPassword;
    private Button bResendVerification;
    private String EmailId,Password;
    private String UserEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        setContentView(R.layout.activity_password_reset);
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        bResetPassword = (Button) findViewById(R.id.bResetPassword);
        bResendVerification = (Button) findViewById(R.id.bResendVerification);
        bResetPassword.setOnClickListener(this);
        bResendVerification.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void ResetPassword(){
        EmailId = etLoginEmail.getText().toString().trim();
        Password = etLoginPassword.getText().toString().trim();
        progressDialog.setMessage("Sending... Password Reset Link");
        if(TextUtils.isEmpty(EmailId)){
            Toast.makeText(this, "Email-ID can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(EmailId.length()<5||(!(EmailId.length()!=0 && EmailId.contains("@")&& EmailId.contains(".")&&(EmailId.endsWith("m")||EmailId.endsWith("M")))))
        {
            Toast.makeText(this, "Please, check Your EmailID", Toast.LENGTH_SHORT).show();
            return;
        }
        if(EmailId.length()!=0 && EmailId.contains("@")&& EmailId.contains(".")&&(EmailId.endsWith("m")||EmailId.endsWith("M")))
        {
            progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(EmailId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(PasswordResetActivity.this, "Reset Link sent to Your E-Mail", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(PasswordResetActivity.this,LoginActivity.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(PasswordResetActivity.this, "Please, Try Again Later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    }
    private void ResendVerification() {
        EmailId = etLoginEmail.getText().toString().trim();
        Password = etLoginPassword.getText().toString().trim();
        progressDialog.setMessage("Sending Verification Link...!");
        if (TextUtils.isEmpty(EmailId) || TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Fields can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EmailId.length() < 5 || Password.length() < 5) {
            Toast.makeText(this, "Please, check the Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (EmailId.length() != 0 && Password.length()!=0) {
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(EmailId,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful())
                               {
                                   progressDialog.dismiss();
                                   Toast.makeText(PasswordResetActivity.this, "Verification Link Sent", Toast.LENGTH_LONG).show();
                               }
                               else  if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                   progressDialog.dismiss();
                                   Toast.makeText(PasswordResetActivity.this, "Incorrect Email-ID / Password", Toast.LENGTH_SHORT).show();
                               } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                   progressDialog.dismiss();
                                   Toast.makeText(PasswordResetActivity.this, "Incorrect Email-Id / Password", Toast.LENGTH_SHORT).show();
                               } else {
                                   progressDialog.dismiss();
                                   Toast.makeText(PasswordResetActivity.this, "Error Occurred.", Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(PasswordResetActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        if(v == bResetPassword){
            ResetPassword();
        }
        if(v == bResendVerification)
        {
            ResendVerification();
        }
    }
}
