package com.example.rewardsapi;

import com.example.rewardsapi.controller.RewardController;
import com.example.rewardsapi.dto.PaginationResponse;
import com.example.rewardsapi.dto.RewardResponse;
import com.example.rewardsapi.exception.GlobalExceptionHandler;
import com.example.rewardsapi.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        List<RewardResponse> list = new ArrayList<>();
        list.add(new RewardResponse("C1", new ArrayList<>(), 100));

        PaginationResponse<RewardResponse> response =
                new PaginationResponse<>(list, 0, 1, 1);

        when(rewardService.getAllRewards(anyInt(), anyInt()))
                .thenReturn(response);

        mvc.perform(get("/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customerId").value("C1"))
                .andExpect(jsonPath("$.data[0].totalPoints").value(100));
    }

    @Test
    void getCustomerRewards_success() throws Exception {

        RewardResponse response =
                new RewardResponse("C1", new ArrayList<>(), 200);

        when(rewardService.getCustomerRewards(anyString()))
                .thenReturn(response);

        mvc.perform(get("/rewards/C1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C1"))
                .andExpect(jsonPath("$.totalPoints").value(200));
    }

    @Test
    void getRewardsByMonth_success() throws Exception {

        Map<String, Integer> result = new HashMap<>();
        result.put("C1", 120);
        result.put("C2", 50);

        when(rewardService.getRewardsByMonth(anyString()))
                .thenReturn(result);

        mvc.perform(get("/rewards/month/january"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.C1").value(120))
                .andExpect(jsonPath("$.C2").value(50));
    }

    @Test
    void addTransaction_success() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("customerId", "C3");
        request.put("amount", 150);
        request.put("transactionDate", "2026-03-30");

        when(rewardService.saveTransaction(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("C3"));
    }
}