package com.satish.bunny.tanga.Activities.Owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.satish.bunny.tanga.Adapters.OrdersListAdapter;
import com.satish.bunny.tanga.FireClasses.PlaceOrderMain;
import com.satish.bunny.tanga.R;

import java.util.ArrayList;
import java.util.List;

public class OrderMangerActivity extends AppCompatActivity {
    private String BasicPath;
    private List<PlaceOrderMain> OrdersList;
    private String OrdersPath;
    private DatabaseReference firebaseDatabase;
    private ListView lvOrders;
    private Spinner sTableNumbers;
    private ArrayList<String> TableNumbers;
    private static String TableNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manger);

        TableNumbers = new ArrayList<String>();
        OrdersList = new ArrayList<PlaceOrderMain>();

        String BasicPath = getIntent().getStringExtra("BasicPath");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        sTableNumbers = (Spinner) findViewById(R.id.sTableNumbers);
        lvOrders = (ListView) findViewById(R.id.lvOrders);
        OrdersPath = BasicPath + "Orders/";
        firebaseDatabase = FirebaseDatabase.getInstance().getReference(OrdersPath);
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.show();

                sTableNumbers.setAdapter(null);

                TableNumbers.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    String ChildName = child.getKey().toString().trim();
                    TableNumbers.add(ChildName);
                }
                progressDialog.dismiss();
                ArrayAdapter<String> Adapter = new ArrayAdapter<String>(OrderMangerActivity.this,R.layout.support_simple_spinner_dropdown_item,TableNumbers);
                sTableNumbers.setAdapter(Adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sTableNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TableNumber = sTableNumbers.getItemAtPosition(position).toString();
                firebaseDatabase.child(TableNumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        OrdersList.clear();
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            PlaceOrderMain POM = child.getValue(PlaceOrderMain.class);
                            OrdersList.add(POM);
                        }
                        if(OrdersList!=null){
                            OrdersListAdapter adapter = new OrdersListAdapter(OrderMangerActivity.this,OrdersList);
                            lvOrders.setAdapter(adapter);
                            lvOrders.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
}
