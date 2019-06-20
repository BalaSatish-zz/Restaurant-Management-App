package com.satish.bunny.tanga.Activities.Access;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.satish.bunny.tanga.Activities.Customer.HotelSelectActivity;
import com.satish.bunny.tanga.Activities.Details.DetailsActivity;
import com.satish.bunny.tanga.Activities.Owner.HotelManagerActivity;
import com.satish.bunny.tanga.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button bLogin,bRegister;
    private ImageView ivHelp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String userID, userType = "form";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);
        if (!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();
        else {
            setContentView(R.layout.activity_login);
            etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
            etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
            bLogin = (Button) findViewById(R.id.bLogin);
            bLogin.setOnClickListener(LoginActivity.this);
            bRegister = (Button) findViewById(R.id.bRegister);
            bRegister.setOnClickListener(this);
            progressDialog = new ProgressDialog(this);
            ivHelp = (ImageView) findViewById(R.id.ivHelp);
            ivHelp.setOnClickListener(this);
            FirebaseApp.initializeApp(this);
            firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                progressDialog.setMessage("Getting info! Please wait.");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                userID = firebaseAuth.getCurrentUser().getUid();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                boolean emailVerified = firebaseUser.isEmailVerified();
                if (emailVerified) {
                    //Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/UserType");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userType = (String) dataSnapshot.getValue();
                            if (userType.contains("Customer")) {
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(LoginActivity.this, HotelSelectActivity.class));
                            } else if (userType.contains("Owner")) {
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(LoginActivity.this, HotelManagerActivity.class));
                            } else if (userType.contains("Unknown")) {
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(LoginActivity.this, DetailsActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please, Verify Your Email", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.goOnline();
    }
    public String getDeviceUniqueID(Activity activity){
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }
    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.goOffline();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.goOffline();
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Check Your Internet Connection And Retry.");
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = LoginActivity.this;
                Intent mStartActivity = new Intent(context, LoginActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 50, mPendingIntent);
                System.exit(0);
            }
        });
        return builder;
    }


    private void userLogin() {
        final String lEmail = etLoginEmail.getText().toString().trim();
        String lPassword = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(lEmail)) {
            Toast.makeText(this, "Please Enter Email-Id", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(lPassword)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Getting info! Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(lEmail, lPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            if (firebaseUser != null) {
                                boolean emailVerified = firebaseUser.isEmailVerified();
                                if (emailVerified) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + userID + "/UserType");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            userType = (String) dataSnapshot.getValue();
                                            if (userType.equals("Customer")) {
                                                progressDialog.dismiss();
                                                finish();
                                                startActivity(new Intent(LoginActivity.this, HotelSelectActivity.class));
                                            } else if (userType.equals("Owner")) {
                                                progressDialog.dismiss();
                                                finish();
                                                startActivity(new Intent(LoginActivity.this, HotelManagerActivity.class));
                                            } else if (userType.equals("Unknown")) {
                                                progressDialog.dismiss();
                                                finish();
                                                startActivity(new Intent(LoginActivity.this, DetailsActivity.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Email is needed to be verified", Toast.LENGTH_LONG).show();
                                    firebaseAuth.signOut();
                                    return;
                                }
                            }
                        }else  if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Email-ID / Password", Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Incorrect Email-Id / Password", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Error Occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    @Override
    public void onClick(View v) {
        if(v == bLogin){
            userLogin();
        }
        if(v == bRegister){
            startActivity(new Intent(this,RegisterActivity.class));
        }
        if(v == ivHelp)
        {
            startActivity(new Intent (this,PasswordResetActivity.class));
        }
    }
}
