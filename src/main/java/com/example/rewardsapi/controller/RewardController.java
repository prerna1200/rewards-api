package com.example.rewardsapi.controller;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.service.RewardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller responsible for exposing APIs related to
 * customer reward calculations.
 */
@RestController
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Endpoint to fetch monthly and total reward points
     * for all customers.
     *
     * @return map containing customer-wise reward details
     */
    @GetMapping("/rewards")
    public Map<String, Map<String, Integer>> getRewards() {
        return rewardService.calculateRewards();
    }

    /**
     * Endpoint to fetch monthly and total reward points
     * for a single customer.
     *
     * Example: GET /rewards/C1
     *
     * @param customerId customer identifier
     * @return monthly map + TOTAL for the customer or 404 if not found
     */
    @GetMapping("/rewards/{customerId}")
    public ResponseEntity<Map<String, Integer>> getRewardsForCustomer(@PathVariable String customerId) {
        Map<String, Map<String, Integer>> all = rewardService.calculateRewards();
        Map<String, Integer> customerData = all.get(customerId);
        if (customerData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", 0));
        }
        return ResponseEntity.ok(customerData);
    }

    /**
     * Endpoint to fetch reward points for every customer for a specific month.
     *
     * Example: GET /rewards/month/JANUARY
     *
     * Note: month should match the month-key used in the service output (e.g. JANUARY).
     *
     * @param month month name (case-insensitive)
     * @return map customerId -> points for that month (0 if none)
     */
    @GetMapping("/rewards/month/{month}")
    public Map<String, Integer> getRewardsForMonth(@PathVariable String month) {

        String lookup = month.toUpperCase();

        Map<String, Integer> result = new HashMap<>();
        Map<String, Map<String, Integer>> all = rewardService.calculateRewards();

        for (Map.Entry<String, Map<String, Integer>> entry : all.entrySet()) {
            String customerId = entry.getKey();
            Map<String, Integer> monthlyMap = entry.getValue();
            int points = monthlyMap.getOrDefault(lookup, 0);
            result.put(customerId, points);
        }

        return result;
    }

    /**
     * Endpoint to add a new transaction to the data store.
     *
     * Example:
     * POST /transactions
     * {
     *   "customerId": "C3",
     *   "amount": 150,
     *   "date": "2026-03-30"
     * }
     *
     * @param transaction transaction to store
     * @return saved transaction with HTTP 201 Created
     */
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction saved = rewardService.saveTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}