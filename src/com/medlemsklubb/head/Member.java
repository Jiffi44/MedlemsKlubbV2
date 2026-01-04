package com.medlemsklubb.head;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// klass för medlem
public class Member {

    private int id; // id
    private String firstname; //förnamn
    private String lastname; // efternamn
    private String level; //satus/nivå (standard, senior, student)
    private boolean license; // körkort eller inte
    private List<String> history = new ArrayList<>(); //Lista som sparar historik

    //Konstruktor
    public Member(String firstname, String lastname, String level, boolean license) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.level = level;
        this.license = license;
    }

    //konstruktor för filinläsning
    public Member(int id, String firstname, String lastname, String level, boolean license) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.level = level;
        this.license = license;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLevel() {
        return level;
    }

    public List<String> getHistory() {
        return history;
    }

    public boolean hasLicense() {
        return license;
    }

//setters

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLevel(String level) {
        if (level == null || level.trim().isEmpty()) {
            this.level = StatusLevel.STANDARD;
        }
        else {
            this.level = level.trim().toUpperCase();
        }
    }
    public boolean setLicense(boolean licens) {
        this.license = licens;
        return licens;
    }
    public boolean hasLicensed() {
        return license;
    }

    public void addHistory(String line) {
        history.add(line);
    }

    @Override
    public String toString() {
        return "Medlem " +
                "id='" + id + '\'' +
                ", Förnamn='" + firstname + '\'' +
                ", Efternamn='" + lastname + '\'' +
                ", Status='" + level + '\'' +
                ", Licens=" + (license ? "JA" : "NEJ");
    }


}

