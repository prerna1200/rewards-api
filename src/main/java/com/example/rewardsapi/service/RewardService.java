package com.example.rewardsapi.service;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/* Simple, readable reward calculation. Uses BigDecimal for money safety. */
@Service
public class RewardService {

    private final TransactionRepository transactionRepository;

    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // kept same signature for compatibility
    public int calculatePoints(double amount) {
        return calculatePoints(BigDecimal.valueOf(amount));
    }

    // BigDecimal version used internally
    private int calculatePoints(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }

        // only whole dollars count
        int dollars = amount.setScale(0, RoundingMode.DOWN).intValue();

        int points = 0;
        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50; // 50 dollars between 50 and 100 => 50 points
        } else if (dollars > 50) {
            points += (dollars - 50);
        }
        return points;
    }

    // customerId -> (MONTH -> points, "TOTAL" -> total)
    public Map<String, Map<String, Integer>> calculateRewards() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Transaction t : transactions) {
            String customerId = t.getCustomerId();
            String month = t.getDate().getMonth().toString();
            int pts = calculatePoints(BigDecimal.valueOf(t.getAmount()));

            result.putIfAbsent(customerId, new HashMap<>());
            Map<String, Integer> monthly = result.get(customerId);

            monthly.put(month, monthly.getOrDefault(month, 0) + pts);
            monthly.put("TOTAL", monthly.getOrDefault("TOTAL", 0) + pts);
        }
        return result;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // single customer breakdown
    public Map<String, Integer> calculateRewardsForCustomer(String customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        Map<String, Integer> monthly = new HashMap<>();
        int total = 0;
        for (Transaction t : transactions) {
            String month = t.getDate().getMonth().toString();
            int pts = calculatePoints(BigDecimal.valueOf(t.getAmount()));
            monthly.put(month, monthly.getOrDefault(month, 0) + pts);
            total += pts;
        }
        monthly.put("TOTAL", total);
        return monthly;
    }

    // points per customer for a given month name (e.g. "JANUARY")
    public Map<String, Integer> calculateRewardsForMonth(String monthName) {
        String lookup = monthName.toUpperCase(Locale.ROOT);
        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, Integer> result = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getDate().getMonth().toString().equals(lookup)) {
                String customerId = t.getCustomerId();
                int pts = calculatePoints(BigDecimal.valueOf(t.getAmount()));
                result.put(customerId, result.getOrDefault(customerId, 0) + pts);
            }
        }
        return result;
    }
}