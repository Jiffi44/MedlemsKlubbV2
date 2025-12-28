package com.medlemsklubb.businesslogic;

import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.head.Member;
import com.medlemsklubb.head.StatusLevel;
import com.medlemsklubb.exception.InvalidMemberDataException;

import java.util.Optional;

//hanterar affärslogiken

public class MemberService {

    private final MemberRegistry registry;

    //konstruktor
    public MemberService(MemberRegistry registry) {
        this.registry = registry;
    }

    //skapa och spara en ny medlem. Varje fel kastas i domän exception
    public Member create(String firstName, String lastName, String level, boolean license) throws InvalidMemberDataException {

        if (level == null || firstName.trim().isEmpty()) {
            throw new InvalidMemberDataException("Förnamn får inte vara tomt");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidMemberDataException("Efternamn får inte vara tomt");
        }

        String normalizedLevel = normalizedLevel(level); //Normalisera status

        Member member = new Member(firstName, lastName, level, license);
        registry.add(member);
        return member;
    }

    // uppdatera namn
    public boolean updateName(int memberId, String firstName, String lastName) throws InvalidMemberDataException {

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidMemberDataException("Förnamn får inte vara tomt");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidMemberDataException("Efternamn får inte vara tomt");
        }
        Optional<Member> opt = registry.findById(memberId);
        if (opt.isEmpty()) {
            return false;
        }

        Member member = opt.get();
        member.setFirstname(firstName);
        member.setLastname(lastName);
        return true; // boolean tilldelas värdet true om medlem fanns och ändrades
    }

    // uppdatera leevel
    public boolean updateLevel(int memberId, String level) throws InvalidMemberDataException {

        String normalizedLevel = normalizedLevel(level);

        Optional<Member> opt = registry.findById(memberId);
        if (opt.isEmpty()) {
            return false;
        }

        opt.get().setLevel(normalizedLevel);
        return true;
    }

    public boolean updateLicense(int memberId, boolean hasLicense) {
        Optional<Member> opt = registry.findById(memberId);
        if (opt.isEmpty()) return false;
        opt.get().setLicense(hasLicense);
        return true;
    }


    private String normalizedLevel(String level) throws InvalidMemberDataException {
        if (level == null || level.isBlank()) {
            return StatusLevel.STANDARD;
        }
String normalized = level.trim().toUpperCase();

        if (!StatusLevel.STANDARD.equals(normalized) && !StatusLevel.STUDENT.equals(normalized) && !StatusLevel.SENIOR.equals(normalized)) {
            throw new InvalidMemberDataException("Ogiltig status.");
        }
        return normalized;
    }
}
