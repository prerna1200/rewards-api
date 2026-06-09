package com.example.rewardsapi.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * DTO to send reward response in structured format
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardResponse {

    private String customerId;
    private Map<String, Integer> monthlyRewards;
    private int totalPoints;

    // No-arg constructor required by Jackson for deserialization
    public RewardResponse() {}

    public RewardResponse(String customerId, Map<String, Integer> monthlyRewards, int totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    // If you prefer immutability, remove setters — kept here for simplicity & Jackson.
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setMonthlyRewards(Map<String, Integer> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    public void setTotalPoints(int totalPoints) {
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

    @Override
    public String toString() {
        return "RewardResponse{" +
                "customerId='" + customerId + '\'' +
                ", monthlyRewards=" + monthlyRewards +
                ", totalPoints=" + totalPoints +
                '}';
    }
}