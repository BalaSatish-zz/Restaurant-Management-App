package com.satish.bunny.tanga.Activities.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.satish.bunny.tanga.MethodClasses.SecureData;
import com.satish.bunny.tanga.OrderActivity;
import com.satish.bunny.tanga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScannerActivity extends AppCompatActivity {
private DatabaseReference StatusReference,ExtraReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String ScannedPath = result.getContents();
                SecureData SD = new SecureData();
                ScannedPath = SD.Decrypt(ScannedPath);
                String StatusPath="";
                String ExtraPath="";
                String ExtraNumber="";
                if(ScannedPath.contains("Tanga"))
                {
                    ExtraNumber = ScannedPathMaker(ScannedPath);
                    ScannedPath = ScannedPath.replace(ExtraNumber,"");
                    StatusPath = ScannedPath.replace("/Menu/","/Status/");
                    ExtraPath = ScannedPath.replace("/Menu/","/Extra");

                    StatusReference = FirebaseDatabase.getInstance().getReference(StatusPath);
                    ExtraReference = FirebaseDatabase.getInstance().getReference(ExtraPath);
                    final String finalScannedPath = ScannedPath;
                    final String finalExtraNumber = ExtraNumber;
                    StatusReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ExtraReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot d) {
                                    String RandomNum = d.child("Extra").getValue().toString();
                                   // Toast.makeText(QRScannerActivity.this, "..........."+RandomNum+"............", Toast.LENGTH_SHORT).show();
                                    if(finalExtraNumber.contains(RandomNum)){
                                        Intent i = new Intent(QRScannerActivity.this,OrderActivity.class);
                                        StatusReference.child("Status").setValue("Used");
                                        i.putExtra("ScannedPath", finalScannedPath);
                                        String RN = getRestaurantName(finalScannedPath);
                                        i.putExtra("RestaurantName",RN);
                                        startActivity(i);
                                        return;
                                    }
                                    else{
                                        Toast.makeText(QRScannerActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else{
                    Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getRestaurantName(String ScannedPath) {
        String RestaurantString="";
        char Character;
        int count = 0;
        ScannedPath =new StringBuffer(ScannedPath).reverse().toString();
        for (int i=0; i<ScannedPath.length();i++)
        {
            Character = ScannedPath.charAt(i);
            if(Character=='/')
            {
                count = count + 1;
            }
            if(count==2)
            {
                if(Character!='/')
                {
                    RestaurantString = RestaurantString + Character;
                }

            }
            if(count==3)
            {
                RestaurantString = new StringBuffer(RestaurantString).reverse().toString();
                //Toast.makeText(this,RestaurantString, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return RestaurantString;
    }

    public String ScannedPathMaker(String ScannedPath)
    {
        String ExtraNumber="";
        char Character;
        ScannedPath =new StringBuffer(ScannedPath).reverse().toString();
        for (int i=0; i<ScannedPath.length();i++)
        {
            Character = ScannedPath.charAt(i);
            if(Character!='/')
            {
                ExtraNumber = ExtraNumber + Character;
            }
            if(Character=='/')
            {
                ExtraNumber = new StringBuffer(ExtraNumber).reverse().toString();
              //  Toast.makeText(this,ExtraNumber, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return ExtraNumber;
    }
}
