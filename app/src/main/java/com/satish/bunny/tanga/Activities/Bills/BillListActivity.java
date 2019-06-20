package com.satish.bunny.tanga.Activities.Bills;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.satish.bunny.tanga.Activities.Access.LoginActivity;
import com.satish.bunny.tanga.Adapters.BillList;
import com.satish.bunny.tanga.FireClasses.BillProducer;
import com.satish.bunny.tanga.MethodClasses.BillAR;
import com.satish.bunny.tanga.MethodClasses.PlaceOrderHelper;
import com.satish.bunny.tanga.OrderActivity;
import com.satish.bunny.tanga.PaytmGateWay;
import com.satish.bunny.tanga.R;

import java.util.ArrayList;
import java.util.List;

public class BillListActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lvBillItems;
    private static List<BillProducer> BPList;
    private DatabaseReference billDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String ItemName="",ItemCost="";
    static int BillTotal = 0;
    int intItemCost=0;
    static boolean ScanStatus =false;
    private TextView tvBillTotal;
    private Button bOrder;
    private String TableNumber;
    private String BasicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_bill_list);
        lvBillItems = (ListView) findViewById(R.id.lvBillItems);
        Activity context = null;
        tvBillTotal =(TextView) findViewById(R.id.tvBillTotal);
        ScanStatus = OrderActivity.isPaymentStatus();
        bOrder = (Button) findViewById(R.id.bOrder);
        bOrder.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BPList = new ArrayList<BillProducer>();
        BPList.clear();
        BPList = BillAR.getBPList();
        BillTotal = 0;
        if(BPList.isEmpty())
        {
            Toast.makeText(getBaseContext(), "Empty", Toast.LENGTH_SHORT).show();
        }else{
            for (BillProducer x :BPList) {
                ItemCost = x.getItemCost();
                intItemCost = Integer.parseInt(String.valueOf(ItemCost));
                BillTotal = BillTotal + intItemCost;
            }
            BillList adapter = new BillList(BillListActivity.this,BPList);
            lvBillItems.setAdapter(adapter);
            tvBillTotal.setText(""+BillTotal);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_logout:
                FirebaseAuth firebaseauth;
                firebaseauth = FirebaseAuth.getInstance();
                firebaseauth.signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.privacy_policies:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://hmspt.000webhostapp.com/privacy_policy.html")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == bOrder){
            TableNumber = OrderActivity.getTableNumber();
            BasicPath = OrderActivity.getBasicPath();
            if(TableNumber != null && BasicPath !=null && TableNumber.contains("Table-")){
                orderMethod(BasicPath,TableNumber);
                Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                startActivity(new Intent(this,PaytmGateWay.class));
            }
        }
    }
    public static void orderMethod(String BP, String TN){
        PlaceOrderHelper POH = new PlaceOrderHelper(BP,TN,BPList);
    }
    public static int getBillTotal() {
        return BillTotal;
    }
}
