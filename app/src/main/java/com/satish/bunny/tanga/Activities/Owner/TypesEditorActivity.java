package com.satish.bunny.tanga.Activities.Owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.satish.bunny.tanga.FireClasses.ItemType;
import com.satish.bunny.tanga.R;
import com.satish.bunny.tanga.Adapters.TypesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TypesEditorActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference,mdatabaseReference,xdatabaseReference;
    private String OwnersPath="";
    private String UserId = "";
    private static String MenuTypesPath="Tanga/Andhra Pradesh/Anantapur/ABC/MenuTypes";
    private List<ItemType> itemTypesList;
    private String Name="";
    private String ItemTypeName="";
    private Button bAddType;
    private EditText etItemTypeName;
    private ListView lvItemTypes;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_editor);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        etItemTypeName = (EditText) findViewById(R.id.etItemTypeName);
        bAddType = (Button) findViewById(R.id.bAddType);
        bAddType.setOnClickListener(this);
        lvItemTypes = (ListView) findViewById(R.id.lvItemTypes);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("getting Item Types");
        String BasicPath = getIntent().getStringExtra("BasicPath");

        itemTypesList = new ArrayList<ItemType>();

        MenuTypesPath = BasicPath +"MenuTypes/";
        xdatabaseReference = FirebaseDatabase.getInstance().getReference(MenuTypesPath);
        xdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemTypesList.clear();
                progressDialog.show();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    ItemType IT = child.getValue(ItemType.class);
                    //Toast.makeText(TypesEditorActivity.this,""+IT.getItemTypeName(), Toast.LENGTH_SHORT).show();
                    itemTypesList.add(IT);
                }
                TypesList adapter = new TypesList(TypesEditorActivity.this,itemTypesList);
                progressDialog.dismiss();
                lvItemTypes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(TypesEditorActivity.this, "Error! Occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String getMenuTypesPath() {
        return MenuTypesPath;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private String CamelCase(String wholeString){
        String Words ="",Result="";
        Words = wholeString;
        char firstChar = Words.charAt(0);
        Result = Result+ Character.toUpperCase(firstChar);
        for(int i =1;i<Words.length();i++){
            char currenetChar = Words.charAt(i);
            char previousChar = Words.charAt(i-1);
            if(previousChar == ' ')
            {
                Result = Result+Character.toUpperCase(currenetChar);
            }else{
                Result = Result + currenetChar;
            }
        }
        return Result;
    }


    @Override
    public void onClick(View v) {
        if(v==bAddType)
        {
            ItemTypeName = etItemTypeName.getText().toString().trim();
            ItemTypeName = ItemTypeName.toLowerCase();
            ItemTypeName = CamelCase(ItemTypeName);
            mdatabaseReference = FirebaseDatabase.getInstance().getReference(MenuTypesPath);
            ItemType it = new ItemType(ItemTypeName);
            mdatabaseReference.child(ItemTypeName).setValue(it);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }
    }
}
