package com.example.rewardsapi;

import com.example.rewardsapi.controller.RewardController;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for RewardController.
 * Uses @WebMvcTest to load only MVC components and the controller.
 */
@WebMvcTest(controllers = RewardController.class)
@Import(GlobalExceptionHandler.class) // include global exception handling
class RewardControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RewardService rewardService;

    // ObjectMapper that registers Java Time modules so LocalDate serializes/deserializes correctly
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void getRewards_returnsAggregatedMap() throws Exception {
        Map<String, Integer> c1 = new HashMap<>();
        c1.put("TOTAL", 405);
        Map<String, Map<String, Integer>> all = new HashMap<>();
        all.put("C1", c1);

        when(rewardService.calculateRewards()).thenReturn(all);

        mvc.perform(get("/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.C1.TOTAL").value(405));
    }

    @Test
    void getRewardsForCustomer_notFound_returns404() throws Exception {
        when(rewardService.calculateRewards()).thenReturn(Collections.emptyMap());

        mvc.perform(get("/rewards/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(0));
    }

    @Test
    void getRewardsForMonth_returnsCustomerPointsForMonth() throws Exception {
        Map<String, Integer> c1Monthly = new HashMap<>();
        c1Monthly.put("JANUARY", 115);
        c1Monthly.put("TOTAL", 115);

        Map<String, Integer> c2Monthly = new HashMap<>();
        c2Monthly.put("JANUARY", 10);
        c2Monthly.put("TOTAL", 10);

        Map<String, Map<String, Integer>> all = new HashMap<>();
        all.put("C1", c1Monthly);
        all.put("C2", c2Monthly);

        when(rewardService.calculateRewards()).thenReturn(all);

        mvc.perform(get("/rewards/month/january"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.C1").value(115))
                .andExpect(jsonPath("$.C2").value(10));
    }

    @Test
    void postTransaction_returnsCreatedTransaction() throws Exception {
        Transaction in = new Transaction("C3", 150, LocalDate.of(2026, 3, 30));
        Transaction saved = new Transaction(1L, "C3", 150, LocalDate.of(2026, 3, 30));

        when(rewardService.saveTransaction(any(Transaction.class))).thenReturn(saved);

        String json = mapper.writeValueAsString(in);

        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("C3"))
                .andExpect(jsonPath("$.id").value(1));
    }
}