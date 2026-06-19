package com.example.rewardsapi.dto;

/**
 * Represents reward points for a specific month.
 * Used in the API response to show monthly breakdown.
 */
public class MonthlyReward {

    private String month;
    private int points;

    // default constructor
    public MonthlyReward() {}

    /**
     * creates a monthly reward object with month and points
     *
     * @param month  month name (e.g., JANUARY)
     * @param points reward points for that month
     */
    public MonthlyReward(String month, int points) {
        this.month = month;
        this.points = points;
    }

    // returns month name
    public String getMonth() {
        return month;
    }

    // returns reward points
    public int getPoints() {
        return points;
    }
}