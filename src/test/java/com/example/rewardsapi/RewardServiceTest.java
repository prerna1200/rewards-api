package com.example.rewardsapi;

import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import com.example.rewardsapi.service.impl.RewardServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository repo;

    @InjectMocks
    private RewardServiceImpl service;

    @Test
    void getAllRewards_shouldCalculatePointsCorrectly() {

        Transaction t1 = new Transaction("C1", new BigDecimal("120"), LocalDate.of(2026, 1, 10));
        Transaction t2 = new Transaction("C1", new BigDecimal("75"), LocalDate.of(2026, 1, 15));

        List<Transaction> txs = Arrays.asList(t1, t2);

        when(repo.findAll(PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(txs));

        PaginationResponse<RewardResponse> result = service.getAllRewards(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getData().size());

        RewardResponse response = result.getData().get(0);

        assertEquals("C1", response.getCustomerId());
        assertEquals(115, response.getTotalPoints()); // 90 + 25
    }
}
