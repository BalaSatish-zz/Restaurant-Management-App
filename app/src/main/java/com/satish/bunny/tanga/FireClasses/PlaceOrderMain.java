package com.satish.bunny.tanga.FireClasses;

/**
 * Created by Bunny on 11/23/2017.
 */

public class PlaceOrderMain {
    String itemName;
    String itemQuantity;

    public PlaceOrderMain() {
    }

    public PlaceOrderMain(String itemName, String itemQuantity) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }
}
