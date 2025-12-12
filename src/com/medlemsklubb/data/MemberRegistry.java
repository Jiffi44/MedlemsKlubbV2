package com.medlemsklubb.data;

import com.medlemsklubb.head.Member;

import java.util.*;

public class MemberRegistry {

    private final List<Member> members = new ArrayList<>();

    public void add(Member member) {
        members.add(member);
    }

    public Optional<Member> findById(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return Optional.of(member);
            }
        }
        return Optional.empty();

    }

    public List<Member> findAll() {
        return new ArrayList<>(members);
    }

    public boolean removeById(int id) {
        return members.removeIf(member -> member.getId() == id);
    }
}
