package com.medlemsklubb.head;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

// klass för medlem
public class Member {
    private static int counter = 50000; // varje medlem får ett unikt id
    private final int id; // id (kan inte ändras)
    private String firstname; //förnamn
    private String lastname; // efternamn
    private String level; //satus/nivå (standard, senior, student)
    private boolean license; // körkort eller inte
    private List<String> history = new ArrayList<>(); //Lista som sparar historik

    //Konstruktor
    public Member(String firstname, String lastname, String level, boolean license) {
        this.id = ++counter; // id skapas och ökar med 1
        this.firstname = firstname;
        this.lastname = lastname;
        this.level = (level == null || level.isBlank())  // standard status om null
                ? StatusLevel.STANDARD
                : level;
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

//setters

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLevel(String level) {
        this.level = (level ==  null || level.isBlank()) ? StatusLevel.STANDARD : level;
    }
    public boolean setLicense(boolean licens) {
        return licens;
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

