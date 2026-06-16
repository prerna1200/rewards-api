package com.example.rewardsapi.dto;

import java.util.List;

/**
 * Represents reward details for a customer.
 * Includes monthly rewards and total points.
 */
public class RewardResponse {

    private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;

    // default constructor
    public RewardResponse() {}

    /**
     * creates reward response for a customer
     *
     * @param customerId customer id
     * @param monthlyRewards list of monthly rewards
     * @param totalPoints total reward points
     */
    public RewardResponse(String customerId, List<MonthlyReward> monthlyRewards, int totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    // returns customer id
    public String getCustomerId() {
        return customerId;
    }

    // returns monthly reward details
    public List<MonthlyReward> getMonthlyRewards() {
        return monthlyRewards;
    }

    // returns total points
    public int getTotalPoints() {
        return totalPoints;
    }
}