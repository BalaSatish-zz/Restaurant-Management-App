package com.satish.bunny.tanga.Activities.Owner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.satish.bunny.tanga.MethodClasses.SecureData;
import com.satish.bunny.tanga.R;

import java.util.concurrent.ThreadLocalRandom;

public class QRGeneratorActivity extends AppCompatActivity {
    private ImageView ivQRImage;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String UserPath = "Owners/";
    private String MenuPath = "MenuPath";
    private String UserId = "";
    private DatabaseReference StatusReference, ExtraReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String BasicPath = getIntent().getStringExtra("BasicPath");

        if (BasicPath != null) {
            MenuPath = BasicPath + "Menu/";
            String StatusPath = "";
            String ExtraPath = "";
            StatusPath = MenuPath.replace("/Menu/", "/Status/");
            ExtraPath = MenuPath.replace("/Menu/", "/Extra/");
            ivQRImage = (ImageView) findViewById(R.id.ivQRImage);
            SecureData SD = new SecureData();
            final int[] RandomNum = {94949};
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                RandomNum[0] = ThreadLocalRandom.current().nextInt(11111111, 99999999 + 1);
            }
            MenuPath = SD.Encrypt(MenuPath);
            GenerateQR(MenuPath + RandomNum[0]);
            // Toast.makeText(QRGeneratorActivity.this, ""+MenuPath, Toast.LENGTH_SHORT).show();
            StatusReference = FirebaseDatabase.getInstance().getReference(StatusPath);
            ExtraReference = FirebaseDatabase.getInstance().getReference(ExtraPath);
            StatusReference.child("Status").setValue("Unused");
            ExtraReference.child("Extra").setValue("" + RandomNum[0]);
            StatusReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String Status = dataSnapshot.child("Status").getValue().toString();
                    //Toast.makeText(QRGeneratorActivity.this,Status, Toast.LENGTH_SHORT).show();
                    if (Status != "Unused") {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            RandomNum[0] = ThreadLocalRandom.current().nextInt(9999, 99999999 + 1);
                        }
                        GenerateQR(MenuPath + RandomNum[0]);
                        //Toast.makeText(QRGeneratorActivity.this, "Entered", Toast.LENGTH_SHORT).show();
                        StatusReference.child("Status").setValue("Unused");
                        ExtraReference.child("Extra").setValue("" + RandomNum[0]);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    public void GenerateQR(String MenuPath){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(MenuPath, BarcodeFormat.QR_CODE,800,800);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQRImage.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}
