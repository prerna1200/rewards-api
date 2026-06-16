package com.example.rewardsapi.service;

import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.model.Transaction;

import java.util.Map;

public interface RewardService {

    PaginationResponse<RewardResponse> getAllRewards(int page, int size);

    RewardResponse getCustomerRewards(String customerId);

    Map<String, Integer> getRewardsByMonth(String monthName);

    Transaction saveTransaction(Transaction transaction);
}