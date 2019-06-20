package com.satish.bunny.tanga.MethodClasses;

import com.satish.bunny.tanga.FireClasses.BillProducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bunny on 10/23/2017.
 */

public class BillAR {
    private String ItemName;
    private String ItemCost;
    private String ItemQuantity;
    int flag =0;
    int y =0;

    public static void setBPList(List<BillProducer> BPList) {
        BillAR.BPList = BPList;
    }

    static List<BillProducer> BPList = new ArrayList<BillProducer>();
    public void Adder(String itemName, String itemCost, String itemQuantity){
        ItemName = itemName;
        ItemCost = itemCost;
        ItemQuantity = itemQuantity;
        BillProducer BP = new BillProducer(ItemName,ItemCost,ItemQuantity);
        int size = BPList.size();
        for (BillProducer x : BPList) {
            if(ItemName.equals(x.getItemName()) && itemCost=="0")
            {
                flag = 2;
                y =BPList.indexOf(x);
                break;
            }
            else if(ItemName.equals(x.getItemName()))
            {
                flag=1;
                x.setItemCost(ItemCost);
                x.setItemQuantity(ItemQuantity);
                break;
            }
        }
        if(flag == 0)
        {
            BPList.add(BP);
        }
        else if(flag == 2)
        {
            BPList.remove(y);
        }
    }

    public static List<BillProducer> getBPList() {
        return BPList;
    }

    public static void clearBPList(){
        BPList.clear();
    }
}
