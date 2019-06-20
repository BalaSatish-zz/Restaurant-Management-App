package com.satish.bunny.tanga.Activities.Owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

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

public class TakeAwayActivity extends AppCompatActivity {
    private String BasicPath;
    private List<PlaceOrderMain> OrdersList;
    private String OrdersPath;
    private DatabaseReference firebaseDatabase;
    private ListView lvOrders;
    ProgressDialog progressDialog;
    //private Spinner sTableNumbers;
    //private ArrayList<String> TableNumbers;
    private static String TableNumber;

    public TakeAwayActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_away);

        //TableNumbers = new ArrayList<String>();
        OrdersList = new ArrayList<PlaceOrderMain>();

        String BasicPath = getIntent().getStringExtra("BasicPath");
        OrdersPath = BasicPath + "Offline/Orders/";
        firebaseDatabase = FirebaseDatabase.getInstance().getReference(OrdersPath);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        lvOrders = (ListView) findViewById(R.id.lvOrders);
        progressDialog.show();
        //Toast.makeText(this, ""+OrdersPath, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OrdersList.clear();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    PlaceOrderMain POM = child.getValue(PlaceOrderMain.class);
                    OrdersList.add(POM);
                    //Toast.makeText(TakeAwayActivity.this, "1", Toast.LENGTH_SHORT).show();
                }
                if(OrdersList!=null){
                    //Toast.makeText(TakeAwayActivity.this, "2", Toast.LENGTH_SHORT).show();
                    OrdersListAdapter adapter = new OrdersListAdapter(TakeAwayActivity.this,OrdersList);
                    lvOrders.setAdapter(adapter);
                    lvOrders.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
