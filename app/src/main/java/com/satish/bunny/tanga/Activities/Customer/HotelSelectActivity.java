package com.satish.bunny.tanga.Activities.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.satish.bunny.tanga.MethodClasses.BillAR;
import com.satish.bunny.tanga.OrderActivity;
import com.satish.bunny.tanga.R;

import java.util.ArrayList;

public class HotelSelectActivity extends AppCompatActivity implements View.OnClickListener {
    //private ArrayAdapter<List> lAdapter,cAdapter,rAdapter;
    private ArrayAdapter<String> lAdapter,cAdapter,rAdapter;
    private Spinner sLocation;
    private Spinner sCity;
    private Spinner sRestaurant;
    private ArrayList<String> Location,City,Restaurant;
    private String l,c,r;
    private DatabaseReference lReference,cReferenece,rReference;
    private TextView tvScanner;
    private ImageView ivScanner;
    private String path="RestaurantsPath/";
    private boolean doubleBackToExitPressedOnce = false;
    private String Lpath,Cpath,Rpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_select);
        sLocation = (Spinner) findViewById(R.id.sLocation);
        sCity = (Spinner) findViewById(R.id.sCity);
        sRestaurant = (Spinner) findViewById(R.id.sRestaurant);
        //ivScanner = (ImageView) findViewById(R.id.ivScanner);
        //ivScanner.setOnClickListener(this);
        tvScanner = (TextView) findViewById(R.id.tvScanner);
        tvScanner.setOnClickListener(this);
        Location = new ArrayList<String>();
        City = new ArrayList<String>();
        Restaurant = new ArrayList<String>();
        Location.add("Select Location");
        Location.add("Andhra Pradesh");
        Location.add("Karnataka");
        Location.add("Telangana");
        Restaurant.add("Select Restaurant");

        lAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,Location);
        lAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sLocation.setAdapter(lAdapter);

        sLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l = String.valueOf(parent.getItemAtPosition(position));
                if(l!="Select Location") {
                    path ="RestaurantsPath/";
                    Lpath = path+l+"/";
                    lReference = FirebaseDatabase.getInstance().getReference(Lpath);
                    City.clear();
                    City.add("Select City");
                    lReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            c=dataSnapshot.getKey().toString();
                            City.add(c);
                        }
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                    cAdapter = new ArrayAdapter<String>(HotelSelectActivity.this,R.layout.support_simple_spinner_dropdown_item,City);
                    cAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    sCity.setAdapter(cAdapter);

                    sCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            c = String.valueOf(parent.getItemAtPosition(position));
                            if(c!="Select City") {
                                Cpath = Lpath+c+"/";
                                cReferenece = FirebaseDatabase.getInstance().getReference(Cpath);
                                Restaurant.clear();
                                Restaurant.add("Select Restaurant");
                                cReferenece.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        r=dataSnapshot.getKey().toString();
                                        Restaurant.add(r);
                                    }
                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });

                                rAdapter = new ArrayAdapter<String>(HotelSelectActivity.this,R.layout.support_simple_spinner_dropdown_item,Restaurant);
                                rAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                sRestaurant.setAdapter(rAdapter);

                                sRestaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        r = String.valueOf(parent.getItemAtPosition(position));
                                        if(r!="Select Restaurant") {
                                            Rpath=Cpath+r+"/";
                                            BillAR.clearBPList();
                                            Rpath = Rpath.replace("RestaurantsPath","Tanga");
                                            Intent i = new Intent(HotelSelectActivity.this,OrderActivity.class);
                                            i.putExtra("WholePath",Rpath);
                                            i.putExtra("RestaurantName",r);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                            else if(c=="Select City"&& Restaurant.size()>1)
                            {
                                sRestaurant.setAdapter(null);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else if (l=="Select Location" && City.size()>1 && Restaurant.size()>1)
                {
                    sCity.setAdapter(null);
                    sRestaurant.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    protected void onStart() {
        super.onStart();
        lReference.goOnline();
        cReferenece.goOnline();

    }

    @Override
    protected void onPause() {
        super.onPause();
        lReference.goOffline();
        cReferenece.goOffline();
        sRestaurant.setAdapter(null);
        sCity.setAdapter(null);
        sLocation.setAdapter(lAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //recreate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        lReference.goOffline();
        cReferenece.goOffline();
    }

    @Override
    public void onClick(View v) {
        if(v==ivScanner || v== tvScanner)
        {
            startActivity(new Intent(HotelSelectActivity.this,QRScannerActivity.class));
        }
    }
}
