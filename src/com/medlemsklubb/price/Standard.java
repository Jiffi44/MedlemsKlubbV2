package com.medlemsklubb.price;

public class Standard implements PricePolicy {
    @Override
    public double calculate(double originalPrice, int hours) {
        return originalPrice * hours;
    }
}
