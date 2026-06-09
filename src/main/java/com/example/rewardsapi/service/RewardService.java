package com.example.rewardsapi.service;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for reward calculation logic.
 * It processes customer transactions and calculates monthly
 * as well as total reward points.
 */
@Service
public class RewardService {

    private final TransactionRepository transactionRepository;

    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Calculates reward points based on transaction amount.
     *
     * Rules:
     * - 2 points for every dollar spent above 100
     * - 1 point for every dollar spent between 50 and 100
     * - No points for amount <= 50
     *
     * Implementation notes:
     * - truncates fractional dollars by casting to int (per-dollar points).
     */
    public int calculatePoints(double amount) {

        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }

        int dollars = (int) amount; // truncate fractional part
        int points = 0;

        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50; // (100 - 50) * 1
        } else if (dollars > 50) {
            points += (dollars - 50);
        }

        return points;
    }

    /**
     * Calculates monthly and total reward points for each customer.
     *
     * The result map structure:
     * CustomerId → { Month → Points, TOTAL → Total Points }
     *
     * @return map containing reward breakdown per customer
     */
    public Map<String, Map<String, Integer>> calculateRewards() {

        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Transaction t : transactions) {

            String customerId = t.getCustomerId();
            String month = t.getDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());

            result.putIfAbsent(customerId, new HashMap<>());
            Map<String, Integer> monthlyData = result.get(customerId);

            monthlyData.put(month, monthlyData.getOrDefault(month, 0) + points);
            monthlyData.put("TOTAL", monthlyData.getOrDefault("TOTAL", 0) + points);
        }

        return result;
    }

    /**
     * Persist a transaction (used by POST /transactions).
     * Returns the saved transaction (with id when using JPA).
     */
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Convenience: calculate rewards for a single customer.
     */
    public Map<String, Integer> calculateRewardsForCustomer(String customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);

        Map<String, Integer> monthly = new HashMap<>();
        int total = 0;

        for (Transaction t : transactions) {
            String month = t.getDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());
            monthly.put(month, monthly.getOrDefault(month, 0) + points);
            total += points;
        }

        monthly.put("TOTAL", total);
        return monthly;
    }

    /**
     * Convenience: for a given month (name, e.g. JANUARY) calculate points per customer.
     */
    public Map<String, Integer> calculateRewardsForMonth(String monthName) {
        String lookup = monthName.toUpperCase();
        List<Transaction> transactions = transactionRepository.findAll();

        Map<String, Integer> result = new HashMap<>();
        for (Transaction t : transactions) {
            if (t.getDate().getMonth().toString().equals(lookup)) {
                String customerId = t.getCustomerId();
                int points = calculatePoints(t.getAmount());
                result.put(customerId, result.getOrDefault(customerId, 0) + points);
            }
        }
        return result;
    }
}