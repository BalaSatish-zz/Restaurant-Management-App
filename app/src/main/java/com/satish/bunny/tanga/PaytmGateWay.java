package com.satish.bunny.tanga;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.satish.bunny.tanga.Activities.Bills.BillListActivity;

public class PaytmGateWay extends AppCompatActivity implements View.OnClickListener {
    private Button bPay,bIncreaseBalance;
    private TextView etBalance,etBillAmount;
    private static int Bal=1500;
    private static int TotalBill=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_gate_way);
        bPay = (Button) findViewById(R.id.bPay);
        bIncreaseBalance = (Button) findViewById(R.id.bIncreaseBalance);
        bIncreaseBalance.setOnClickListener(this);
        bPay.setOnClickListener(this);
        TotalBill = BillListActivity.getBillTotal();
        etBalance = (TextView) findViewById(R.id.etBalance);
        etBalance.setText("Balance: "+Bal);
        etBillAmount = (TextView) findViewById(R.id.etBillAmount);
        etBillAmount.setText("Total Bill: "+ TotalBill);
    }

    @Override
    public void onClick(View v) {
        if(v == bPay){
            if(TotalBill>Bal){
                Toast.makeText(this,"Low Balance", Toast.LENGTH_SHORT).show();
            }
            else if(TotalBill<=Bal){
                Bal = Bal - TotalBill;
                etBalance.setText("Balance: "+Bal);
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                TotalBill = BillListActivity.getBillTotal();
                String BP = OrderActivity.getBasicPath();
                BillListActivity.orderMethod(BP+"/Offline/","Orders");
                Toast.makeText(PaytmGateWay.this, "Order Placed", Toast.LENGTH_SHORT).show();
            }

        }
        if(v == bIncreaseBalance){
            Bal = Bal+500;
            Toast.makeText(this, "Added Rs.500", Toast.LENGTH_SHORT).show();
            etBalance.setText("Balance: "+Bal);

        }
    }
}
