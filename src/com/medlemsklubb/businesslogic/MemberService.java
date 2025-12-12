package com.medlemsklubb.businesslogic;

import com.medlemsklubb.data.MemberRegistry;
import com.medlemsklubb.head.Member;
import com.medlemsklubb.head.StatusLevel;

import java.util.Optional;

//hanterar affärslogiken

public class MemberService {

    private final MemberRegistry registry;

    //konstruktor
    public MemberService(MemberRegistry registry) {
        this.registry = registry;
    }

    //skapa och spara en ny medlem
    public Member create(String firstName, String lastName, String level, boolean license) {

        if (level == null || level.isBlank()) {
            level = StatusLevel.STANDARD;
        }
        Member member = new Member(firstName, lastName, level, license);
        registry.add(member);
        return member;
    }

    // uppdatera namn
    public boolean updateName(int memberId, String firstName, String lastName) {
        Optional<Member> opt = registry.findById(memberId);
        if (opt.isEmpty())
            return false;
        Member member = opt.get();
        member.setFirstname(firstName);
        member.setLastname(lastName);
        return true; // boolean tilldelas värdet true om medlem fanns och ändrades
    }
// uppdatera leevel
        public boolean updateLevel(int memberId, String level) {
            Optional<Member> opt = registry.findById(memberId);
            if (opt.isEmpty()) {
                return false;
            }
            if (level == null || level.isBlank()) {
                level = StatusLevel.STANDARD;
            }
            opt.get().setLevel(level);
            return true;

        }
}
