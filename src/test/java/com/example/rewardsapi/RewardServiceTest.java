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
import java.util.*;

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
        assertEquals(115, response.getTotalPoints());
    }

    @Test
    void getCustomerRewards_shouldReturnCorrectData() {

        Transaction t1 = new Transaction("C1", new BigDecimal("120"), LocalDate.of(2026, 1, 10));

        when(repo.findByCustomerId("C1"))
                .thenReturn(List.of(t1));

        RewardResponse response = service.getCustomerRewards("C1");

        assertEquals("C1", response.getCustomerId());
        assertEquals(90, response.getTotalPoints());
    }

    @Test
    void getCustomerRewards_shouldThrowException_whenNoData() {

        when(repo.findByCustomerId("C9"))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class,
                () -> service.getCustomerRewards("C9"));
    }

    @Test
    void getRewardsByMonth_shouldReturnCorrectResult() {

        Transaction t1 = new Transaction("C1", new BigDecimal("120"), LocalDate.of(2026, 1, 10));
        Transaction t2 = new Transaction("C2", new BigDecimal("80"), LocalDate.of(2026, 1, 15));

        when(repo.findAll())
                .thenReturn(Arrays.asList(t1, t2));

        Map<String, Integer> result = service.getRewardsByMonth("JANUARY");

        assertEquals(2, result.size());
        assertEquals(90, result.get("C1"));
        assertEquals(30, result.get("C2"));
    }

    @Test
    void saveTransaction_shouldReturnSavedTransaction() {

        Transaction t = new Transaction("C1", new BigDecimal("100"), LocalDate.now());

        when(repo.save(t)).thenReturn(t);

        Transaction saved = service.saveTransaction(t);

        assertNotNull(saved);
        assertEquals("C1", saved.getCustomerId());
    }

    @Test
    void calculatePoints_shouldThrowException_whenAmountNegative() {

        Transaction t = new Transaction("C1", new BigDecimal("-10"), LocalDate.now());

        when(repo.findAll(PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(List.of(t)));

        assertThrows(IllegalArgumentException.class,
                () -> service.getAllRewards(0, 5));
    }
}
