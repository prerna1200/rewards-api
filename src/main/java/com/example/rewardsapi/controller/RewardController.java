package com.example.rewardsapi.controller;

import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.dto.TransactionRequest;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// This controller handles all reward-related APIs
@RestController
@RequestMapping("/rewards")
public class RewardController {

    // calling service layer methods from here
    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    // get rewards for all customers with pagination
    @GetMapping
    public PaginationResponse<RewardResponse> getAllRewards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getAllRewards(page, size);
    }

    // get reward details for a single customer
    @GetMapping("/{customerId}")
    public RewardResponse getCustomerRewards(@PathVariable String customerId) {
        return service.getCustomerRewards(customerId);
    }

    // get rewards by month for all customers
    @GetMapping("/month/{month}")
    public Map<String, Integer> getRewardsByMonth(@PathVariable String month) {
        return service.getRewardsByMonth(month);
    }

    // add a new transaction
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> addTransaction(
            @Valid @RequestBody TransactionRequest request) {

        // converting request to entity
        Transaction transaction = new Transaction(
                request.getCustomerId(),
                request.getAmount(),
                request.getTransactionDate()
        );

        // saving transaction and returning response
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveTransaction(transaction));
    }
}
