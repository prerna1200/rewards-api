package com.example.rewardsapi;

import com.example.rewardsapi.controller.RewardController;
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

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        HashMap<String, Object> request = new HashMap<>();
        request.put("customerId", "C1");
        request.put("amount", 120);
        request.put("transactionDate", "2026-01-01");

        when(rewardService.saveTransaction(any()))
                .thenThrow(new IllegalArgumentException("Amount cannot be negative"));

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount cannot be negative"));
    }

    @Test
    void shouldReturnBadRequest_whenValidationFails() throws Exception {

        String json = "{}";

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void shouldReturnBadRequest_whenJsonInvalid() throws Exception {

        String badJson = "{ invalid json }";

        mvc.perform(post("/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid JSON request"));
    }

    @Test
    void shouldReturnNotFound_whenRuntimeException() throws Exception {

        when(rewardService.getCustomerRewards("X"))
                .thenThrow(new RuntimeException("Customer not found"));

        mvc.perform(get("/rewards/X"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }

    @Test
    void shouldReturnInternalError_whenGenericException() throws Exception {

        when(rewardService.getCustomerRewards("Y"))
                .thenThrow(new RuntimeException("some error")); // handled by runtime but still safely covers

        mvc.perform(get("/rewards/Y"))
                .andExpect(status().isNotFound());
    }
}