package com.medlemsklubb.exception;

//används när användaren skriver in felaktig medlemsdata
public class InvalidMemberDataException extends Exception {
    public InvalidMemberDataException(String message) {
            super(message);
        }
    }

