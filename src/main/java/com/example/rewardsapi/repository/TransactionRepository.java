package com.example.rewardsapi.repository;

import com.example.rewardsapi.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Acts as in-memory data source
@Repository
public class TransactionRepository {

    public List<Transaction> getAllTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        // Sample data for 3 months and multiple customers
        transactions.add(new Transaction("C1", 120, LocalDate.of(2026, 1, 10)));
        transactions.add(new Transaction("C1", 75, LocalDate.of(2026, 1, 15)));
        transactions.add(new Transaction("C1", 200, LocalDate.of(2026, 2, 5)));
        transactions.add(new Transaction("C1", 90, LocalDate.of(2026, 3, 20)));

        transactions.add(new Transaction("C2", 60, LocalDate.of(2026, 1, 12)));
        transactions.add(new Transaction("C2", 110, LocalDate.of(2026, 2, 18)));
        transactions.add(new Transaction("C2", 45, LocalDate.of(2026, 3, 25)));

        return transactions;
    }
}