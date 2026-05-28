package com.example.rewardsapi.model;

import java.time.LocalDate;

/**
 * Represents a transaction made by a customer.
 * Contains customer identifier, transaction amount and date.
 */
public class Transaction {

    private String customerId;
    private double amount;
    private LocalDate date;

    public Transaction() {}

    public Transaction(String customerId, double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
