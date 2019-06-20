package com.satish.bunny.tanga.FireClasses;

/**
 * Created by Bunny on 10/22/2017.
 */

public class BillProducer {
    String itemName;
    String itemCost;
    String itemQuantity;

    public BillProducer(){}

    public BillProducer(String itemName, String itemCost,String itemQuantity) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCost() {
        return itemCost;
    }

    public String getItemQuantity(){return itemQuantity;}

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
