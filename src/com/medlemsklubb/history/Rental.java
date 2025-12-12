package com.medlemsklubb.history;

import com.medlemsklubb.head.Item;
import com.medlemsklubb.head.Member;

import java.time.LocalDate;

// klass för hyrperiod kopplad till medlem och item
public class Rental {
    private final int rentalId; //vem som hyrde
    private final Member member;
    private final Item item;
    private final LocalDate startDate; // när det hyrdes
    private LocalDate endDate; // När det lämnades tillbaks
    private boolean active; // true tills booking är avslutad
    private double totalPrice; // totalpris för uthyrning

    //kontroktur
    public Rental(int rentalId, Member member, Item item) {
        this.rentalId = rentalId;
        this.member = member;
        this.item = item;
        this.startDate = LocalDate.now();
        this.active = true;
    }

    //uthyrning avslutas
    public void finish(LocalDate endDate) {
        this.endDate = endDate;
        this.active = false;
    }

    //getters
    public int getRentalId() {
        return rentalId;
    }

    public Member getMember() {
        return member;
    }

    public Item getItem() {
        return item;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    //setters

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    //retunerar info text om uthyrningen
    @Override
    public String toString() {
        return "member: : " + member.getFirstname() + " " + member.getLastname() + '\'' +
                "'Uthyrnings id: " + rentalId +
                "' item: : " + item.getItemName() + '\'' +
                ", Start datum för hyrperiod: " + startDate + '\'' +
                ", Slut datum för hyrperiod: " + (endDate == null ? "pågående uthyrning" : endDate) + '\'' +
                ", aktiv uthyrning: " + (active ? "ja" : "nej") +
                "'totalpris: " + totalPrice + " kr";
    }
}






