package com.example.rewardsapi;

import com.example.rewardsapi.controller.RewardController;
import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.exception.GlobalExceptionHandler;
import com.example.rewardsapi.model.Transaction;
import com.example.rewardsapi.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
@Import(GlobalExceptionHandler.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getAllRewards_success() throws Exception {

        List<RewardResponse> list =
                List.of(new RewardResponse("C1", new ArrayList<>(), 100));

        PaginationResponse<RewardResponse> response =
                new PaginationResponse<>(list, 0, 1, 1);

        when(rewardService.getAllRewards(anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/rewards?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customerId").value("C1"))
                .andExpect(jsonPath("$.data[0].totalPoints").value(100));
    }

    @Test
    void getCustomerRewards_success() throws Exception {

        when(rewardService.getCustomerRewards("C1"))
                .thenReturn(new RewardResponse("C1", new ArrayList<>(), 200));

        mvc.perform(get("/rewards/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C1"));
    }

    @Test
    void getCustomerRewards_notFound() throws Exception {

        when(rewardService.getCustomerRewards("X"))
                .thenThrow(new RuntimeException("Customer not found"));

        mvc.perform(get("/rewards/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    void getRewardsByMonth_success() throws Exception {

        Map<String, Integer> result = new HashMap<>();
        result.put("C1", 120);

        when(rewardService.getRewardsByMonth("JANUARY"))
                .thenReturn(result);

        mvc.perform(get("/rewards/month/JANUARY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.C1").value(120));
    }

    @Test
    void addTransaction_success() throws Exception {

        Transaction t = new Transaction("C3",
                new BigDecimal("150"),
                LocalDate.of(2026, 3, 30));

        when(rewardService.saveTransaction(any()))
                .thenReturn(t);

        Map<String, Object> request = new HashMap<>();
        request.put("customerId", "C3");
        request.put("amount", 150);
        request.put("transactionDate", "2026-03-30");

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("C3"));
    }

    @Test
    void addTransaction_invalidRequest() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("customerId", "");
        request.put("amount", -10);
        request.put("transactionDate", null);

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void addTransaction_invalidJson() throws Exception {

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"C1\",\"amount\":\"abc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid JSON request"));
    }
}