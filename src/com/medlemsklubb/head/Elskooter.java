package com.medlemsklubb.head;

public class Elskooter extends Item { // klassen Elskooter ärver från item

    //unika attribut
    private String type; // typ av fordon
    private boolean licens; //krävs körkort eller inte
    private int maxSpeed; //max hastighet

    //konstruktor
    public Elskooter(String itemId, String itemName, double price, String type, boolean licens, int maxSpeed) {
        super(itemId, itemName, price);
        this.type = type;
        this.licens = licens;
        this.maxSpeed = maxSpeed;
    }

    //getters och setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean isLicens() {
        return licens;
    }
    public void setLicens(boolean licens) {
        this.licens = licens;
    }
    public int getMaxSpeed() {
        return maxSpeed;
    }
    @Override //metod som skriver ut info om fordonet
    public void info () {
        System.out.println(
                "Fordon: " + itemName + '\'' +
                        ", Typ av fordon: " + type + '\'' +
                ", Pris: " + price + '\'' +
                        ", Maxhastighet: " + maxSpeed + '\'' +
                        ",Körkort krävs" + (licens ? "JA" : "NEJ")

        );
    }

    //retunerar om fordonet kräver körkort
    @Override
    public boolean requiresLicense() {
        return licens;
    }

}
