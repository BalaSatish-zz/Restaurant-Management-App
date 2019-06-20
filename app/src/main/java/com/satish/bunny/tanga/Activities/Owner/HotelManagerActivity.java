package com.satish.bunny.tanga.Activities.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.satish.bunny.tanga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HotelManagerActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvTypesEditor;
    private TextView tvMenuEditor;
    private TextView tvQRGenerator;
    private TextView tvTakeAway;
    private TextView tvOrderMangaer;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String UserPath = "Owners/";
    private String MenuPath = "MenuPath";
    private String UserId = "";
    private static String BasicPath;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean GrantAccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_manager);
        tvTypesEditor = (TextView) findViewById(R.id.tvTypesEditor);
        tvMenuEditor = (TextView) findViewById(R.id.tvMenuEditor);
        tvQRGenerator = (TextView) findViewById(R.id.tvQRGenerator);
        tvTakeAway = (TextView) findViewById(R.id.tvTakeAway);
        tvOrderMangaer = (TextView) findViewById(R.id.tvOrderManager);

        tvTypesEditor.setOnClickListener(this);
        tvMenuEditor.setOnClickListener(this);
        tvQRGenerator.setOnClickListener(this);
        tvTakeAway.setOnClickListener(this);
        tvOrderMangaer.setOnClickListener(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserId = firebaseUser.getUid();
            UserPath = UserPath + UserId + "/Path";
            databaseReference = FirebaseDatabase.getInstance().getReference(UserPath);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        MenuPath = dataSnapshot.getValue().toString();
                        BasicPath = MenuPath.replace("Menu/","");
                        SetOwnerStatus();
                    }
                    catch (NullPointerException e){
                        Toast.makeText(HotelManagerActivity.this, "Contact Us.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        if(v == tvTypesEditor)
        {
            Intent i = new Intent(this,TypesEditorActivity.class);
            i.putExtra("BasicPath",BasicPath);
            if(GrantAccess){
                startActivity(i);
            }else{
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == tvMenuEditor)
        {
            Intent i = new Intent(this,MenuCreatorActivity.class);
            i.putExtra("BasicPath",BasicPath);
            if(GrantAccess){
                startActivity(i);
            }else{
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == tvQRGenerator)
        {

            Intent i = new Intent(this,QRGeneratorActivity.class);
            i.putExtra("BasicPath",BasicPath);
            if(GrantAccess){
                startActivity(i);
            }else{
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == tvTakeAway)
        {
            Intent i = new Intent(this,TakeAwayActivity.class);
            i.putExtra("BasicPath",BasicPath);
            if(GrantAccess){
                startActivity(i);
            }else{
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == tvOrderMangaer)
        {

            Intent i = new Intent(this,OrderMangerActivity.class);
            i.putExtra("BasicPath",BasicPath);
            if(GrantAccess){
                startActivity(i);
            }else{
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SetOwnerStatus() {
        if( BasicPath==null){
            Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
        }else if(!BasicPath.isEmpty())
        {
            if(!BasicPath.contains("Tanga"))
            {
                Toast.makeText(this, "Contact Us.", Toast.LENGTH_SHORT).show();
            }else{
                GrantAccess = true;
            }
        }
    }
}
