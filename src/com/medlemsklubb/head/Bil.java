package com.medlemsklubb.head;

public class Bil extends Item { // klassen Bil ärver fråån item

    //unika attribut
    private String brand; //vilket märke bilen har
    private String  color; //färg
    private boolean license; //behövs körkort eller inte

    //konstruktor
    public Bil(String itemId, String itemName, double price, String brand, String color, boolean license) {
        super(itemId, itemName, price);
        this.brand = brand;
        this.color = color;
        this.license = license;
    }

    //metod som skriver ut info om fordonet
    @Override
    public void info() {
       System.out.println( "Fordon: " + itemName + '\'' +
                ", Typ av fordon: " + brand + '\'' +
                ", Pris: " + price + '\'' +
                ", Färg: " + color + '\'' +
                ",Körkort krävs" + (license ? "JA" : "NEJ"));
    }
    //retunerar om fordonet kräver körkort
    @Override
    public boolean requiresLicense() {
        return license;
    }

}
