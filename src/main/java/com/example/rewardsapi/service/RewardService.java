package com.example.rewardsapi.service;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Handles reward calculation logic
@Service
public class RewardService {

    private final TransactionRepository transactionRepository;

    public RewardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Calculate points based on amount
    public int calculatePoints(double amount) {

        int points = 0;

        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50;
        } else if (amount > 50) {
            points += (amount - 50);
        }

        return points;
    }

    // Calculate monthly + total rewards per customer
    public Map<String, Map<String, Integer>> calculateRewards() {

        List<Transaction> transactions = transactionRepository.getAllTransactions();

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
}
