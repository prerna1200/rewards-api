package com.example.rewardsapi.controller;

import com.example.rewardsapi.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}