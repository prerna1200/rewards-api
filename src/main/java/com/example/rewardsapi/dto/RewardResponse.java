package com.example.rewardsapi.dto;

import java.util.Map;

// DTO to send reward response in structured format
public class RewardResponse {

    private String customerId;
    private Map<String, Integer> monthlyRewards;
    private int totalPoints;

    public RewardResponse(String customerId, Map<String, Integer> monthlyRewards, int totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<String, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
