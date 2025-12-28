package com.medlemsklubb.businesslogic;

import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.data.Inventory;
import com.medlemsklubb.exception.RentalException;
import com.medlemsklubb.head.*;
import com.medlemsklubb.history.Rental;
import com.medlemsklubb.price.*;

import java.time.temporal.ChronoUnit;

import java.util.*;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class RentalService {

    private final Inventory invetory; // invetory för hyrbara items
    private MemberRegistry members; // register för medlemmar
    private final List<Rental> rentals = new ArrayList<>(); // lista för alla uthyrningar
    private final AtomicInteger rentalIdCounter = new AtomicInteger(1); //räknare till uthyrning

    //konstruktor
    public RentalService(Inventory invetory, MemberRegistry members) {
        this.invetory = invetory;
        this.members = members;
    }

    public Rental book(int memberIdm, String itemId, LocalDate endDate) throws RentalException {

        if (itemId == null || itemId.trim().isEmpty()) {
            throw new RentalException("item id saknas");
        }
        if (endDate == null) {
            throw new RentalException("end date saknas");
        }
        Member member = members.findById(memberIdm).orElseThrow(() -> new RentalException("Det finns ingen med detta id"));

        Item item = invetory.findById(itemId.trim().toUpperCase()).orElseThrow(() -> new RentalException("Det finns ingen item med detta id."));

        if (item.requiresLicense() && !member.hasLicense()) {
            throw new RentalException("Medlemmen har inget körkort.");
        }

        LocalDate start = LocalDate.now(); // räknar timmar
        long days = ChronoUnit.DAYS.between(start, endDate);
        if (days <= 0) {
            days = 1; //minst en dag uthyrning
        }

        int hours = (int) (days * 24); // gör om till timmar

        PricePolicy policy = policyFor(member.getLevel());
        double total = policy.calculate(item.getPrice(), hours);

        int rentalId = rentalIdCounter.getAndIncrement();
        Rental rental = new Rental(rentalId, member, item);
        rental.setTotalPrice(total); //priset kopplas nu till bokningen

        rentals.add(rental);

        member.addHistory(
                "Bokade'" + item.getItemName() +
                        "'Uthyrnings id: " + rental.getRentalId() +
                        "' från " + rental.getStartDate() +
                        "' till " + endDate +
                        "' pris: " + total + " kr"
        );

        return rental;
    }

    public Rental finishRental(int rentalId) throws RentalException {

        for (Rental rental : rentals) {
            if (rental.getRentalId() == rentalId && rental.isActive()) {

                rental.finish(LocalDate.now());

                rental.getMember().addHistory(
                        "Avslutade uthyrning: " + rentalId + " " + "Item: " + rental.getItem() + " " + "Datum: " + LocalDate.now()
                );
                return rental;
            }
        }

        throw new RentalException("ingen aktiv uthyrning hittades.");

    }

    public List<Rental> getAllRentalals() {
        return new ArrayList<>(rentals);
    }

    private PricePolicy policyFor(String level){
        String normalized = (level == null) ? StatusLevel.STANDARD : level.trim().toUpperCase();

        if (StatusLevel.STUDENT.equals(normalized)) {
            return new Student();
        }

        if (StatusLevel.SENIOR.equals(normalized)) {
            return new Senior();
        }
        return new Standard();
    }
}
