package com.example.rewardsapi.service.impl;

import com.example.rewardsapi.dto.MonthlyReward;
import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import com.example.rewardsapi.service.RewardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository repository;

    public RewardServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    private int calculatePoints(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        int dollars = amount.intValue();
        int points = 0;

        if (dollars > 100) {
            points += (dollars - 100) * 2;
            points += 50;
        } else if (dollars > 50) {
            points += (dollars - 50);
        }

        return points;
    }

    @Override
    public PaginationResponse<RewardResponse> getAllRewards(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> pageData = repository.findAll(pageable);

        Map<String, Map<String, Integer>> rewardMap = new HashMap<>();

        for (Transaction t : pageData.getContent()) {
            String customerId = t.getCustomerId();
            String month = t.getTransactionDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());

            rewardMap.putIfAbsent(customerId, new HashMap<>());
            Map<String, Integer> monthly = rewardMap.get(customerId);

            monthly.put(month, monthly.getOrDefault(month, 0) + points);
        }

        List<RewardResponse> responseList = new ArrayList<>();

        for (String customerId : rewardMap.keySet()) {

            Map<String, Integer> monthlyMap = rewardMap.get(customerId);
            List<MonthlyReward> monthlyList = new ArrayList<>();
            int total = 0;

            for (String month : monthlyMap.keySet()) {
                int pts = monthlyMap.get(month);
                total += pts;
                monthlyList.add(new MonthlyReward(month, pts));
            }

            responseList.add(new RewardResponse(customerId, monthlyList, total));
        }

        return new PaginationResponse<>(
                responseList,
                pageData.getNumber(),
                pageData.getTotalPages(),
                pageData.getTotalElements()
        );
    }

    @Override
    public RewardResponse getCustomerRewards(String customerId) {

        List<Transaction> transactions = repository.findByCustomerId(customerId);

        if (transactions.isEmpty()) {
            throw new RuntimeException("Customer exists but no transactions found");
        }

        Map<String, Integer> monthlyMap = new HashMap<>();

        for (Transaction t : transactions) {
            String month = t.getTransactionDate().getMonth().toString();
            int points = calculatePoints(t.getAmount());

            monthlyMap.put(month, monthlyMap.getOrDefault(month, 0) + points);
        }

        List<MonthlyReward> monthlyList = new ArrayList<>();
        int total = 0;

        for (String month : monthlyMap.keySet()) {
            int pts = monthlyMap.get(month);
            total += pts;
            monthlyList.add(new MonthlyReward(month, pts));
        }

        return new RewardResponse(customerId, monthlyList, total);
    }

    @Override
    public Map<String, Integer> getRewardsByMonth(String monthName) {

        String month = monthName.toUpperCase();
        List<Transaction> transactions = repository.findAll();

        Map<String, Integer> result = new HashMap<>();

        for (Transaction t : transactions) {

            if (t.getTransactionDate().getMonth().toString().equals(month)) {
                String customerId = t.getCustomerId();
                int points = calculatePoints(t.getAmount());

                result.put(customerId, result.getOrDefault(customerId, 0) + points);
            }
        }

        return result;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return repository.save(transaction);
    }
}