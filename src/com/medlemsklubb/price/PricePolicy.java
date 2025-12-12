package com.medlemsklubb.price;

public interface PricePolicy {
    double calculate(double originalPrice, int hours);
}
