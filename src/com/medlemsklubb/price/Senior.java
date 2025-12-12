package com.medlemsklubb.price;

public class Senior implements PricePolicy {
    private static final double discountRate = 0.10;

    @Override
    public double calculate(double originalPrice, int hours) {
        return originalPrice * hours * (1 - discountRate);
    }
}
