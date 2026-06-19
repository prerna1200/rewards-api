package com.example.rewardsapi;

import com.example.rewardsapi.dto.*;
import com.example.rewardsapi.model.Transaction;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DTOTest {

    @Test
    void test_Transaction_model() {

        Transaction t = new Transaction("C1",
                new BigDecimal("120"),
                LocalDate.of(2026, 1, 10));

        assertEquals("C1", t.getCustomerId());
        assertEquals(new BigDecimal("120"), t.getAmount());
        assertEquals(LocalDate.of(2026, 1, 10), t.getTransactionDate());
    }

    @Test
    void test_TransactionRequest() {

        TransactionRequest req = new TransactionRequest();

        req.setCustomerId("C1");
        req.setAmount(new BigDecimal("200"));
        req.setTransactionDate(LocalDate.of(2026, 2, 1));

        assertEquals("C1", req.getCustomerId());
        assertEquals(new BigDecimal("200"), req.getAmount());
        assertEquals(LocalDate.of(2026, 2, 1), req.getTransactionDate());
    }

    @Test
    void test_MonthlyReward() {

        MonthlyReward m = new MonthlyReward("JANUARY", 100);

        assertEquals("JANUARY", m.getMonth());
        assertEquals(100, m.getPoints());
    }

    @Test
    void test_RewardResponse() {

        MonthlyReward m = new MonthlyReward("JANUARY", 50);

        RewardResponse r = new RewardResponse(
                "C1",
                List.of(m),
                50
        );

        assertEquals("C1", r.getCustomerId());
        assertEquals(50, r.getTotalPoints());
        assertEquals(1, r.getMonthlyRewards().size());
    }

    @Test
    void test_PaginationResponse() {

        List<String> data = List.of("A", "B");

        PaginationResponse<String> p =
                new PaginationResponse<>(data, 0, 1, 2);

        assertEquals(2, p.getData().size());
        assertEquals(0, p.getCurrentPage());
        assertEquals(1, p.getTotalPages());
        assertEquals(2, p.getTotalElements());
    }
}
