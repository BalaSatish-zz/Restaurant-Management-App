package com.satish.bunny.tanga.MethodClasses;

import com.satish.bunny.tanga.FireClasses.BillProducer;
import com.satish.bunny.tanga.FireClasses.PlaceOrderMain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bunny on 11/23/2017.
 */

public class PlaceOrderHelper {
    public PlaceOrderHelper(String BasicPath, String TableNumber, List<BillProducer> BPL) {
        DatabaseReference firebaseDatabase;
        String Path = BasicPath;
        String OrdersPath = Path+"Orders/"+TableNumber+"/";
        List<BillProducer> BPList;
        List<PlaceOrderMain> OrderPlacingList;
        BPList = new ArrayList<BillProducer>();
        BPList = BPL;
        OrderPlacingList = new ArrayList<PlaceOrderMain>();

        for(BillProducer x : BPList)
        {
            PlaceOrderMain pom = new PlaceOrderMain(x.getItemName(),x.getItemQuantity()) ;
            OrderPlacingList.add(pom);
        }
        firebaseDatabase = FirebaseDatabase.getInstance().getReference(OrdersPath);
        for(PlaceOrderMain x : OrderPlacingList){
            firebaseDatabase.child(x.getItemName()).setValue(x);
        }

    }
}
