package com.satish.bunny.tanga.FireClasses;

/**
 * Created by Bunny on 9/17/2017.
 */

public class Menu {
    String itemName,itemCost,itemType;
    public Menu(){}

    public Menu(String itemCost, String itemName, String itemType) {
        this.itemName = itemName;
        this.itemCost = itemCost;
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCost() {
        return itemCost;
    }

    public String getItemType() { return itemType; }
}
