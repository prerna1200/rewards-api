package com.example.rewardsapi;

import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.repository.TransactionRepository;
import com.example.rewardsapi.service.RewardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository repo;

    @InjectMocks
    private RewardService service;

    @Test
    void calculatePoints_basicCases() {
        assertEquals(0, service.calculatePoints(0));
        assertEquals(0, service.calculatePoints(50));
        assertEquals(1, service.calculatePoints(51));
        assertEquals(90, service.calculatePoints(120)); // example from spec
    }

    @Test
    void calculateRewards_aggregatesMonthlyAndTotal() {
        Transaction t1 = new Transaction("C1", 120, LocalDate.of(2026, 1, 10)); // 90
        Transaction t2 = new Transaction("C1", 75, LocalDate.of(2026, 1, 15));  // 25
        Transaction t3 = new Transaction("C2", 110, LocalDate.of(2026, 2, 18)); // 70

        List<Transaction> txs = Arrays.asList(t1, t2, t3);
        when(repo.findAll()).thenReturn(txs);

        Map<String, Map<String, Integer>> result = service.calculateRewards();

        assertNotNull(result);
        assertTrue(result.containsKey("C1"));
        assertEquals(115, result.get("C1").get("JANUARY").intValue());
        assertEquals(115, result.get("C1").get("TOTAL").intValue());

        assertTrue(result.containsKey("C2"));
        assertEquals(70, result.get("C2").get("FEBRUARY").intValue());
        assertEquals(70, result.get("C2").get("TOTAL").intValue());
    }
}