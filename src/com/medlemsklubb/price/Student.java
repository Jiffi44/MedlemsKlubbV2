package com.medlemsklubb.price;

public class Student implements PricePolicy {
    private static final double discountRate = 0.05;

    @Override
    public double calculate(double originalPrice, int hours) {
        return originalPrice * hours * (1 - discountRate);
    }
}
