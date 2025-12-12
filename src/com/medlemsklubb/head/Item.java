package com.medlemsklubb.head;

public abstract class Item {
    private final String itemId; //id för varje item
    protected String itemName; //item namn
    protected double price; // pris

    //konstruktor
    public Item( String itemId, String itemName, double price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
    }

    //getters

    public String itemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public abstract void info();

    public abstract boolean requiresLicense();
}

