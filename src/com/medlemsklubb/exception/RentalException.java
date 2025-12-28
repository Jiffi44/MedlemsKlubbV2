package com.medlemsklubb.exception;

//används när en bokning inte kan göras
public class RentalException extends Exception {
    public RentalException(String message) {
        super(message);
    }
}
