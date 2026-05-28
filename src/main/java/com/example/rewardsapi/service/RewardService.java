package com.example.rewardsapi.service;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param amount transaction amount
     * @return calculated reward points
     */
    public int calculatePoints(double amount) {

        // validate input
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be negative");
        }

        int points = 0;

        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50;
        } else if (amount > 50) {
            points += (amount - 50);
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

        List<Transaction> transactions = transactionRepository.getAllTransactions();

        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Transaction t : transactions) {

            String customerId = t.getCustomerId();
            String month = t.getDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());

            result.putIfAbsent(customerId, new HashMap<>());

            Map<String, Integer> monthlyData = result.get(customerId);

            // accumulate monthly points
            monthlyData.put(month, monthlyData.getOrDefault(month, 0) + points);

            // accumulate total points
            monthlyData.put("TOTAL", monthlyData.getOrDefault("TOTAL", 0) + points);
        }

        return result;
    }
}