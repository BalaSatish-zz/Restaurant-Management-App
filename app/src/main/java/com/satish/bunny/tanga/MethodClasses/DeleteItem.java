package com.satish.bunny.tanga.MethodClasses;

import android.content.Context;
import android.widget.Toast;

import com.satish.bunny.tanga.Activities.Owner.TypesEditorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Bunny on 10/23/2017.
 */

public class DeleteItem {
    String itemName="";
    String itemType="";
    String itemTypeName="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dReference;
    FirebaseUser firebaseUser;
    String UserID="";
    DatabaseReference OwnerPathRef;
    String pathRef="";
    public DeleteItem(){}
    private static Context context;
    public DeleteItem(Context c)
    {
        context =c;
    }

    public void Delete(String itemTypeName)
    {
        String Path = "";
        this.itemTypeName = itemTypeName;
        TypesEditorActivity TEA = new TypesEditorActivity();
        Path  = TEA.getMenuTypesPath();
        dReference = FirebaseDatabase.getInstance().getReference(Path+"/"+itemTypeName);
        dReference.removeValue();
    }

    public void Delete(final String itemName, final String itemType) {
        this.itemName = itemName;
        this.itemType = itemType;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserID = firebaseUser.getUid();
            OwnerPathRef = FirebaseDatabase.getInstance().getReference("Owners/" + UserID + "/Path/");
            OwnerPathRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pathRef = dataSnapshot.getValue().toString();
                    String choose = pathRef + itemType;
                    Toast.makeText(context, "" + choose, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,""+this.itemType, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,""+this.itemName, Toast.LENGTH_SHORT).show();
                    databaseReference = FirebaseDatabase.getInstance().getReference(choose + "/" + itemName);
                    databaseReference.removeValue();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
