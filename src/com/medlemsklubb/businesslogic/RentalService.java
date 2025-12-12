package com.medlemsklubb.businesslogic;

import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.data.Invetory;
import com.medlemsklubb.head.*;
import com.medlemsklubb.history.Rental;
import com.medlemsklubb.price.*;
import java.time.temporal.ChronoUnit;

import java.util.*;
import java.time.LocalDate;

public class RentalService {

    private final Invetory invetory; // invetory för hyrbara items
    private MemberRegistry members; // register för medlemmar
    private final List<Rental> rentals = new ArrayList<>(); // lista för alla uthyrningar
    private int counter = 1; //räknare till uthyrning

    //konstruktor
    public RentalService(Invetory invetory, MemberRegistry members) {
        this.invetory = invetory;
        this.members = members;
    }

    // välj prispolicy beroende på medlemmens statud
    private PricePolicy policyfor(String level) {
        if (StatusLevel.STUDENT.equals(level))
            return new Student();
        if (StatusLevel.SENIOR.equals(level))
            return new Senior();
        return new Standard();
    }

    //kod för att boka en item
    public Optional<Rental> book(int memberId, String itemID, LocalDate endDate) {

        //hitta medlem
        Optional<Member> memberOpt = members.findById(memberId);
        if (memberOpt.isEmpty()) {
            System.out.println("Det finns ingen medlem med: " + memberId + " med id nummer.");
            return Optional.empty();
        }

        //hitta item
        Optional<Item> itemOpt = invetory.findById(itemID);
        if (itemOpt.isEmpty()) {
            System.out.println("Det finns ingen item: " + itemID + " med id nummer.");
            return Optional.empty();
        }

        Member member = memberOpt.get();
        Item item = itemOpt.get();

        //Pris policy utifrån medlems status
        PricePolicy policy = policyfor(member.getLevel());

        LocalDate start = LocalDate.now(); // räknar timmar
        long days = ChronoUnit.DAYS.between(start, endDate);
        if (days <= 0) {
        days = 1; //minst en dag uthyrning
        }

        int hours = (int) (days * 24); // gör om till timmar

        double total = policy.calculate(item.getPrice(), hours);

        Rental rental = new Rental(counter++, member, item);
        rental.setTotalPrice(total); //priset kopplas nu till bokningen

        rentals.add(rental);

        member.addHistory(
                "Bokade'" + item.getItemName() +
                        "'Uthyrnings id: " + rental.getRentalId() +
                        "' från " + rental.getStartDate() +
                        "' till " + endDate +
                        "' pris: " + total + " kr"
        );

        return Optional.of(rental);
    }

    public List<Rental> getAllRentalals() {
        return new ArrayList<>(rentals);
    }
}
