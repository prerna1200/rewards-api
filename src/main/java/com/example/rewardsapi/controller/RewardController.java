package com.example.rewardsapi.controller;
import com.example.rewardsapi.service.RewardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// REST API for reward details
@RestController
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/rewards")
    public Map<String, Map<String, Integer>> getRewards() {
        return rewardService.calculateRewards();
    }
}